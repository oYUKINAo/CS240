package request;

import java.util.ArrayList;
import model.User;
import model.Person;
import model.Event;

/**
 * LoadRequest class: makes an request to the LoadService class
 */
public class LoadRequest {
    /**
     * Users: Array of User objects
     */
    private ArrayList<User> users;
    /**
     * Persons: Array of Person objects
     */
    private ArrayList<Person> persons;
    /**
     * Events: Array of Event objects
     */
    private ArrayList<Event> events;

    /**
     * Constructor
     * @param users Array of User objects
     * @param persons Array of Person objects
     * @param events Array of Event objects
     */
    public LoadRequest(ArrayList<User> users, ArrayList<Person> persons, ArrayList<Event> events) {
        this.users = users;
        this.persons = persons;
        this.events = events;
    }

    public ArrayList<User> getUsers() {
        return users;
    }
    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
    public ArrayList<Person> getPersons() {
        return persons;
    }
    public void setPersons(ArrayList<Person> people) {
        this.persons = people;
    }
    public ArrayList<Event> getEvents() {
        return events;
    }
    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }
}
