package service;

import DAO.*;
import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoadRequest;
import response.LoadResponse;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class LoadServiceTest {
    private User user;
    private Person person;
    private Event event;

    private ArrayList<User> users;
    private ArrayList<Person> people;
    private ArrayList<Event> events;

    private LoadRequest request;
    private LoadResponse response;

    @BeforeEach
    public void setUp() throws DataAccessException {
        user = new User(
                "murasame",
                "kusanagi",
                "murasame@yakuza.edu",
                "shiba",
                "inu",
                "m",
                "123456"
        );
        users = new ArrayList<>();
        users.add(user);
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
        people = new ArrayList<>();
        people.add(person);
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
        events = new ArrayList<>();
        events.add(event);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {}

    @Test
    public void loadPass() throws DataAccessException {
        request = new LoadRequest(users, people, events);

        response = LoadService.load(request);

        assertEquals("Successfully added 1 users, 1 persons, and 1 events to the database.", response.getMessage());
        assertTrue(response.isSuccess());
    }
}
