package com.example.familymapclient;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.Event;
import model.Person;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class AndroidTest {
    DataCache cache = DataCache.getInstance();

    Person person = new Person(
            "shiba inu",
            "dog",
            "shiba",
            "inu",
            "m",
            "siberian husky",
            "golden retriever",
            "border collie");
    Event person_birth_event = new Event(
            "birth",
            "dog",
            "shiba inu",
            0,
            0,
            "Japan",
            "Osaka",
            "BIRTH",
            2000);
    Event person_bark_event = new Event(
            "bark",
            "dog",
            "shiba inu",
            0,
            0,
            "China",
            "Shenzhen",
            "BARK",
            2010);
    Event person_chase_event = new Event(
            "chase",
            "dog",
            "shiba inu",
            0,
            0,
            "USA",
            "SLC",
            "CHASE",
            2020);

    Person father = new Person(
            "siberian husky",
            "dog",
            "siberian",
            "husky",
            "m",
            null,
            null,
            "golden retriever");
    Event father_event = new Event(
            "sabotage",
            "dog",
            "siberian husky",
            0,
            0,
            "Russia",
            "Moscow",
            "SABOTAGE",
            1988);

    Person mother = new Person(
            "golden retriever",
            "dog",
            "golden",
            "retriever",
            "f",
            null,
            null,
            "siberian husky");
    Event mother_event = new Event(
            "chase",
            "dog",
            "golden retriever",
            0,
            0,
            "USA",
            "Boston",
            "CHASE",
            1988);

    Person spouse = new Person(
            "border collie",
            "dog",
            "border",
            "collie",
            "f",
            null,
            null,
            "shiba inu");
    Event spouse_event = new Event(
            "dance",
            "dog",
            "border collie",
            0,
            0,
            "Brazil",
            "Rio",
            "DANCE",
            2000);

    Person child = new Person(
            "german shepherd",
            "dog",
            "german",
            "shepherd",
            "m",
            "shiba inu",
            "border collie",
            null);

    String substring = "c";

    @Before
    public void set_up() {
        cache.setPersonID(person.getPersonID());

        ArrayList<Person> people = new ArrayList<>();
        people.add(person);
        people.add(father);
        people.add(mother);
        people.add(spouse);
        people.add(child);
        cache.addPeople(people);

        ArrayList<Event> events = new ArrayList<>();
        events.add(person_birth_event);
        events.add(person_bark_event);
        events.add(person_chase_event);
        events.add(father_event);
        events.add(mother_event);
        events.add(spouse_event);
        cache.addEvents(events);

        cache.setPaternal(true);
        cache.setMaternal(true);
        cache.setMale(true);
        cache.setFemale(true);
    }

    @After
    public void tear_down() {
        cache.destroy();
    }

    // Calculates family relationships (i.e., spouses, parents, children)
    @Test
    public void family_members_PASS() {
        Person dog = person;

        Person dad = cache.getPerson(dog.getFatherID());
        assertEquals(father, dad);

        Person mom = cache.getPerson(dog.getMotherID());
        assertEquals(mother, mom);

        Person wife = cache.getPerson(dog.getSpouseID());
        assertEquals(spouse, wife);

        Person kid = cache.getChild(dog.getPersonID());
        assertEquals(child, kid);
    }
    @Test
    public void family_members_FAIL() {
        Person puppy = child;

        assertNull(cache.getChild(puppy.getPersonID()));
    }

    // Filters events according to the current filter settings
    @Test
    public void father_side_filtered_PASS() {
        cache.setPaternal(false);
        ArrayList<Event> events = cache.getEventsAfterFilters();
        assertEquals(5, events.size());
    }
    @Test
    public void father_side_filtered_FAIL() {
        cache.setPersonID(father.getPersonID());
        cache.setPaternal(false);
        ArrayList<Event> events = cache.getEventsAfterFilters();
        assertEquals(2, events.size());
    }

    @Test
    public void mother_side_filtered_PASS() {
        cache.setMaternal(false);
        ArrayList<Event> events = cache.getEventsAfterFilters();
        assertEquals(5, events.size());
    }
    @Test
    public void mother_side_filtered_FAIL() {
        cache.setPersonID(mother.getPersonID());
        cache.setMaternal(false);
        ArrayList<Event> events = cache.getEventsAfterFilters();
        assertEquals(2, events.size());
    }

    @Test
    public void male_filtered_PASS() {
        cache.setMale(false);
        ArrayList<Event> events = cache.getEventsAfterFilters();
        assertEquals(2, events.size());
    }
    @Test
    public void male_filtered_FAIL() {
        cache.setMale(true);
        ArrayList<Event> events = cache.getEventsAfterFilters();
        assertNotEquals(2, events.size());
    }

    @Test
    public void female_filtered_PASS() {
        cache.setFemale(false);
        ArrayList<Event> events = cache.getEventsAfterFilters();
        assertEquals(4, events.size());
    }
    @Test
    public void female_filtered_FAIL() {
        cache.setFemale(true);
        ArrayList<Event> events = cache.getEventsAfterFilters();
        assertNotEquals(4, events.size());
    }

    // Chronologically sorts a person’s individual events (birth first, death last, etc.)
    @Test
    public void in_chronological_order_PASS() {
        Map<String, ArrayList<Event>> people_events = cache.get_people_and_events();
        ArrayList<Event> events = people_events.get(person.getPersonID());
        for (int i = 0; i < events.size() - 1; i++) {
            assertTrue(events.get(i).getYear() <= events.get(i + 1).getYear());
        }
    }
    @Test
    public void in_chronological_order_FAIL() {
        Map<String, ArrayList<Event>> people_events = cache.get_people_and_events();
        ArrayList<Event> events = people_events.get(person.getPersonID());
        for (int i = 0; i < events.size() - 1; i++) {
            assertFalse(events.get(i).getYear() > events.get(i + 1).getYear());
        }
    }

    // Correctly searches for people and events (for your Search Activity)
    @Test
    public void search_people_PASS() {
        List<Person> people = cache.getPeopleByRegex(substring);
        for (Person person : people) {
            String string = person.getFirstName() + person.getLastName();
            assertTrue(string.toLowerCase().contains(substring.toLowerCase()));
        }
    }
    @Test
    public void search_people_FAIL() {
        List<Person> people = cache.getPeopleByRegex("^_^");
        assertEquals(0, people.size());
    }

    @Test
    public void search_event_PASS() {
        List<Event> events = cache.getEventsByRegex(substring);
        for (Event event : events) {
            String string = event.getCountry() + event.getCity() + event.getEventType() + event.getYear();
            assertTrue(string.toLowerCase().contains(substring.toLowerCase()));
        }
    }
    @Test
    public void search_event_FAIL() {
        List<Event> events = cache.getEventsByRegex("^_^");
        assertEquals(0, events.size());
    }
}