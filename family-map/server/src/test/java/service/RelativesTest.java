package service;

import DAO.*;
import model.Event;
import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class RelativesTest {
    private Relatives relatives;
    private ArrayList<Person> people;
    private ArrayList<Event> events;

    @BeforeEach
    public void setUp() throws DataAccessException {
        relatives = new Relatives("1214", "murasame", "shiba", "inu", "m");
        relatives.generateAncestors(4);
        people = relatives.getPeople();
        events = relatives.getEvents();
    }

    @AfterEach
    public void tearDown() throws DataAccessException {}

    @Test
    public void loadJsonFilesPass() {
        // make sure we successfully read the JSon files
        for (String name : relatives.getFemaleNames()) {
            assertNotNull(name);
        }
        for (String name : relatives.getMaleNames()) {
            assertNotNull(name);
        }
        for (String name : relatives.getSurnames()) {
            assertNotNull(name);
        }
        for (Location location : relatives.getLocations()) {
            assertNotNull(location);
        }
    }

    @Test
    public void threeEvents() {
        // make sure the person has three events associated with him/her
        String personID;
        int eventCount;
        for (Person person : people) {
            personID = person.getPersonID();
            eventCount = 0;
            for (Event event : events) {
                if (event.getPersonID().equals(personID)) {
                    eventCount++;
                }
            }
            assertTrue(eventCount >= 3);
        }
    }

    /**
     * Event: Birth
     */

    @Test
    public void birthPass() {
        // make sure the person's birth event comes before all other events
        String personID;
        int birthYear;
        for (Person person : people) {
            personID = person.getPersonID();
            birthYear = relatives.getBirthYear(personID);
            for (Event event : events) {
                if (event.getPersonID().equals(personID)) {
                    assertTrue(event.getYear() >= birthYear);
                }
            }
        }
    }

    @Test
    public void birthAgePass() {
        // make sure the age gap between the child and the parent is between ages 20-40
        String childID;
        String fatherID;
        String motherID;

        int childBirthYear;
        int fatherBirthYear;
        int motherBirthYear;

        for (Person child : people) {
            childID = child.getPersonID();
            fatherID = child.getFatherID();
            motherID = child.getMotherID();

            childBirthYear = relatives.getBirthYear(childID);
            fatherBirthYear = relatives.getBirthYear(fatherID);
            motherBirthYear = relatives.getBirthYear(motherID);

            if (fatherBirthYear != -1) {
                assertTrue(childBirthYear - fatherBirthYear >= 20);
                assertTrue(childBirthYear - fatherBirthYear <= 40);
            }
            if (motherBirthYear != -1) {
                assertTrue(childBirthYear - motherBirthYear >= 20);
                assertTrue(childBirthYear - fatherBirthYear <= 40);
            }
        }
    }

    /**
     * Event: Marriage
     */

    @Test
    public void marriageAgePass() {
        // make sure the person was married between ages 20-40
        String personID;
        int birthYear;
        int marriageYear;

        for (Person person : people) {
            personID = person.getPersonID();
            birthYear = relatives.getBirthYear(personID);
            marriageYear = relatives.getMarriageYear(personID);
            if (birthYear != -1 && marriageYear != -1) {
                assertTrue(marriageYear - birthYear >= 20);
                assertTrue(marriageYear - birthYear <= 40);
            }
        }
    }

    @Test
    public void marriageYearPass() {
        // make sure the person's marriage year is the same as that of his/her spouse
        String personID;
        String spouseID;

        for (Person person : people) {
            personID = person.getPersonID();
            spouseID = person.getSpouseID();
            if (spouseID != null && !spouseID.equals("")) {
                assertEquals(relatives.getMarriageYear(spouseID), relatives.getMarriageYear(personID));
            }
        }
    }

    @Test
    public void marriageLocationPass() {
        // make sure the person's marriage location is the same as that of his/her spouse
        String personID;
        String spouseID;

        Location personMarriageLocation;
        Location spouseMarriageLocation;

        for (Person person : people) {
            personID = person.getPersonID();
            spouseID = person.getSpouseID();
            if (spouseID != null && !spouseID.equals("")) {
                personMarriageLocation = relatives.getMarriageLocation(personID);
                spouseMarriageLocation = relatives.getMarriageLocation(spouseID);
                if (personMarriageLocation != null && spouseMarriageLocation != null) {
                    assertEquals(personMarriageLocation.getCountry(), spouseMarriageLocation.getCountry());
                    assertEquals(personMarriageLocation.getCity(), spouseMarriageLocation.getCity());
                    assertEquals(personMarriageLocation.getLatitude(), spouseMarriageLocation.getLatitude());
                    assertEquals(personMarriageLocation.getLongitude(), spouseMarriageLocation.getLongitude());
                }
            }
        }
    }

    /**
     * Event: Death
     */

    @Test
    public void deathPass() {
        // make sure the person's death event comes after all other events
        String personID;

        int deathYear;

        for (Person person : people) {
            personID = person.getPersonID();
            deathYear = relatives.getDeathYear(personID);
            if (deathYear != -1) {
                for (Event event : events) {
                    if (event.getPersonID().equals(personID)) {
                        assertTrue(event.getYear() <= deathYear);
                    }
                }
            }
        }
    }

    @Test
    public void deathAgePass() {
        // make sure the person died before the age of 120
        String personID;

        int birthYear;
        int deathYear;

        for (Person person : people) {
            personID = person.getPersonID();
            birthYear = relatives.getBirthYear(personID);
            deathYear = relatives.getDeathYear(personID);
            if (deathYear != -1) {
                assertTrue(deathYear - birthYear < 120);
            }
        }
    }

    @Test
    public void deathBirthPass() {
        // make sure the person died after the birth of his/her child
        for (Person child : people) {
            String childID = child.getPersonID();
            String fatherID = child.getFatherID();
            String motherID = child.getMotherID();

            int childBirthYear = relatives.getBirthYear(childID);
            int fatherDeathYear = relatives.getDeathYear(fatherID);
            int motherDeathYear = relatives.getDeathYear(motherID);

            if (fatherDeathYear != -1) {
                assertTrue(childBirthYear < fatherDeathYear);
            }
            if (motherDeathYear != -1) {
                assertTrue(childBirthYear < motherDeathYear);
            }
        }
    }
}
