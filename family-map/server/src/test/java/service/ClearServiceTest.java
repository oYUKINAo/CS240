package service;

import DAO.*;
import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.ClearRequest;
import response.ClearResponse;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class ClearServiceTest {
    private Database database;
    
    private UserDAO userDAO;
    private PersonDAO personDAO;
    private EventDAO eventDAO;
    private AuthTokenDAO authTokenDAO;
    
    private User user;
    private Person person;
    private Event event;
    private AuthToken authtoken;
    
    private ClearRequest request;
    private ClearResponse response;

    private void connectToDatabase() throws DataAccessException {
        database = new Database();
        Connection connection = database.getConnection();

        userDAO = new UserDAO(connection);
        personDAO = new PersonDAO(connection);
        eventDAO = new EventDAO(connection);
        authTokenDAO = new AuthTokenDAO(connection);
    }

    private void disconnectFromDatabase() throws DataAccessException {
        database.closeConnection(true);
    }

    @BeforeEach
    public void setUp() throws DataAccessException {
        connectToDatabase();

        user = new User(
                "murasame",
                "kusanagi",
                "murasame@yakuza.edu",
                "shiba",
                "inu",
                "m",
                "123456"
        );
        person = new Person(
                "1910",
                "ww2",
                "Chinese",
                "soldier",
                "m",
                "1888",
                "1890",
                "1912"
        );
        event = new Event(
                "shop",
                "jay",
                "shilongjaycui",
                35.9f,
                140.1f,
                "Japan",
                "Ushiku",
                "Shop",
                2020
        );
        authtoken = new AuthToken(
                "murasame",
                "kusanagi"
        );

        userDAO.insert(user);
        personDAO.insert(person);
        eventDAO.insert(event);
        authTokenDAO.insert(authtoken);

        disconnectFromDatabase();
        
        request = new ClearRequest();
    }

    @AfterEach
    public void tearDown() throws DataAccessException {}

    @Test
    public void clearPass() throws DataAccessException {
        response = ClearService.clear();

        assertEquals("Clear succeeded.", response.getMessage());
        assertTrue(response.isSuccess());

        connectToDatabase();

        assertNull(userDAO.findUser(user.getUsername()));
        assertNull(personDAO.findPerson(person.getPersonID()));
        assertNull(eventDAO.findEvent(event.getEventID()));
        assertNull(authTokenDAO.findToken(authtoken.getTokenStr()));

        disconnectFromDatabase();
    }
}
