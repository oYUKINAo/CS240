package service;

import DAO.*;
import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.SinglePersonRequest;
import response.SinglePersonResponse;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class SinglePersonServiceTest {
    private Database database;

    private PersonDAO personDAO;
    private AuthTokenDAO authTokenDAO;

    private Person person;
    private AuthToken authtoken;

    private String personID;
    private String associatedUsername;
    private String firstName;
    private String lastName;
    private String gender;
    private String fatherID;
    private String motherID;
    private String spouseID;

    private SinglePersonRequest request;
    private SinglePersonResponse response;

    private void connectToDatabase() throws DataAccessException {
        database = new Database();
        Connection connection = database.getConnection();

        personDAO = new PersonDAO(connection);
        authTokenDAO = new AuthTokenDAO(connection);
    }

    private void disconnectFromDatabase() throws DataAccessException {
        database.closeConnection(true);
    }

    @BeforeEach
    public void setUp() throws DataAccessException {
        ClearService.clear();

        connectToDatabase();

        personID = "1910";
        associatedUsername = "jay";
        firstName = "Chinese";
        lastName = "soldier";
        gender = "m";
        fatherID = "1888";
        motherID = "1890";
        spouseID = "1912";

        person = new Person(
                personID,
                associatedUsername,
                firstName,
                lastName,
                gender,
                fatherID,
                motherID,
                spouseID
        );
        personDAO.insert(person);
        authtoken = new AuthToken(
                "jay",
                "cui"
        );
        authTokenDAO.insert(authtoken);

        disconnectFromDatabase();
    }

    @AfterEach
    public void tearDown() throws DataAccessException {}

    @Test
    public void getPersonPass() {
        request = new SinglePersonRequest(person.getPersonID(), authtoken.getTokenStr());

        response = SinglePersonService.getPerson(request);

        assertEquals(personID, response.getPersonID());
        assertEquals(associatedUsername, response.getAssociatedUsername());
        assertEquals(firstName, response.getFirstName());
        assertEquals(lastName, response.getLastName());
        assertEquals(gender, response.getGender());
        assertEquals(fatherID, response.getFatherID());
        assertEquals(motherID, response.getMotherID());
        assertEquals(spouseID, response.getSpouseID());
    }

    @Test
    public void getPersonFail() {
        request = new SinglePersonRequest(person.getPersonID(), "doge");

        response = SinglePersonService.getPerson(request);

        assertEquals("Error: request person does not belong to this user", response.getMessage());
        assertFalse(response.isSuccess());
    }
}
