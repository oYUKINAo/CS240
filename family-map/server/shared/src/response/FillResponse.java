package response;

/**
 * FillResponse: tells the handler whether the filling is successful or not
 */
public class FillResponse extends Response {
    /**
     * Success constructor
     * @param personCount Number of people added to the database
     * @param eventCount Number of events added to the database
     */
    public FillResponse(int personCount, int eventCount) {
        StringBuilder message = new StringBuilder();
        message.append("Successfully added ");
        message.append(personCount);
        message.append(" persons and ");
        message.append(eventCount);
        message.append(" events to the database.");

        super.setMessage(message.toString());
        super.setSuccess(true);
    };

    /**
     * Error constructor
     * @param errorMessage Description of the error
     */
    public FillResponse(String errorMessage) {
        super.setMessage(errorMessage);
        super.setSuccess(false);
    };
}