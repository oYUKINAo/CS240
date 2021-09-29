package request;

public class SinglePersonRequest {
    String personID;
    String authtoken;

    public SinglePersonRequest(String personID, String authtoken) {
        this.personID = personID;
        this.authtoken = authtoken;
    }

    public String getPersonID() {
        return personID;
    }
    public void setPersonID(String personID) {
        this.personID = personID;
    }
    public String getAuthTokenStr() {
        return authtoken;
    }
    public void setAuthTokenStr(String authtoken) {
        this.authtoken = authtoken;
    }
}