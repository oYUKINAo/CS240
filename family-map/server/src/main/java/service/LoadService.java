package service;

import DAO.*;
import model.User;
import model.Person;
import model.Event;
import request.LoadRequest;
import response.LoadResponse;

import java.util.ArrayList;

/**
 * LoadService class: Clears all data from the database (just like the /clear API),
 * and then loads the posted user, person, and event data into the database.
 */
public class LoadService {
    /**
     * @param request Request Body
     * @return Response Body
     */
    public static LoadResponse load(LoadRequest request) {
        Database database = new Database();

        LoadResponse response = null;

        try {
            UserDAO userDAO = new UserDAO(database.getConnection());
            PersonDAO personDAO = new PersonDAO(database.getConnection());
            EventDAO eventDAO = new EventDAO(database.getConnection());
            AuthTokenDAO authTokenDAO = new AuthTokenDAO(database.getConnection());

            userDAO.clear();
            personDAO.clear();
            eventDAO.clear();
            authTokenDAO.clear();

            ArrayList<User> users = request.getUsers();
            ArrayList<Person> persons = request.getPersons();
            ArrayList<Event> events = request.getEvents();

            for (User user : users) {
                userDAO.insert(user);
            }
            for (Person person : persons) {
                personDAO.insert(person);
            }
            for (Event event : events) {
                eventDAO.insert(event);
            }

            response = new LoadResponse(users.size(), persons.size(), events.size());
        }
        catch (DataAccessException e) {
            response = new LoadResponse(e.getMessage());
        }
        finally {
            try {
                database.closeConnection(true);
            }
            catch (DataAccessException e) {
                response = new LoadResponse(e.getMessage());
            }
        }
        return response;
    }
}
