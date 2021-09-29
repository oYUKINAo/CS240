package request;

public class SingleEventRequest {
    private String eventID;
    private String authtoken;

    public SingleEventRequest(String eventID, String authtoken) {
        this.eventID = eventID;
        this.authtoken = authtoken;
    }

    public String getEventID() {
        return eventID;
    }
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }
    public String getAuthTokenStr() {
        return authtoken;
    }
    public void setAuthTokenStr(String authtoken) {
        this.authtoken = authtoken;
    }
}
