package model;

/**
 * Event class: event ID, associated username, person ID, latitude, longitude, country, city, event type, year
 */
public class Event {
    /**
     * Event ID: Unique identifier for this event (non-empty string)
     */
    private String eventID;
    /**
     * Associated Username: User (Username) to which this person belongs
     */
    private String associatedUsername;
    /**
     * Person ID: ID of person to which this event belongs
     */
    private String personID;
    /**
     * Latitude: Latitude of event’s location (float)
     */
    private float latitude;
    /**
     * Longitude: Longitude of event’s location (float)
     */
    private float longitude;
    /**
     * Country: Country in which event occurred
     */
    private String country;
    /**
     * City: City in which event occurred
     */
    private String city;
    /**
     * EventType: Type of event (birth, baptism, christening, marriage, death, etc.)
     */
    private String eventType;
    /**
     * Year: Year in which event occurred (integer)
     */
    private int year;

    /**
     * Constructor
     * @param eventID Unique identifier for this event (non-empty string)
     * @param associatedUsername User (Username) to which this person belongs
     * @param personID ID of person to which this event belongs
     * @param latitude Latitude of event’s location (float)
     * @param longitude Longitude of event’s location (float)
     * @param country Country in which event occurred
     * @param city City in which event occurred
     * @param eventType Type of event (birth, baptism, christening, marriage, death, etc.)
     * @param year Year in which event occurred (integer)
     */
    public Event(String eventID, String associatedUsername, String personID,
                 float latitude, float longitude,
                 String country, String city,
                 String eventType, int year) {
        this.eventID = eventID;
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    public String getEventID() {
        return eventID;
    }
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }
    public String getAssociatedUsername() {
        return associatedUsername;
    }
    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }
    public String getPersonID() {
        return personID;
    }
    public void setPersonID(String personID) {
        this.personID = personID;
    }
    public float getLatitude() {
        return latitude;
    }
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }
    public float getLongitude() {
        return longitude;
    }
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getEventType() {
        return eventType;
    }
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof Event) {
            Event event = (Event) o;
            return event.getEventID().equals(getEventID()) &&
                    event.getAssociatedUsername().equals(getAssociatedUsername()) &&
                    event.getPersonID().equals(getPersonID()) &&
                    event.getLatitude() == (getLatitude()) &&
                    event.getLongitude() == (getLongitude()) &&
                    event.getCountry().equals(getCountry()) &&
                    event.getCity().equals(getCity()) &&
                    event.getEventType().equals(getEventType()) &&
                    event.getYear() == (getYear());
        }
        else {
            return false;
        }
    }
}
