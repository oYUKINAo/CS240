package service;

import handler.JsonSerializer;
import model.Event;
import model.Person;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

// Ancestor generation logic
public class Relatives {
    private ArrayList<Person> people;
    private ArrayList<Event> events;

    private String associatedUsername;
    private Person userPerson;

    private ArrayList<String> femaleNames = new ArrayList<>();
    private ArrayList<String> maleNames = new ArrayList<>();
    private ArrayList<String> surnames = new ArrayList<>();
    private ArrayList<Location> locations = new ArrayList<>();

    // Read the json files of female names, male names, surnames, and locations
    private void loadJsonFiles() {
        JSONParser parser = new JSONParser();
        try {
            Object femaleNamesObject = parser.parse(new FileReader("json/fnames.json"));
            JSONObject femaleNamesJSONObject = (JSONObject) femaleNamesObject;
            JSONArray femaleNamesJSONArray = (JSONArray) femaleNamesJSONObject.get("data");
            for (Object o : femaleNamesJSONArray) {
                femaleNames.add(o.toString());
            }

            Object maleNamesObject = parser.parse(new FileReader("json/mnames.json"));
            JSONObject maleNamesJSONObject = (JSONObject) maleNamesObject;
            JSONArray maleNamesJSONArray = (JSONArray) maleNamesJSONObject.get("data");
            for (Object o : maleNamesJSONArray) {
                maleNames.add(o.toString());
            }

            Object surnamesObject = parser.parse(new FileReader("json/snames.json"));
            JSONObject surnamesJSONObject = (JSONObject) surnamesObject;
            JSONArray surnamesJSONArray = (JSONArray) surnamesJSONObject.get("data");
            for (Object o : surnamesJSONArray) {
                surnames.add(o.toString());
            }

            Object locationsObject = parser.parse(new FileReader("json/locations.json"));
            JSONObject locationsJSONObject = (JSONObject) locationsObject;
            JSONArray locationsJSONArray = (JSONArray) locationsJSONObject.get("data");
            for (Object o : locationsJSONArray) {
                String locationDict = o.toString();
                Location location = JsonSerializer.deserialize(locationDict, Location.class);
                locations.add(location);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Random generation functions
    private String generateRandomFemaleName() {
        int i = ThreadLocalRandom.current().nextInt(0, femaleNames.size());
        return femaleNames.get(i);
    }
    private String generateRandomMaleName() {
        int i = ThreadLocalRandom.current().nextInt(0, maleNames.size());
        return maleNames.get(i);
    }
    private String generateRandomSurname() {
        int i = ThreadLocalRandom.current().nextInt(0, surnames.size());
        return surnames.get(i);
    }
    private Location generateRandomLocation() {
        int i = ThreadLocalRandom.current().nextInt(0, locations.size());
        return locations.get(i);
    }

    private Event generateRandomEvent(Person person, String eventType, int year) {
        Location loc = generateRandomLocation();
        Event event = new Event(
                UUID.randomUUID().toString(),
                associatedUsername,
                person.getPersonID(),
                loc.getLatitude(),
                loc.getLongitude(),
                loc.getCountry(),
                loc.getCity(),
                eventType,
                year
        );
        return event;
    }


    // Restricted event generation functions
    private Event generateBirthEvent(Person person, int year) {
        Location birthplace = generateRandomLocation();
        return new Event(
                UUID.randomUUID().toString(),
                associatedUsername,
                person.getPersonID(),
                birthplace.getLatitude(),
                birthplace.getLongitude(),
                birthplace.getCountry(),
                birthplace.getCity(),
                "birth",
                year
        );
    }
    private Event generateMarriageEvent(Person person, int year, Location weddingLoc) {
        return new Event(
                UUID.randomUUID().toString(),
                associatedUsername,
                person.getPersonID(),
                weddingLoc.getLatitude(),
                weddingLoc.getLongitude(),
                weddingLoc.getCountry(),
                weddingLoc.getCity(),
                "marriage",
                year
        );
    }
    private Event generateDeathEvent(Person person, int year) {
        Location deathbed = generateRandomLocation();
        return new Event(
                UUID.randomUUID().toString(),
                associatedUsername,
                person.getPersonID(),
                deathbed.getLatitude(),
                deathbed.getLongitude(),
                deathbed.getCountry(),
                deathbed.getCity(),
                "death",
                year
        );
    }

    // Restricted event generation rules
    private int generateBirthYear(String childPersonID) {
        int childBirthYear = getBirthYear(childPersonID);
        return ThreadLocalRandom.current().nextInt(childBirthYear - 40, childBirthYear - 20);
    }
    private int generateMarriageYear(String fatherPersonID, String motherPersonID) {
        int fatherBirthYear = getBirthYear(fatherPersonID);
        int motherBirthYear = getBirthYear(motherPersonID);
        return ThreadLocalRandom.current().nextInt(Math.max(fatherBirthYear, motherBirthYear) + 20, Math.min(fatherBirthYear, motherBirthYear) + 40);
    }
    private int generateDeathYear(String childPersonID) {
        int childBirthYear = getBirthYear(childPersonID);
        return ThreadLocalRandom.current().nextInt(childBirthYear + 20, childBirthYear + 60);
    }


    public Relatives(String personID, String associatedUsername, String firstName, String lastName, String gender) {
        this.associatedUsername = associatedUsername;
        this.people = new ArrayList<>();
        this.events = new ArrayList<>();

        // Load the JSon files
        loadJsonFiles();

        // generate new Person object for the user and add it to the list
        Person person = new Person(personID,
                associatedUsername,
                firstName,
                lastName,
                gender,
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "");
        userPerson = person;
        people.add(person);

        // generate the user person's three events and add them to the list
        Event birth = generateBirthEvent(person, 2000);
        events.add(birth);

        Event event2 = generateRandomEvent(person, "amusement park", 2010);
        events.add(event2);

        Event event3 = generateRandomEvent(person, "water park", 2020);
        events.add(event3);
    }

    // Find the birth year of a particular Person object by personID
    public int getBirthYear(String personID) {
        for (Event event : events) {
            if (event.getPersonID().equals(personID) && event.getEventType().equals("birth")) {
                return event.getYear();
            }
        }
        return -1;
    }
    // Find the marriage year of a particular Person object by personID
    public int getMarriageYear(String personID) {
        for (Event event : events) {
            if (event.getPersonID().equals(personID) && event.getEventType().equals("marriage")) {
                return event.getYear();
            }
        }
        return -1;
    }
    // Find the death year of a particular Person object by personID
    public int getDeathYear(String personID) {
        for (Event event : events) {
            if (event.getPersonID().equals(personID) && event.getEventType().equals("death")) {
                return event.getYear();
            }
        }
        return -1;
    }

    // Find the marriage Location of a particular Person object by personID
    public Location getMarriageLocation(String personID) {
        for (Event event : events) {
            if (event.getPersonID().equals(personID) && event.getEventType().equals("marriage")) {
                String country = event.getCountry();
                String city = event.getCity();
                float latitude = event.getLatitude();
                float longitude = event.getLongitude();
                return new Location(country, city, latitude, longitude);
            }
        }
        return null;
    }

    /**
     * Generate ancestor data for the user and store them in ArrayLists of Person and Event objects, respectively
     * @param numGenerations Number of ancestor generations we want to generate for the user
     */
    public void generateAncestors(int numGenerations) {
        int currGeneration = 1;
        generateAncestorsHelper(this.userPerson, currGeneration, numGenerations);
    }

    private void generateAncestorsHelper(Person child, int currGeneration, int numGenerations) {
        if (currGeneration <= numGenerations) {
            String fatherID = child.getFatherID();
            String motherID = child.getMotherID();

            // generate father Person for child and add it to the list
            String dadID;
            String momID;
            if (currGeneration == numGenerations) {
                dadID = null;
                momID = null;
            }
            else {
                dadID = UUID.randomUUID().toString();
                momID = UUID.randomUUID().toString();
            }
            Person father = new Person(
                    fatherID,
                    this.associatedUsername,
                    generateRandomMaleName(),
                    generateRandomSurname(),
                    "m",
                    dadID,
                    momID,
                    motherID
            );
            people.add(father);

            // generate father's birth Event and add it to the list
            int fatherBirthYear = generateBirthYear(child.getPersonID());
            Event fatherBirth = generateBirthEvent(father, fatherBirthYear);
            events.add(fatherBirth);

            // generate father's death Event and add it to the list
            int fatherDeathYear = generateDeathYear(child.getPersonID());
            Event fatherDeath = generateDeathEvent(father, fatherDeathYear);
            events.add(fatherDeath);


            // generate mother Person for child and add it to the list
            if (currGeneration == numGenerations) {
                dadID = null;
                momID = null;
            }
            else {
                dadID = UUID.randomUUID().toString();
                momID = UUID.randomUUID().toString();
            }
            Person mother = new Person(
                    motherID,
                    this.associatedUsername,
                    generateRandomFemaleName(),
                    generateRandomSurname(),
                    "f",
                    dadID,
                    momID,
                    fatherID
            );
            people.add(mother);

            // generate mother's birth Event and add it to the list
            int motherBirthYear = generateBirthYear(child.getPersonID());
            Event motherBirth = generateBirthEvent(mother, motherBirthYear);
            events.add(motherBirth);

            // generate mother's death Event and add it to the list
            int motherDeathYear = generateDeathYear(child.getPersonID());
            Event motherDeath = generateDeathEvent(mother, motherDeathYear);
            events.add(motherDeath);

            // generate both parents' marriage Events and add them to the list
            int marriageYear = generateMarriageYear(father.getPersonID(), mother.getPersonID());
            Location weddingLoc = generateRandomLocation();
            Event fatherMarriage = generateMarriageEvent(father, marriageYear, weddingLoc);
            events.add(fatherMarriage);
            Event motherMarriage = generateMarriageEvent(mother, marriageYear, weddingLoc);
            events.add(motherMarriage);

            // recursive calls
            generateAncestorsHelper(father, currGeneration + 1, numGenerations);
            generateAncestorsHelper(mother, currGeneration + 1, numGenerations);
        }
    }

    // getters and setters
    public ArrayList<Person> getPeople() {
        return people;
    }
    public void setPeople(ArrayList<Person> people) {
        this.people = people;
    }
    public ArrayList<Event> getEvents() {
        return events;
    }
    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public String getAssociatedUsername() { return associatedUsername; }
    public void setAssociatedUsername(String associatedUsername) { this.associatedUsername = associatedUsername; }
    public Person getUserPerson() { return userPerson; }
    public void setUserPerson(Person userPerson) { this.userPerson = userPerson; }

    public ArrayList<String> getFemaleNames() {
        return femaleNames;
    }
    public void setFemaleNames(ArrayList<String> femaleNames) {
        this.femaleNames = femaleNames;
    }
    public ArrayList<String> getMaleNames() {
        return maleNames;
    }
    public void setMaleNames(ArrayList<String> maleNames) {
        this.maleNames = maleNames;
    }
    public ArrayList<String> getSurnames() {
        return surnames;
    }
    public void setSurnames(ArrayList<String> surnames) {
        this.surnames = surnames;
    }
    public ArrayList<Location> getLocations() {
        return locations;
    }
    public void setLocations(ArrayList<Location> locations) {
        this.locations = locations;
    }

    /**
     * Debug helper
     * @param args arguments
     */
    public static void main(String[] args) {
        System.out.println("Debugging...");
        Relatives relatives = new Relatives("1214", "murasame", "shiba", "inu", "m");
    }
}
