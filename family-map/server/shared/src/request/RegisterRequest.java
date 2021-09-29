package request;

/**
 * RegisterRequest class: makes a request to the RegisterService class
 */
public class RegisterRequest {
    /**
     * Non-empty string
     */
    private String username;
    /**
     * Non-empty string
     */
    private String password;
    /**
     * Non-empty string
     */
    private String email;
    /**
     * Non-empty string
     */
    private String firstName;
    /**
     * Non-empty string
     */
    private String lastName;
    /**
     * “f” or “m”
     */
    private String gender;

    /**
     * Constructor
     * @param username Non-empty string
     * @param password Non-empty string
     * @param email Non-empty string
     * @param firstName Non-empty string
     * @param lastName Non-empty string
     * @param gender “f” or “m”
     */
    public RegisterRequest(String username, String password, String email,
                           String firstName, String lastName, String gender) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
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
}
