package DAO;

import model.Event;

import java.sql.*;
import java.util.ArrayList;

/**
 * EventDAO class: access the Events table in the database
 */
public class EventDAO {
    private final Connection connection;

    public EventDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Add a Event record to the Events table
     * @param event the Event object we want to add
     */
    public void insert(Event event) throws DataAccessException {

        if (findEvent(event.getEventID()) != null) {
            throw new DataAccessException("Error: eventID already exists in the database");
        }

        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO Events (EventID, AssociatedUsername, PersonID, Latitude, Longitude, " +
                "Country, City, EventType, Year) VALUES(?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            statement.setString(1, event.getEventID());
            statement.setString(2, event.getAssociatedUsername());
            statement.setString(3, event.getPersonID());
            statement.setFloat(4, event.getLatitude());
            statement.setFloat(5, event.getLongitude());
            statement.setString(6, event.getCountry());
            statement.setString(7, event.getCity());
            statement.setString(8, event.getEventType());
            statement.setInt(9, event.getYear());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database", e);
        }
    }

    /**
     * Find an event in the Events table by eventID
     * @param eventID the ID of the event we're trying to find
     * @return what we find
     */
    public Event findEvent(String eventID) throws DataAccessException {
        Event event;
        ResultSet set = null;
        String sql = "SELECT * FROM Events WHERE EventID = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, eventID);
            set = statement.executeQuery();
            if (set.next()) {
                event = new Event(
                        set.getString("EventID"),
                        set.getString("AssociatedUsername"),
                        set.getString("PersonID"),
                        set.getFloat("Latitude"),
                        set.getFloat("Longitude"),
                        set.getString("Country"),
                        set.getString("City"),
                        set.getString("EventType"),
                        set.getInt("Year")
                );
                return event;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event", e);
        } finally {
            if(set != null) {
                try {
                    set.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Find all events in the Events table associated to the user
     * @param associatedUsername the username of the user whose events we're trying to find
     * @return an ArrayList of events in this user's life
     */
    public ArrayList<Event> findEvents(String associatedUsername) throws DataAccessException {
        ArrayList<Event> events = new ArrayList<>();
        Event event;
        ResultSet set = null;
        String sql = "SELECT * FROM Events WHERE AssociatedUsername = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, associatedUsername);
            set = statement.executeQuery();
            while (set.next()) {
                event = new Event(
                        set.getString("EventID"),
                        set.getString("AssociatedUsername"),
                        set.getString("PersonID"),
                        set.getFloat("Latitude"),
                        set.getFloat("Longitude"),
                        set.getString("Country"),
                        set.getString("City"),
                        set.getString("EventType"),
                        set.getInt("Year")
                );
                events.add(event);
            }
            if (events.size() > 0) {
                return events;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding events", e);
        }
        finally {
            if(set != null) {
                try {
                    set.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public void delete(String associatedUsername) throws DataAccessException {
        String sql = "DELETE FROM Events WHERE AssociatedUsername = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, associatedUsername);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new DataAccessException("Error encountered while deleting from the database", e);
        }
    }

    /**
     * Clear all information from the Events table
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Events;";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
        catch (SQLException e) {
            throw new DataAccessException("Error encountered while clearing the Events table", e);
        }
    }
}
