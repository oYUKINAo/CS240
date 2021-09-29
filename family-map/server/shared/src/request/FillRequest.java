package request;

import model.Event;
import model.Person;
import model.User;

import java.util.ArrayList;

public class FillRequest {
    private String username;
    private int numGenerations;

    public FillRequest(String username, int numGenerations) {
        this.username = username;
        this.numGenerations = numGenerations;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public int getNumGenerations() {
        return numGenerations;
    }
    public void setNumGenerations(int numGenerations) {
        this.numGenerations = numGenerations;
    }
}