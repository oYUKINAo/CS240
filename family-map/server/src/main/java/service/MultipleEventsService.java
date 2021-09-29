package service;

import DAO.*;
import model.AuthToken;
import model.Event;
import request.MultipleEventsRequest;
import response.MultipleEventsResponse;

import java.util.ArrayList;

/**
 * MultipleEventsService class: Returns ALL events for ALL family members of the current user.
 * The current user is determined from the provided auth token.
 */
public class MultipleEventsService {
    public static MultipleEventsResponse getEvents(MultipleEventsRequest request) {
        Database database = new Database();

        MultipleEventsResponse response;

        // retrieve information from request
        String authTokenStr = request.getAuthTokenStr();

        try {
            AuthTokenDAO authTokenDAO = new AuthTokenDAO(database.getConnection());
            AuthToken authToken = authTokenDAO.findToken(authTokenStr);

            // make sure the auth token is valid
            if (authToken == null || authToken.getUsername() == null) {
                response = new MultipleEventsResponse("Error: invalid auth token");
            }
            else {
                String username = authToken.getUsername();
                EventDAO eventDAO = new EventDAO(database.getConnection());
                ArrayList<Event> events = eventDAO.findEvents(username);
                response = new MultipleEventsResponse(events);
            }
        }
        catch (DataAccessException e) {
            response = new MultipleEventsResponse(e.getMessage());
        }
        finally {
            try {
                database.closeConnection(true);
            }
            catch (DataAccessException e) {
                response = new MultipleEventsResponse(e.getMessage());
            }
        }
        return response;
    }
}
