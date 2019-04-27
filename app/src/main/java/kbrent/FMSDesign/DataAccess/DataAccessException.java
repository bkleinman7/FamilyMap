package kbrent.FMSDesign.DataAccess;

/*
 * Created by kbrent on 2/09/19.
 */

/**
 * Further error handling
 */
public class DataAccessException extends Exception {
    DataAccessException(String message)
    {
        super(message);
    }
}

