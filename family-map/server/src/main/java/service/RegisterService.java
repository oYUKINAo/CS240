package service;

import DAO.*;
import model.AuthToken;
import model.Event;
import model.Person;
import model.User;
import request.LoginRequest;
import request.RegisterRequest;
import response.RegisterResponse;

import java.util.UUID;

/**
 * RegisterService class: Creates a new user account,
 * generates 4 generations of ancestor data for the new user,
 * logs the user in,
 * and returns an auth token.
 */
public class RegisterService {
    /**
     * @param request Request Body
     * @return Response Body
     */
    public static RegisterResponse register(RegisterRequest request) {
        Database database = new Database();

        RegisterResponse response;

        // retrieve information from request
        String username = request.getUsername();
        String password = request.getPassword();
        String email = request.getEmail();
        String firstName = request.getFirstName();
        String lastName = request.getLastName();
        String gender = request.getGender();

        try {
            // make sure request doesn't have any missing/invalid values
            if (username == null || password == null || email == null || firstName == null || lastName == null || gender == null) {
                response = new RegisterResponse("Error: missing value in RegisterRequest");
            }
            else if (username.isEmpty() || password.isEmpty() || email.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || gender.isEmpty()) {
                response = new RegisterResponse("Error: invalid value in RegisterRequest");
            }
            else {
                UserDAO userDAO = new UserDAO(database.getConnection());
                // make sure that username isn't already taken by another user
                if (userDAO.findUser(username) != null) {
                    response = new RegisterResponse("Error: username already taken");
                }
                else {
                    // create a new user account: create new User object and add it to the Users table
                    String personID = UUID.randomUUID().toString();
                    User user = new User(username, password, email, firstName, lastName, gender, personID);
                    userDAO.insert(user);

                    // generate 4 generations of ancestor data for the new user
                    Relatives relatives = new Relatives(personID, username, firstName, lastName, gender);
                    relatives.generateAncestors(4);

                    // add the ancestor data to the Persons table and the Events table
                    PersonDAO personDAO = new PersonDAO(database.getConnection());
                    for (Person relative : relatives.getPeople()) {
                        personDAO.insert(relative);
                    }
                    EventDAO eventDAO = new EventDAO(database.getConnection());
                    for (Event event : relatives.getEvents()) {
                        eventDAO.insert(event);
                    }

                    // logs the user in
                    LoginService.login(new LoginRequest(username, password));

                    // return an auth token
                    String tokenStr = UUID.randomUUID().toString();
                    AuthTokenDAO authTokenDAO = new AuthTokenDAO(database.getConnection());
                    authTokenDAO.insert(new AuthToken(username, tokenStr));

                    response = new RegisterResponse(tokenStr, username, personID);
                }
            }
        }
        catch (DataAccessException e) {
            response = new RegisterResponse(e.getMessage());
        }
        finally {
            try {
                database.closeConnection(true);
            }
            catch (DataAccessException e) {
                response = new RegisterResponse(e.getMessage());
            }
        }
        return response;
    }
}