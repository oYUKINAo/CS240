package request;

public class MultipleEventsRequest {
    private String authtoken;

    public MultipleEventsRequest(String authtoken) {
        this.authtoken = authtoken;
    }

    public String getAuthTokenStr() {
        return authtoken;
    }
    public void setAuthTokenStr(String authtoken) {
        this.authtoken = authtoken;
    }
}
