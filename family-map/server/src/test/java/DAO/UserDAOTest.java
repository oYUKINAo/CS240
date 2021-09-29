package DAO;

import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {
    private Database database;
    private User user;
    private UserDAO userDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        database = new Database();
        user = new User(
                "murasame",
                "kusanagi",
                "murasame@yakuza.edu",
                "shiba",
                "inu",
                "m",
                "123456"
        );
        Connection connection = database.getConnection();
        database.clearTables();
        userDAO = new UserDAO(connection);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        database.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        String username = user.getUsername();
        userDAO.insert(user);
        User cloneUser = userDAO.findUser(username);
        assertNotNull(cloneUser);
        assertEquals(username, cloneUser.getUsername());
    }

    @Test
    public void insertFail() throws DataAccessException {
        userDAO.insert(user);
        assertThrows(DataAccessException.class, ()-> userDAO.insert(user));
    }

    @Test
    public void findUserPass() throws DataAccessException {
        String username = user.getUsername();
        userDAO.insert(user);
        User cloneUser = userDAO.findUser(username);
        assertNotNull(cloneUser);
        assertEquals(username, cloneUser.getUsername());
    }

    @Test
    public void findUserFail() throws DataAccessException {
        String username = user.getUsername();
        assertNull(userDAO.findUser(username));
    }

    @Test
    public void clearPass() throws DataAccessException {
        String username = user.getUsername();
        userDAO.insert(user);
        assertEquals(username, userDAO.findUser(username).getUsername());
        userDAO.clear();
        assertNull(userDAO.findUser(username));
    }
}