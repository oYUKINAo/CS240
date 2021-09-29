package com.example.familymapclient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.Event;
import model.Person;

public class DataCache {
    // singleton
    private static DataCache cache;
    private DataCache() {}  // so that only getInstance() can create an instance of DataCache
    public static DataCache getInstance() {
        if (cache == null) {
            cache = new DataCache();
        }
        return cache;
    }

    public void destroy() {
        cache = null;
    }

    // private data members
    private boolean loggedIn = false;

    private String username;
    private String tokenStr;
    private String firstName;
    private String lastName;
    private String personID;

    private Map<String, Person> people = new HashMap<>();
    private Map<String, Event> events = new HashMap<>();

    private Map<String, ArrayList<Event>> people_events = new HashMap<>();

    private boolean life_story_lines = false;
    private boolean family_tree_lines = false;
    private boolean spouse_line = false;
    private boolean paternal = true;
    private boolean maternal = true;
    private boolean male = true;
    private boolean female = true;

    private ArrayList<Event> fatherSideEvents = new ArrayList<>();
    private ArrayList<Event> motherSideEvents = new ArrayList<>();

    private Event selectedEvent = null;
    private Person selectedPerson = null;

    private Event displayingEvent = null;
    private Person displayingPerson = null;

    // getters and setters
    public boolean isLoggedIn() {
        return loggedIn;
    }
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getTokenStr() {
        return tokenStr;
    }
    public void setTokenStr(String tokenStr) {
        this.tokenStr = tokenStr;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setPersonID(String personID) {
        this.personID = personID;
    }
    public Person getPerson(String personID) {
        return people.get(personID);
    }
    public Event getEvent(String eventID) {
        return events.get(eventID);
    }
    public Map<String, Person> getPeopleMap() {
        return people;
    }
    public Map<String, Event> getEventsMap() {
        return events;
    }

    public Map<String, ArrayList<Event>> get_people_and_events() {
        return people_events;
    }

    public boolean hasLifeStoryLines() {
        return life_story_lines;
    }
    public void setLifeStoryLines(boolean wantLifeStoryLines) {
        this.life_story_lines = wantLifeStoryLines;
    }

    public boolean hasFamilyTreeLines() {
        return family_tree_lines;
    }
    public void setFamilyTreeLines(boolean wantFamilyTreeLines) {
        this.family_tree_lines = wantFamilyTreeLines;
    }

    public boolean hasSpouseLine() {
        return spouse_line;
    }
    public void setSpouseLine(boolean wantSpouseLine) {
        this.spouse_line = wantSpouseLine;
    }

    public boolean isPaternal() {
        return paternal;
    }
    public void setPaternal(boolean wantPaternal) {
        this.paternal = wantPaternal;
    }

    public boolean isMaternal() {
        return maternal;
    }
    public void setMaternal(boolean wantMaternal) {
        this.maternal = wantMaternal;
    }

    public boolean isMale() {
        return male;
    }
    public void setMale(boolean wantMale) {
        this.male = wantMale;
    }

    public boolean isFemale() {
        return female;
    }
    public void setFemale(boolean wantFemale) {
        this.female = wantFemale;
    }

    public Event getSelectedEvent() {
        return selectedEvent;
    }
    public void setSelectedEvent(Event selectedEvent) {
        this.selectedEvent = selectedEvent;
    }

    public Person getSelectedPerson() {
        return selectedPerson;
    }
    public void setSelectedPerson(Person selectedPerson) {
        this.selectedPerson = selectedPerson;
    }

    public Event getDisplayingEvent() {
        return displayingEvent;
    }
    public void setDisplayingEvent(Event displayingEvent) {
        this.displayingEvent = displayingEvent;
    }

    public Person getDisplayingPerson() {
        return displayingPerson;
    }
    public void setDisplayingPerson(Person displayingPerson) {
        this.displayingPerson = displayingPerson;
    }

    // helper functions
    public void findFatherSideEvents() {
        String fatherID = getPerson(personID).getFatherID();
        findFatherSideEventsHelper(fatherID);
    }
    private void findFatherSideEventsHelper(String personID) {
        if (people_events.containsKey(personID)) {
            fatherSideEvents.addAll(people_events.get(personID));

            Person person = getPerson(personID);

            String fatherID = person.getFatherID();
            findFatherSideEventsHelper(fatherID);

            String motherID = person.getMotherID();
            findFatherSideEventsHelper(motherID);
        }
    }

    public void findMotherSideEvents() {
        String motherID = getPerson(personID).getMotherID();
        findMotherSideEventsHelper(motherID);
    }
    private void findMotherSideEventsHelper(String personID) {
        if (people_events.containsKey(personID)) {
            motherSideEvents.addAll(people_events.get(personID));

            Person person = getPerson(personID);

            String fatherID = person.getFatherID();
            findMotherSideEventsHelper(fatherID);

            String motherID = person.getMotherID();
            findMotherSideEventsHelper(motherID);
        }
    }

    public ArrayList<Event> findMaleEvents(ArrayList<Event> events) {
        ArrayList<Event> maleEvents = new ArrayList<>();
        for (Event event : events) {
            Person person = getPerson(event.getPersonID());
            String gender = person.getGender();
            if (gender.equals("m")) {
                maleEvents.add(event);
            }
        }
        return maleEvents;
    }

    public ArrayList<Event> findFemaleEvents(ArrayList<Event> events) {
        ArrayList<Event> femaleEvents = new ArrayList<>();
        for (Event event : events) {
            Person person = getPerson(event.getPersonID());
            String gender = person.getGender();
            if (gender.equals("f")) {
                femaleEvents.add(event);
            }
        }
        return femaleEvents;
    }

    // Choose events to display by switches in SettingsActivity
    public ArrayList<Event> getEventsAfterFilters() {
        this.findFatherSideEvents();
        this.findMotherSideEvents();

        ArrayList<Event> events = new ArrayList<>();
        events.addAll(people_events.get(personID));
        Person user = getPerson(personID);
        Person spouse = getPerson(user.getSpouseID());
        if (spouse != null) {
            String spouseID = spouse.getPersonID();
            events.addAll(people_events.get(spouseID));
        }

        if (paternal && maternal) {
            events.addAll(fatherSideEvents);
            events.addAll(motherSideEvents);
        }
        else if (paternal) {
            events.addAll(fatherSideEvents);
        }
        else if (maternal) {
            events.addAll(motherSideEvents);
        }
//        else {
//            events.addAll(people_events.get(personID));
//        }

        if (male && female) {
            return events;
        }
        else if (male) {
            return findMaleEvents(events);
        }
        else if (female) {
            return findFemaleEvents(events);
        }
        else {
            return new ArrayList<>();
        }
    }

    public void addPeople(ArrayList<Person> data) {
        for (Person person : data) {
            String personID = person.getPersonID();
            people.put(personID, person);

            if (!people_events.containsKey(personID)) {
                people_events.put(personID, new ArrayList<>());
            }
        }
    }
    public void addEvents(ArrayList<Event> data) {
        for (Event event : data) {
            String eventID = event.getEventID();
            events.put(eventID, event);

            String personID = event.getPersonID();
            ArrayList<Event> events = people_events.get(personID);

            events.add(event);
            Collections.sort(events, new Comparator<Event>() {
                @Override
                public int compare(Event event1, Event event2) {
                    return event1.getYear() - (event2.getYear());
                }
            });

            people_events.put(personID, events);
        }
    }

    public String getToastMessage() {
        return firstName + " " + lastName;
    }

    /**
     * @param substring a single search string
     * @return a list of Person objects that contain search_string in first/last names
     */
    public List<Person> getPeopleByRegex(String substring) {
        List<Person> matches = new ArrayList<>();
        for (Person person : people.values()) {
            String string = person.getFirstName() + person.getLastName();
            if (string.toLowerCase().contains(substring.toLowerCase())) {
                matches.add(person);
            }
        }
        return matches;
    }

    /**
     * @param substring a single search string
     * @return a list of Event objects that contain search_string in countries/cities/event types/years
     */
    public List<Event> getEventsByRegex(String substring) {
        List<Event> matches = new ArrayList<>();
        for (Event event : getEventsAfterFilters()) {
            String string = event.getCountry() + event.getCity() + event.getEventType() + event.getYear();
            if (string.toLowerCase().contains(substring.toLowerCase())) {
                matches.add(event);
            }
        }
        return matches;
    }

    public Person getChild(String parentID) {
        for (Person person : people.values()) {
            if ((person.getFatherID() != null && person.getMotherID() != null) && (person.getFatherID().equals(parentID) || person.getMotherID().equals(parentID))) {
                return person;
            }
        }
        return null;
    }

    public Map<String, ArrayList<Event>> filter(Map<String, ArrayList<Event>> people_events, ArrayList<Event> filtered_events) {
        Set<Event> set = new HashSet<>(filtered_events);
        Map<String, ArrayList<Event>> updated_people_events = new HashMap<>();
        for (String personID : people_events.keySet()) {
            ArrayList<Event> events = people_events.get(personID);
            ArrayList<Event> updated_events = new ArrayList<>();
            for (Event event : events) {
                if (set.contains(event)) {
                    updated_events.add(event);
                }
            }
            updated_people_events.put(personID, updated_events);
        }
        return updated_people_events;
    }
}
