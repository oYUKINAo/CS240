package service;

import DAO.*;
import model.AuthToken;
import model.Event;
import request.SingleEventRequest;
import response.SingleEventResponse;

/**
 * SingleEventService: Returns the single Event object with the specified ID.
 */
public class SingleEventService {
    public static SingleEventResponse getEvent(SingleEventRequest request) {
        Database database = new Database();

        SingleEventResponse response;

        // retrieve information from request;
        String eventID = request.getEventID();
        String authTokenStr = request.getAuthTokenStr();

        try {
            // make sure the eventID parameter is valid
            if (eventID == null || eventID.equals("")) {
                response = new SingleEventResponse("Error: invalid eventID parameter");
            }
            else {
                EventDAO eventDAO = new EventDAO(database.getConnection());
                AuthTokenDAO authTokenDAO = new AuthTokenDAO(database.getConnection());

                Event event = eventDAO.findEvent(eventID);
                AuthToken authToken = authTokenDAO.findToken(authTokenStr);

                // make sure the auth token is valid and the request event belong to the user
                if (authToken == null || !event.getAssociatedUsername().equals(authToken.getUsername())) {
                    response = new SingleEventResponse("Error: request event does not belong to this user");
                }
                else {
                    String associatedUsername = event.getAssociatedUsername();

                    String personID = event.getPersonID();
                    double latitude = event.getLatitude();
                    double longitude = event.getLongitude();
                    String country = event.getCountry();
                    String city = event.getCity();
                    String eventType = event.getEventType();
                    int year = event.getYear();

                    response = new SingleEventResponse(
                            associatedUsername,
                            eventID,
                            personID,
                            latitude,
                            longitude,
                            country,
                            city,
                            eventType,
                            year
                    );
                }
            }
        }
        catch (DataAccessException e) {
            response = new SingleEventResponse(e.getMessage());
        }
        finally {
            try {
                database.closeConnection(true);
            }
            catch (DataAccessException e) {
                response = new SingleEventResponse(e.getMessage());
            }
        }
        return response;
    }
}
