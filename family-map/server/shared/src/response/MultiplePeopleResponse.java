package response;

import java.util.ArrayList;
import java.util.Objects;

import model.Person;

/**
 * MultiplePeopleResponse class: tells the handlers
 * whether we are able to return the array of Person objects or not
 */
public class MultiplePeopleResponse extends Response {
    /**
     * Data: Array of Person objects
     */
    private ArrayList<Person> data;

    /**
     * Success constructor
     * @param data Array of Person objects
     */
    public MultiplePeopleResponse(ArrayList<Person> data) {
        this.data = data;

        super.setSuccess(true);
    }

    /**
     * Error constructor
     * @param errorMessage Description of the error
     */
    public MultiplePeopleResponse(String errorMessage) {
        super.setMessage(errorMessage);
        super.setSuccess(false);
    }

    public ArrayList<Person> getData() {
        return data;
    }
    public void setData(ArrayList<Person> data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MultiplePeopleResponse that = (MultiplePeopleResponse) o;
        return Objects.equals(data, that.data);
    }
}
