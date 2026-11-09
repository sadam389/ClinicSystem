package com.clinic.exception;

/**
 * Exception thrown for unauthorized attempts or invalid credentials.
 */
public class AuthenticationException extends AppException {
    private static final long serialVersionUID = 1L;

    public AuthenticationException(String message) {
        super("ERR_AUTH_FAILED", message);
    }
}
