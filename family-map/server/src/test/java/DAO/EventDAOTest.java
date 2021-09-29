package DAO;

import model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class EventDAOTest {
    private Database database;
    private Event event1;
    private Event event2;
    private Event event3;
    private EventDAO eventDAO;

    @BeforeEach
    public void setUp() throws DataAccessException
    {
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        database = new Database();
        //and a new event with random data
        event1 = new Event(
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
        event2 = new Event(
                "cook",
                "jay",
                "shilongjaycui",
                35.9f,
                140.1f,
                "Japan",
                "Ushiku",
                "Cook",
                2020
        );
        event3 = new Event(
                "eat",
                "jay",
                "shilongjaycui",
                35.9f,
                140.1f,
                "Japan",
                "Ushiku",
                "Eat",
                2020
        );
        //Here, we'll open the connection in preparation for the test case to use it
        Connection conn = database.getConnection();
        //Let's clear the database as well so any lingering data doesn't affect our tests
        database.clearTables();
        //Then we pass that connection to the EventDAO so it can access the database
        eventDAO = new EventDAO(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        //Here we close the connection to the database file so it can be opened elsewhere.
        //We will leave commit to false because we have no need to save the changes to the database
        //between test cases
        database.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        //While insert returns a bool we can't use that to verify that our function actually worked
        //only that it ran without causing an error
        eventDAO.insert(event1);
        //So lets use a find method to get the event that we just put in back out
        Event cloneEvent = eventDAO.findEvent(event1.getEventID());
        //First lets see if our find found anything at all. If it did then we know that if nothing
        //else something was put into our database, since we cleared it in the beginning
        assertNotNull(cloneEvent);
        //Now lets make sure that what we put in is exactly the same as what we got out. If this
        //passes then we know that our insert did put something in, and that it didn't change the
        //data in any way
        assertEquals(event1, cloneEvent);
    }

    @Test
    public void insertFail() throws DataAccessException {
        //lets do this test again but this time lets try to make it fail
        //if we call the method the first time it will insert it successfully
        eventDAO.insert(event1);
        //but our sql table is set up so that "eventID" must be unique. So trying to insert it
        //again will cause the method to throw an exception
        //Note: This call uses a lambda function. What a lambda function is is beyond the scope
        //of this class. All you need to know is that this line of code runs the code that
        //comes after the "()->" and expects it to throw an instance of the class in the first parameter.
        assertThrows(DataAccessException.class, ()-> eventDAO.insert(event1));
    }

    @Test
    public void findEventPass() throws DataAccessException {
        String eventID = event1.getEventID();
        eventDAO.insert(event1);
        Event cloneEvent = eventDAO.findEvent(eventID);
        assertNotNull(cloneEvent);
        assertEquals(eventID, cloneEvent.getEventID());
    }

    @Test
    public void findEventFail() throws DataAccessException {
        String eventID = event1.getEventID();
        assertNull(eventDAO.findEvent(eventID));
    }

    @Test
    public void findEventsPass() throws DataAccessException {
        String associatedUsername = event1.getAssociatedUsername();
        eventDAO.insert(event1);
        eventDAO.insert(event2);
        eventDAO.insert(event3);
        ArrayList<Event> events = eventDAO.findEvents(associatedUsername);
        assertEquals(events.size(), 3);
    }

    @Test
    public void findEventsFail() throws DataAccessException {
        String associatedUsername = event1.getAssociatedUsername();
        ArrayList<Event> events = eventDAO.findEvents(associatedUsername);
        assertNull(events);
    }

    @Test
    void deletePass1() throws DataAccessException {
        String associatedUsername = event1.getAssociatedUsername();
        eventDAO.insert(event1);
        eventDAO.delete(associatedUsername);
        String eventID = event1.getEventID();
        assertNull(eventDAO.findEvent(eventID));
    }

    @Test
    void deletePass2() throws DataAccessException {
        String associatedUsername = event1.getAssociatedUsername();
        eventDAO.insert(event1);
        eventDAO.insert(event2);
        eventDAO.insert(event3);
        eventDAO.delete(associatedUsername);
        String event1ID = event1.getEventID();
        String event2ID = event2.getEventID();
        String event3ID = event3.getEventID();
        assertNull(eventDAO.findEvent(event1ID));
        assertNull(eventDAO.findEvent(event2ID));
        assertNull(eventDAO.findEvent(event3ID));
    }

    @Test
    public void clearPass() throws DataAccessException {
        String eventID = event1.getEventID();
        eventDAO.insert(event1);
        assertEquals(eventID, eventDAO.findEvent(eventID).getEventID());
        eventDAO.clear();
        assertNull(eventDAO.findEvent(eventID));
    }
}
