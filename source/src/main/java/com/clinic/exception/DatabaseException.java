package com.clinic.exception;

/**
 * Exception thrown when JDBC, transaction, or database connectivity faults occur.
 */
public class DatabaseException extends AppException {
    private static final long serialVersionUID = 1L;

    public DatabaseException(String message) {
        super("ERR_DATABASE", message);
    }

    public DatabaseException(String message, Throwable cause) {
        super("ERR_DATABASE", message, cause);
    }
}
