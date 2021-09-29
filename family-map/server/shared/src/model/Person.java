package model;

/**
 * Person class: person ID, associated username, first name, last name, gender, father ID, mother ID, spouse ID
 */
public class Person {
    /**
     * Person ID: Unique identifier for this person (non-empty string)
     */
    private String personID;
    /**
     * Associated Username: User (Username) to which this person belongs
     */
    private String associatedUsername;
    /**
     * First Name: Person’s first name (non-empty string)
     */
    private String firstName;
    /**
     * Last Name: Person’s last name (non-empty string)
     */
    private String lastName;
    /**
     * Gender: Person’s gender (string: “f” or “m”)
     */
    private String gender;
    /**
     * Father ID: Person ID of person’s father (possibly null)
     */
    private String fatherID;
    /**
     * Mother ID: Person ID of person’s mother (possibly null)
     */
    private String motherID;
    /**
     * Spouse ID: Person ID of person’s spouse (possibly null)
     */
    private String spouseID;

    /**
     * Constructor
     * @param personID Unique identifier for this person (non-empty string)
     * @param associatedUsername User (Username) to which this person belongs
     * @param firstName Person’s first name (non-empty string)
     * @param lastName Person’s last name (non-empty string)
     * @param gender Person’s gender (string: “f” or “m”)
     * @param fatherID Person ID of person’s father (possibly null)
     * @param motherID Person ID of person’s mother (possibly null)
     * @param spouseID Person ID of person’s spouse (possibly null)
     */
    public Person(String personID, String associatedUsername,
                  String firstName, String lastName,
                  String gender,
                  String fatherID, String motherID, String spouseID) {
        this.personID = personID;
        this.associatedUsername = associatedUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }

    public String getPersonID() {
        return personID;
    }
    public void setPersonID(String personID) {
        this.personID = personID;
    }
    public String getAssociatedUsername() {
        return associatedUsername;
    }
    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getFatherID() {
        return fatherID;
    }
    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }
    public String getMotherID() {
        return motherID;
    }
    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }
    public String getSpouseID() {
        return spouseID;
    }
    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
    }
}
