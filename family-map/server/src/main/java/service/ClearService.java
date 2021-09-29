package service;

import DAO.*;
import response.ClearResponse;

/**
 * ClearService class: Deletes ALL data from the database, including user accounts, auth tokens, and generated person and event data.
 */
public class ClearService {
    /**
     * @return Response Body
     */
    public static ClearResponse clear() {
        Database database = new Database();

        ClearResponse response = null;

        try {
            UserDAO userDAO = new UserDAO(database.getConnection());
            PersonDAO personDAO = new PersonDAO(database.getConnection());
            EventDAO eventDAO = new EventDAO(database.getConnection());
            AuthTokenDAO authTokenDAO = new AuthTokenDAO(database.getConnection());

            userDAO.clear();
            personDAO.clear();
            eventDAO.clear();
            authTokenDAO.clear();

            response = new ClearResponse();
        }
        catch (DataAccessException e) {
            response = new ClearResponse(e.getMessage());
        }
        finally {
            try {
                database.closeConnection(true);
            }
            catch (DataAccessException e) {
                response = new ClearResponse(e.getMessage());
            }
        }
        return response;
    }
}
