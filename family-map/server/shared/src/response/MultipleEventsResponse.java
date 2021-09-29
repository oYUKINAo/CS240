package response;

import java.util.ArrayList;
import java.util.Objects;

import model.Event;

/**
 * MultipleEventsResponse class: tells the handlers
 * whether we're able to return the list of Event objects or not
 */
public class MultipleEventsResponse extends Response {
    /**
     * Data: Array of Event objects
     */
    private ArrayList<Event> data;

    /**
     * Success constructor
     * @param data Array of Event objects
     */
    public MultipleEventsResponse(ArrayList<Event> data) {
        this.data = data;

        super.setSuccess(true);
    }

    /**
     * Error constructor
     * @param errorMessage Description of the error
     */
    public MultipleEventsResponse(String errorMessage) {
        super.setMessage(errorMessage);
        super.setSuccess(false);
    }

    public ArrayList<Event> getData() {
        return data;
    }
    public void setData(ArrayList<Event> data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MultipleEventsResponse that = (MultipleEventsResponse) o;
        return Objects.equals(data, that.data);
    }
}