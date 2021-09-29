package response;

import java.util.Objects;

/**
 * LoginResponse class: tells the handler whether the login is successful or not
 */
public class LoginResponse extends Response {
    /**
     * Non-empty auth token string
     */
    private String authtoken;
    /**
     * Username passed in with request
     */
    private String username;
    /**
     * Non-empty string containing the Person ID of the user’s generated Person object
     */
    private String personID;

    /**
     * Success constructor
     * @param authtoken Non-empty auth token string
     * @param username Username passed in with request
     * @param personID Non-empty string containing the Person ID of the user’s generated Person object
     */
    public LoginResponse(String authtoken, String username, String personID) {
        this.authtoken = authtoken;
        this.username = username;
        this.personID = personID;

        super.setSuccess(true);
    }

    /**
     * Error constructor
     * @param errorMessage Description of the error
     */
    public LoginResponse(String errorMessage) {
        super.setMessage(errorMessage);
        super.setSuccess(false);
    }

    public String getTokenStr() {
        return authtoken;
    }
    public void setTokenStr(String authtoken) {
        this.authtoken = authtoken;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPersonID() {
        return personID;
    }
    public void setPersonID(String personID) {
        this.personID = personID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LoginResponse that = (LoginResponse) o;
        return Objects.equals(authtoken, that.authtoken) && Objects.equals(username, that.username) && Objects.equals(personID, that.personID);
    }
}
