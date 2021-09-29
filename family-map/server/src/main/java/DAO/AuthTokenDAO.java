package DAO;

import model.AuthToken;

import java.sql.*;

/**
 * AuthorizationTokenDAO class: access the AuthorizationToken table in the database
 */
public class AuthTokenDAO {
    private final Connection connection;

    public AuthTokenDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Add a AuthorizationToken record to the AuthorizationToken table
     * @param token the AuthorizationToken object we want to add
     */
    public void insert(AuthToken token) throws DataAccessException {

        if (findToken(token.getTokenStr()) != null) {
            throw new DataAccessException("Error: token already exists in the database");
        }

        String sql = "INSERT INTO AuthTokens (Username, Token) VALUES(?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, token.getUsername());
            statement.setString(2, token.getTokenStr());

            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database", e);
        }
    }

    /**
     * Find a token in the AuthorizationToken table by the string representation of the token
     * @param tokenStr string representation of the token
     * @return what we find
     */
    public AuthToken findToken(String tokenStr) throws DataAccessException {
        AuthToken token;
        ResultSet set = null;
        String sql = "SELECT * FROM AuthTokens WHERE Token = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, tokenStr);
            set = statement.executeQuery();
            if (set.next()) {
                token = new AuthToken(set.getString("Username"), set.getString("Token"));
                return token;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding auth token", e);
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
     * Clear all information from the table
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM AuthTokens";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
        catch (SQLException e) {
            throw new DataAccessException("Error encountered while clearing the AuthTokens table", e);
        }
    }
}
