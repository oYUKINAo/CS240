package request;

public class MultiplePeopleRequest {
    private String authtoken;

    public MultiplePeopleRequest(String authtoken) {
        this.authtoken = authtoken;
    }

    public String getAuthToken() {
        return authtoken;
    }
    public void setAuthToken(String authtoken) {
        this.authtoken = authtoken;
    }
}