package response;

import java.util.Objects;

/**
 * SingleEventResponse class: tells the handlers
 * whether we're able to return the Event object or not
 */
public class SingleEventResponse extends Response {
    /**
     * Associated Username: Username of user account this event belongs to (non-empty string)
     */
    private String associatedUsername;
    /**
     * Event ID: Event’s unique ID (non-empty string)
     */
    private String eventID;
    /**
     * Person ID: ID of the person this event belongs to (non-empty string)
     */
    private String personID;
    /**
     * Latitude: Latitude of the event’s location (number)
     */
    private double latitude;
    /**
     * Longitude: Longitude of the event’s location (number)
     */
    private double longitude;
    /**
     * Country: Name of country where event occurred (non-empty string)
     */
    private String country;
    /**
     * City: Name of city where event occurred (non-empty string)
     */
    private String city;
    /**
     * Event Type: Type of event (“birth”, “baptism”, etc.) (non-empty string)
     */
    private String eventType;
    /**
     * Year: Year the event occurred (integer)
     */
    private int year;

    /**
     * Success constructor
     * @param associatedUsername Username of user account this event belongs to (non-empty string)
     * @param eventID Event’s unique ID (non-empty string)
     * @param personID ID of the person this event belongs to (non-empty string)
     * @param latitude Latitude of the event’s location (number)
     * @param longitude Longitude of the event’s location (number)
     * @param country Name of country where event occurred (non-empty string)
     * @param city Name of city where event occurred (non-empty string)
     * @param eventType Type of event (“birth”, “baptism”, etc.) (non-empty string)
     * @param year Year the event occurred (integer)
     */
    public SingleEventResponse(String associatedUsername,
                               String eventID, String personID,
                               double latitude, double longitude,
                               String country, String city,
                               String eventType, int year) {
        this.associatedUsername = associatedUsername;
        this.eventID = eventID;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;

        super.setSuccess(true);
    }

    /**
     * Error constructor
     * @param errorMessage Description of the error
     */
    public SingleEventResponse(String errorMessage) {
        super.setMessage(errorMessage);
        super.setSuccess(false);
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }
    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }
    public String getEventID() {
        return eventID;
    }
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }
    public String getPersonID() {
        return personID;
    }
    public void setPersonID(String personID) {
        this.personID = personID;
    }
    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SingleEventResponse that = (SingleEventResponse) o;
        return Double.compare(that.latitude, latitude) == 0 && Double.compare(that.longitude, longitude) == 0 && year == that.year && Objects.equals(associatedUsername, that.associatedUsername) && Objects.equals(eventID, that.eventID) && Objects.equals(personID, that.personID) && Objects.equals(country, that.country) && Objects.equals(city, that.city) && Objects.equals(eventType, that.eventType);
    }
}