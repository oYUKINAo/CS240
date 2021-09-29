package request;

/**
 * LoginRequest class: make a request to the LoginService class
 */
public class LoginRequest {
    /**
     * Non-empty string
     */
    private String username;
    /**
     * Non-empty string
     */
    private String password;

    /**
     * Constructor
     * @param username Non-empty string
     * @param password Non-empty string
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
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
}
