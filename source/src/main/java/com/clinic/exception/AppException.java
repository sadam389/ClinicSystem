package com.clinic.exception;

/**
 * Base root checked exception for Clinic Management System.
 */
public class AppException extends Exception {
    private static final long serialVersionUID = 1L;
    private String errorCode;

    public AppException(String message) {
        super(message);
        this.errorCode = "ERR_GENERAL";
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "ERR_GENERAL";
    }

    public AppException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public AppException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
