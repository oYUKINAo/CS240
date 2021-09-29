package response;

/**
 * LoadResponse class: tells the handlers whether the loading is successful or not
 */
public class LoadResponse extends Response {
    /**
     * Success constructor
     * @param userCount Number of users added to the database
     * @param personCount Number of people added to the database
     * @param eventCount Number of events added to the database
     */
    public LoadResponse(int userCount, int personCount, int eventCount) {
        StringBuilder message = new StringBuilder();
        message.append("Successfully added ");
        message.append(userCount);
        message.append(" users, ");
        message.append(personCount);
        message.append(" persons, and ");
        message.append(eventCount);
        message.append(" events to the database.");

        super.setMessage(message.toString());
        super.setSuccess(true);
    }

    /**
     * Error constructor
     * @param errorMessage Description of the error
     */
    public LoadResponse(String errorMessage) {
        super.setMessage(errorMessage);
        super.setSuccess(false);
    }
}
