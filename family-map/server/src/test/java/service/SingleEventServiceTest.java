package service;

import DAO.*;
import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.SingleEventRequest;
import response.SingleEventResponse;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class SingleEventServiceTest {
    private Database database;

    private EventDAO eventDAO;
    private AuthTokenDAO authTokenDAO;

    private Event event;
    private AuthToken authtoken;

    private String eventID;
    private String associatedUsername;
    private String personID;
    private float latitude;
    private float longitude;
    private String country;
    private String city;
    private String eventType;
    private int year;

    private SingleEventRequest request;
    private SingleEventResponse response;

    private void connectToDatabase() throws DataAccessException {
        database = new Database();
        Connection connection = database.getConnection();

        eventDAO = new EventDAO(connection);
        authTokenDAO = new AuthTokenDAO(connection);
    }

    private void disconnectFromDatabase() throws DataAccessException {
        database.closeConnection(true);
    }

    @BeforeEach
    public void setUp() throws DataAccessException {
        ClearService.clear();

        connectToDatabase();

        eventID = "shop";
        associatedUsername = "jay";
        personID = "shilongcui";
        latitude = 35.9f;
        longitude = 140.1f;
        country = "Japan";
        city = "Osaka";
        eventType = "Shopping";
        year = 2000;

        event = new Event(
                eventID,
                associatedUsername,
                personID,
                latitude,
                longitude,
                country,
                city,
                eventType,
                year
        );
        eventDAO.insert(event);
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
    public void getEventPass() {
        request = new SingleEventRequest(event.getEventID(), authtoken.getTokenStr());

        response = SingleEventService.getEvent(request);

        assertEquals(eventID, response.getEventID());
        assertEquals(associatedUsername, response.getAssociatedUsername());
        assertEquals(personID, response.getPersonID());
        assertEquals(latitude, response.getLatitude());
        assertEquals(longitude, response.getLongitude());
        assertEquals(country, response.getCountry());
        assertEquals(city, response.getCity());
        assertEquals(eventType, response.getEventType());
        assertEquals(year, response.getYear());
        assertTrue(response.isSuccess());
    }

    @Test
    public void getEventFail() {
        request = new SingleEventRequest(event.getEventID(), "doge");

        response = SingleEventService.getEvent(request);

        assertEquals("Error: request event does not belong to this user", response.getMessage());
        assertFalse(response.isSuccess());
    }
}
