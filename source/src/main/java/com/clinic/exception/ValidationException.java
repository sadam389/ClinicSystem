package com.clinic.exception;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Exception raised during business validation failures.
 * Captures field-specific validation errors for user feedback.
 */
public class ValidationException extends AppException {
    private static final long serialVersionUID = 1L;
    private final List<String> errorMessages = new ArrayList<>();

    public ValidationException(String message) {
        super("ERR_VALIDATION", message);
        this.errorMessages.add(message);
    }

    public ValidationException(List<String> messages) {
        super("ERR_VALIDATION", String.join("; ", messages));
        this.errorMessages.addAll(messages);
    }

    public List<String> getErrorMessages() {
        return Collections.unmodifiableList(errorMessages);
    }
}
