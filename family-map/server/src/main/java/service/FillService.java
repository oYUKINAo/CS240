package service;

import DAO.*;
import DAO.Database;
import model.Event;
import model.Person;
import model.User;
import request.FillRequest;
import response.FillResponse;

/**
 * FillService class: Populates the server's database with generated data for the specified username.
 */
public class FillService {
    public static FillResponse fill(FillRequest request) {
        Database database = new Database();

        FillResponse response;

        // retrieve information from request
        String username = request.getUsername();
        int numGenerations = request.getNumGenerations();

        try {
            // make sure request doesn't have any missing/invalid values
            if (username == null || username.isEmpty() || numGenerations < 0) {
                response = new FillResponse("Error: missing/invalid value in FillRequest");
            }
            else {
                // username must be a user already registered with the server
                UserDAO userDAO = new UserDAO(database.getConnection());
                User user = userDAO.findUser(username);
                if (user == null) {
                    response = new FillResponse("Error: username not registered");
                }
                else {
                    // if there's any data in the database already associated with the given username, it is deleted
                    PersonDAO personDAO = new PersonDAO(database.getConnection());
                    personDAO.delete(username);
                    EventDAO eventDAO = new EventDAO(database.getConnection());
                    eventDAO.delete(username);

                    // generate ancestor data for the user
                    Relatives relatives = new Relatives(
                            user.getPersonID(),
                            user.getUsername(),
                            user.getFirstName(),
                            user.getLastName(),
                            user.getGender()
                    );
                    relatives.generateAncestors(numGenerations);

                    // add the ancestor data to the Persons table and the Events table
                    for (Person relative : relatives.getPeople()) {
                        personDAO.insert(relative);
                    }
                    for (Event event : relatives.getEvents()) {
                        eventDAO.insert(event);
                    }

                    response = new FillResponse(relatives.getPeople().size(), relatives.getEvents().size());
                }
            }
        }
        catch (DataAccessException e) {
            response = new FillResponse(e.getMessage());
        }
        finally {
            try {
                database.closeConnection(true);
            }
            catch (DataAccessException e) {
                response = new FillResponse(e.getMessage());
            }
        }
        return response;
    }
}
