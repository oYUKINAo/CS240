package service;

import DAO.*;
import model.AuthToken;
import model.Person;
import request.SinglePersonRequest;
import response.SinglePersonResponse;

/**
 * SinglePersonService class: Returns the single Person object with the specified ID.
 */
public class SinglePersonService {
    public static SinglePersonResponse getPerson(SinglePersonRequest request) {
        Database database = new Database();

        SinglePersonResponse response;

        // retrieve information from request
        String personID = request.getPersonID();
        String authTokenStr = request.getAuthTokenStr();

        try {
            // make sure the personID parameter is valid
            if (personID == null || personID.equals("")) {
                response = new SinglePersonResponse("Error: invalid personID parameter");
            }
            else {
                PersonDAO personDAO = new PersonDAO(database.getConnection());
                AuthTokenDAO authTokenDAO = new AuthTokenDAO(database.getConnection());

                Person person = personDAO.findPerson(personID);
                AuthToken authToken = authTokenDAO.findToken(authTokenStr);

                // make sure the auth token is valid and the request person belong to the user
                if (authToken == null || !person.getAssociatedUsername().equals(authToken.getUsername())) {
                    response = new SinglePersonResponse("Error: request person does not belong to this user");
                }
                else {
                    String associatedUsername = person.getAssociatedUsername();

                    String firstName = person.getFirstName();
                    String lastName = person.getLastName();
                    String gender = person.getGender();
                    String fatherID = person.getFatherID();
                    String motherID = person.getMotherID();
                    String spouseID = person.getSpouseID();

                    response = new SinglePersonResponse(
                            associatedUsername,
                            personID,
                            firstName,
                            lastName,
                            gender,
                            fatherID,
                            motherID,
                            spouseID
                    );
                }
            }
        }
        catch (DataAccessException e) {
            response = new SinglePersonResponse(e.getMessage());
        }
        finally {
            try {
                database.closeConnection(true);
            }
            catch (DataAccessException e) {
                response = new SinglePersonResponse(e.getMessage());
            }
        }
        return response;
    }
}
