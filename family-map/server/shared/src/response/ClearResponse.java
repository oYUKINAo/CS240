package response;

/**
 * ClearResponse class: tells the handler whether the clearing is successful or not
 */
public class ClearResponse extends Response {
    /**
     * Success constructor
     */
    public ClearResponse() {
        super.setMessage("Clear succeeded.");
        super.setSuccess(true);
    };

    /**
     * Error constructor
     * @param errorMessage Description of the error
     */
    public ClearResponse(String errorMessage) {
        super.setMessage(errorMessage);
        super.setSuccess(false);
    }
}
