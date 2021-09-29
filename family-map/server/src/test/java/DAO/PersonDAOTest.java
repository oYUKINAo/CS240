package DAO;

import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PersonDAOTest {
    private Database database;
    private Person person1;
    private Person person2;
    private Person person3;
    private PersonDAO personDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        database = new Database();
        person1 = new Person(
                "1900",
                "ww2",
                "German",
                "soldier",
                "m",
                "1878",
                "1880",
                "1902"
        );
        person2 = new Person(
                "1910",
                "ww2",
                "Chinese",
                "soldier",
                "m",
                "1888",
                "1890",
                "1912"
        );
        person3 = new Person(
                "1920",
                "ww2",
                "American",
                "soldier",
                "m",
                "1898",
                "1900",
                "1922"
        );
        Connection connection = database.getConnection();
        database.clearTables();
        personDAO = new PersonDAO(connection);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        database.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        String personID = person1.getPersonID();
        personDAO.insert(person1);
        Person clonePerson = personDAO.findPerson(personID);
        assertNotNull(clonePerson);
        assertEquals(personID, clonePerson.getPersonID());
    }

    @Test
    public void insertFail() throws DataAccessException {
        personDAO.insert(person1);
        assertThrows(DataAccessException.class, ()-> personDAO.insert(person1));
    }

    @Test
    public void findPersonPass() throws DataAccessException {
        String personID = person1.getPersonID();
        personDAO.insert(person1);
        Person clonePerson = personDAO.findPerson(personID);
        assertNotNull(clonePerson);
        assertEquals(personID, clonePerson.getPersonID());
    }

    @Test
    public void findPersonFail() throws DataAccessException {
        String personID = person1.getPersonID();
        assertNull(personDAO.findPerson(personID));
    }

    @Test
    public void findPeoplePass() throws DataAccessException {
        String associatedUsername = person1.getAssociatedUsername();
        personDAO.insert(person1);
        personDAO.insert(person2);
        personDAO.insert(person3);
        ArrayList<Person> people = personDAO.findPeople(associatedUsername);
        assertEquals(people.size(), 3);
    }

    @Test
    public void findPeopleFail() throws DataAccessException {
        String associatedUsername = person1.getAssociatedUsername();
        ArrayList<Person> people = personDAO.findPeople(associatedUsername);
        assertNull(people);
    }

    @Test
    public void deletePass1() throws DataAccessException {
        String associatedUsername = person1.getAssociatedUsername();
        personDAO.insert(person1);
        personDAO.delete(associatedUsername);
        String personID = person1.getPersonID();
        assertNull(personDAO.findPerson(personID));
    }

    @Test
    public void deletePass2() throws DataAccessException {
        String associatedUsername = person1.getAssociatedUsername();
        personDAO.insert(person1);
        personDAO.insert(person2);
        personDAO.insert(person3);
        personDAO.delete(associatedUsername);
        String person1ID = person1.getPersonID();
        String person2ID = person2.getPersonID();
        String person3ID = person3.getPersonID();
        assertNull(personDAO.findPerson(person1ID));
        assertNull(personDAO.findPerson(person2ID));
        assertNull(personDAO.findPerson(person3ID));
    }

    @Test
    public void clearPass() throws DataAccessException {
        String personID = person1.getPersonID();
        personDAO.insert(person1);
        assertEquals(personID, personDAO.findPerson(personID).getPersonID());
        personDAO.clear();
        assertNull(personDAO.findPerson(personID));
    }
}