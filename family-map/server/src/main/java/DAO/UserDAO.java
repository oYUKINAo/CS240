package DAO;

import model.User;

import java.sql.*;

/**
 * UserDAO class: access the User table in the database
 */
public class UserDAO {
    private final Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Add a User record to the User table
     * @param user the User object we want to add
     */
    public void insert(User user) throws DataAccessException {

        if (findUser(user.getUsername()) != null) {
            throw new DataAccessException("Error: username already exists in the database");
        }

        String sql = "INSERT INTO Users (Username, Password, Email, FirstName, LastName, Gender, PersonID) " +
                "VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getFirstName());
            statement.setString(5, user.getLastName());
            statement.setString(6, user.getGender());
            statement.setString(7, user.getPersonID());

            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database", e);
        }
    }

    /**
     * Find a user in the User table by username
     * @param username the username of the user we're trying to find
     * @return what we find
     */
    public User findUser(String username) throws DataAccessException {
        User user;
        ResultSet set = null;
        String sql = "SELECT * FROM Users WHERE Username = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            set = statement.executeQuery();
            if (set.next()) {
                user = new User(
                        set.getString("Username"),
                        set.getString("Password"),
                        set.getString("Email"),
                        set.getString("FirstName"),
                        set.getString("LastName"),
                        set.getString("Gender"),
                        set.getString("PersonID")
                );
                return user;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding user", e);
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
     * Clear all information from the Users table
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Users;";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
        catch (SQLException e) {
            throw new DataAccessException("Error encountered while clearing the Users table", e);
        }
    }
}
