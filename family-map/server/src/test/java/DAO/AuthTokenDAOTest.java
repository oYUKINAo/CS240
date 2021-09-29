package DAO;

import model.AuthToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class AuthTokenDAOTest {
    private Database database;
    private AuthToken token;
    private AuthTokenDAO authTokenDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        database = new Database();
        token = new AuthToken("murasame", "panzer130551");
        Connection connection = database.getConnection();
        database.clearTables();
        authTokenDAO = new AuthTokenDAO(connection);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        database.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        String authTokenStr = token.getTokenStr();
        authTokenDAO.insert(token);
        AuthToken cloneToken = authTokenDAO.findToken(authTokenStr);
        assertNotNull(cloneToken);
        assertEquals(authTokenStr, cloneToken.getTokenStr());
    }

    @Test
    public void insertFail() throws DataAccessException {
        authTokenDAO.insert(token);
        assertThrows(DataAccessException.class, ()-> authTokenDAO.insert(token));
    }

    @Test
    public void findAuthTokenPass() throws DataAccessException {
        String authTokenStr = token.getTokenStr();
        authTokenDAO.insert(token);
        AuthToken cloneToken = authTokenDAO.findToken(authTokenStr);
        assertNotNull(cloneToken);
        assertEquals(authTokenStr, cloneToken.getTokenStr());
    }

    @Test
    public void findAuthTokenFail() throws DataAccessException {
        String authTokenStr = token.getTokenStr();
        assertNull(authTokenDAO.findToken(authTokenStr));
    }

    @Test
    public void clearPass() throws DataAccessException {
        String authTokenStr = token.getTokenStr();
        authTokenDAO.insert(token);
        assertEquals(authTokenStr, authTokenDAO.findToken(authTokenStr).getTokenStr());
        authTokenDAO.clear();
        assertNull(authTokenDAO.findToken(authTokenStr));
    }
}
