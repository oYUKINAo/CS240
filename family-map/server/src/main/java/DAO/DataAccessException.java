package DAO;

public class DataAccessException extends Exception {
    DataAccessException(String message, Exception e)
    {
        super(message, e);
    }

    DataAccessException(String message)
    {
        super(message);
    }
}
