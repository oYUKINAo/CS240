package response;

import java.util.Objects;

/**
 * Response class: base class of all response classes
 */
public abstract class Response {
    /**
     * Message delivered in the Response Body
     */
    private String message;
    /**
     * Boolean identifier
     */
    private boolean success;

    /**
     * Default constructor
     */
    public Response() {
        this.message = null;
        this.success = false;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Response response = (Response) o;
        return success == response.success && Objects.equals(message, response.message);
    }
}
