package model;

/**
 * User class: username, password, email, first name, last name, gender, person ID
 */
public class User {
    /**
     * Username: Unique username (non-empty string)
     */
    private String username;
    /**
     * Password: User’s password (non-empty string)
     */
    private String password;
    /**
     * Email: User’s email address (non-empty string)
     */
    private String email;
    /**
     * First Name: User’s first name (non-empty string)
     */
    private String firstName;
    /**
     * Last Name: User’s last name (non-empty string)
     */
    private String lastName;
    /**
     * Gender: User’s gender (string: “f” or “m”)
     */
    private String gender;
    /**
     * Unique Person ID assigned to this user’s generated Person object (non-empty string)
     */
    private String personID;

    /**
     * Constructor
     * @param username Unique username (non-empty string)
     * @param password User’s password (non-empty string)
     * @param email User’s email address (non-empty string)
     * @param firstName User’s first name (non-empty string)
     * @param lastName User’s last name (non-empty string)
     * @param gender User’s gender (string: “f” or “m”)
     * @param personID Unique Person ID assigned to this user’s generated Person object (non-empty string)
     */
    public User(String username, String password,
                String email,
                String firstName, String lastName,
                String gender,
                String personID) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.personID = personID;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
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
    public String getPersonID() {
        return personID;
    }
    public void setPersonID(String personID) {
        this.personID = personID;
    }
}
