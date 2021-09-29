package service;

import DAO.*;
import model.User;
import model.AuthToken;
import request.LoginRequest;
import response.LoginResponse;

import java.util.UUID;

/**
 * LoginService class: Logs in the user and returns an auth token.
 */
public class LoginService {
    /**
     * @param request Request Body
     * @return Response Body
     */
    public static LoginResponse login(LoginRequest request) {
        Database database = new Database();

        LoginResponse response = null;

        // retrieve the username and password from request
        String username = request.getUsername();
        String password = request.getPassword();

        try {
            // retrieve the user from Users: make sure the user is in Users
            UserDAO userDAO = new UserDAO(database.getConnection());
            User user = userDAO.findUser(username);
            if (user == null) {
                response = new LoginResponse("Error: user not found in the database");
            }
            else {
                // make sure the password matches the user's password
                if (!password.equals(user.getPassword())) {
                    response = new LoginResponse("Error: wrong password");
                }
                else {
                    // personID of the user's generated Person object
                    String personID = user.getPersonID();

                    // create an auth token for the user
                    String authTokenStr = UUID.randomUUID().toString();
                    AuthTokenDAO authTokenDAO = new AuthTokenDAO(database.getConnection());
                    authTokenDAO.insert(new AuthToken(username, authTokenStr));

                    response = new LoginResponse(authTokenStr, username, personID);
                }
            }
        }
        catch (DataAccessException e) {
            response = new LoginResponse(e.getMessage());
        }
        finally {
            try {
                database.closeConnection(true);
            }
            catch (DataAccessException e) {
                response = new LoginResponse(e.getMessage());
            }
        }
        return response;
    }
}
