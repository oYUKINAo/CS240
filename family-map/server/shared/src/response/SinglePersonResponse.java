package response;

import java.util.Objects;

/**
 * SinglePersonResponse class: tells the handlers
 * whether we are able to return the Person object or not
 */
public class SinglePersonResponse extends Response {
    /**
     * Associated Username: Name of user account this person belongs to
     */
    private String associatedUsername;
    /**
     * Person ID: Person’s unique ID
     */
    private String personID;
    /**
     * First Name: Person’s first name
     */
    private String firstName;
    /**
     * Last Name: Person’s last name
     */
    private String lastName;
    /**
     * Gender: Person’s gender (“m” or “f”)
     */
    private String gender;
    /**
     * Father ID: ID of person’s father [OPTIONAL, can be missing]
     */
    private String fatherID;
    /**
     * Mother ID: ID of person’s mother [OPTIONAL, can be missing]
     */
    private String motherID;
    /**
     * Spouse ID: ID of person’s spouse [OPTIONAL, can be missing]
     */
    private String spouseID;

    /**
     * Success constructor
     * @param associatedUsername Name of user account this person belongs to
     * @param personID Person’s unique ID
     * @param firstName Person’s first name
     * @param lastName Person’s last name
     * @param gender Person’s gender (“m” or “f”)
     * @param fatherID ID of person’s father [OPTIONAL, can be missing]
     * @param motherID ID of person’s mother [OPTIONAL, can be missing]
     * @param spouseID ID of person’s spouse [OPTIONAL, can be missing]
     */
    public SinglePersonResponse(String associatedUsername, String personID,
                         String firstName, String lastName,
                         String gender,
                         String fatherID, String motherID, String spouseID) {
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;

        super.setSuccess(true);
    }

    /**
     * Error constructor
     * @param errorMessage Description of the error
     */
    public SinglePersonResponse(String errorMessage) {
        super.setMessage(errorMessage);
        super.setSuccess(false);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SinglePersonResponse that = (SinglePersonResponse) o;
        return Objects.equals(associatedUsername, that.associatedUsername) && Objects.equals(personID, that.personID) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(gender, that.gender) && Objects.equals(fatherID, that.fatherID) && Objects.equals(motherID, that.motherID) && Objects.equals(spouseID, that.spouseID);
    }
}
