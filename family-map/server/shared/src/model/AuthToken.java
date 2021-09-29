package model;

/**
 * Authorization Token class: username, token
 */
public class AuthToken {
    /**
     * Username: Unique username (non-empty string)
     */
    private String username;
    /**
     * Token: Unique token's string representation (non-empty string)
     */
    private String authTokenStr;

    /**
     * Constructor
     * @param username Unique username (non-empty string)
     * @param authTokenStr Unique token's string representation (non-empty string)
     */
    public AuthToken(String username, String authTokenStr) {
        this.username = username;
        this.authTokenStr = authTokenStr;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getTokenStr() {
        return authTokenStr;
    }
    public void setTokenStr(String authTokenStr) {
        this.authTokenStr = authTokenStr;
    }
}
