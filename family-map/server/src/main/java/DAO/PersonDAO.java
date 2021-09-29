package DAO;

import model.Person;

import java.sql.*;
import java.util.ArrayList;

/**
 * PersonDAO class: access the Persons table in the database
 */
public class PersonDAO {
    private final Connection connection;

    public PersonDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Add a Person record to the User table
     * @param person the Person object we want to add
     */
    public void insert(Person person) throws DataAccessException {

        if (findPerson(person.getPersonID()) != null) {
            throw new DataAccessException("Error: personID already exists in the database");
        }

        String sql = "INSERT INTO Persons (PersonID, AssociatedUsername, FirstName, LastName, Gender, " +
                "FatherID, MotherID, SpouseID) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, person.getPersonID());
            statement.setString(2, person.getAssociatedUsername());
            statement.setString(3, person.getFirstName());
            statement.setString(4, person.getLastName());
            statement.setString(5, person.getGender());
            statement.setString(6, person.getFatherID());
            statement.setString(7, person.getMotherID());
            statement.setString(8, person.getSpouseID());

            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database (Persons table)", e);
        }
    }

    /**
     * Find a person in the Person table by personID
     * @param personID the ID of the person we're trying to find
     * @return what we find
     */
    public Person findPerson(String personID) throws DataAccessException {
        Person person;
        ResultSet set = null;
        String sql = "SELECT * FROM Persons WHERE PersonID = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, personID);
            set = statement.executeQuery();
            if (set.next()) {
                person = new Person(
                        set.getString("PersonID"),
                        set.getString("AssociatedUsername"),
                        set.getString("FirstName"),
                        set.getString("LastName"),
                        set.getString("Gender"),
                        set.getString("FatherID"),
                        set.getString("MotherID"),
                        set.getString("SpouseID")
                );
                return person;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding person", e);
        }
        finally {
            if (set != null) {
                try {
                    set.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Find all people in the Person table associated with a user by username
     * @param associatedUsername the username of the user whose family members we're trying to find
     * @return an ArrayList of family members of the user
     */
    public ArrayList<Person> findPeople(String associatedUsername) throws DataAccessException {
        ArrayList<Person> people = new ArrayList<>();
        Person person;
        ResultSet set = null;
        String sql = "SELECT * FROM Persons WHERE AssociatedUsername = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, associatedUsername);
            set = statement.executeQuery();
            while (set.next()) {
                person = new Person(
                        set.getString("PersonID"),
                        set.getString("AssociatedUsername"),
                        set.getString("FirstName"),
                        set.getString("LastName"),
                        set.getString("Gender"),
                        set.getString("FatherID"),
                        set.getString("MotherID"),
                        set.getString("SpouseID")
                );
                people.add(person);
            }
            if (people.size() > 0) {
                return people;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding people", e);
        }
        finally {
            if (set != null) {
                try {
                    set.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public void delete(String associatedUsername) throws DataAccessException {
        String sql = "DELETE FROM Persons WHERE AssociatedUsername = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, associatedUsername);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new DataAccessException("Error encountered while deleting from the database", e);
        }
    }

    /**
     * Clear all information from the table
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Persons;";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
        catch (SQLException e) {
            throw new DataAccessException("Error encountered while clearing the Persons table", e);
        }
    }
}
