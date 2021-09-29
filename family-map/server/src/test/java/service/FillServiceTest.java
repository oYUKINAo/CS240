package service;

import DAO.*;
import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.FillRequest;
import request.RegisterRequest;
import response.FillResponse;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class FillServiceTest {
    private Database database;

    private UserDAO userDAO;
    private PersonDAO personDAO;
    private EventDAO eventDAO;

    private String username;
    private int numGenerations;

    private FillRequest request;
    private FillResponse response;

    private void connectToDatabase() throws DataAccessException {
        database = new Database();
        Connection connection = database.getConnection();

        userDAO = new UserDAO(connection);
        personDAO = new PersonDAO(connection);
        eventDAO = new EventDAO(connection);
    }

    private void disconnectFromDatabase() throws DataAccessException {
        database.closeConnection(true);
    }

    @BeforeEach
    public void setUp() throws DataAccessException {
        username = "shilongcui";
        numGenerations = 4;

        RegisterService.register(new RegisterRequest(
                "shilongcui",
                "130551",
                "seryu.lucky@gmail.com",
                "jay",
                "cui",
                "m")
        );
    }

    @AfterEach
    public void tearDown() throws DataAccessException {}

    @Test
    public void fillPass() throws DataAccessException {
        request = new FillRequest(username, numGenerations);

        response = FillService.fill(request);

        assertEquals("Successfully added 31 persons and 93 events to the database.", response.getMessage());
        assertTrue(response.isSuccess());

        connectToDatabase();

        ArrayList<Person> people = personDAO.findPeople(username);
        assertEquals(31, people.size());
        ArrayList<Event> events = eventDAO.findEvents(username);
        assertEquals(93, events.size());

        disconnectFromDatabase();
    }

    @Test
    public void fillFail() {
        request = new FillRequest("doge", numGenerations);

        response = FillService.fill(request);

        assertEquals("Error: username not registered", response.getMessage());
        assertFalse(response.isSuccess());
    }
}
