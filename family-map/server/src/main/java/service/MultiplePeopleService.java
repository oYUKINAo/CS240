package service;

import DAO.*;
import model.AuthToken;
import model.Person;
import request.MultiplePeopleRequest;
import response.MultiplePeopleResponse;

import java.util.ArrayList;

/**
 * Returns ALL family members of the current user.
 * The current user is determined from the provided auth token.
 */
public class MultiplePeopleService {
    public static MultiplePeopleResponse getPeople(MultiplePeopleRequest request) {
        Database database = new Database();

        MultiplePeopleResponse response;

        // retrieve information from request;
        String authTokenStr = request.getAuthToken();

        try {
            AuthTokenDAO authTokenDAO = new AuthTokenDAO(database.getConnection());
            AuthToken authToken = authTokenDAO.findToken(authTokenStr);

            // make sure the auth token is valid
            if (authToken == null || authToken.getUsername() == null) {
                response = new MultiplePeopleResponse("Error: invalid auth token");
            }
            else {
                String username = authToken.getUsername();
                PersonDAO personDAO = new PersonDAO(database.getConnection());
                ArrayList<Person> people = personDAO.findPeople(username);
                response = new MultiplePeopleResponse(people);
            }
        }
        catch (DataAccessException e) {
            response = new MultiplePeopleResponse(e.getMessage());
        }
        finally {
            try {
                database.closeConnection(true);
            }
            catch (DataAccessException e) {
                response = new MultiplePeopleResponse(e.getMessage());
            }
        }
        return response;
    }
}