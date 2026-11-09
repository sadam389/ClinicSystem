package com.clinic.model;

/**
 * Lifecycle states of an outpatient clinical appointment.
 */
public enum AppointmentStatus {
    PENDING,
    CONFIRMED,
    COMPLETED,
    CANCELLED,
    NO_SHOW;

    public static AppointmentStatus fromString(String statusStr) {
        if (statusStr == null) return PENDING;
        try {
            return AppointmentStatus.valueOf(statusStr.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return PENDING;
        }
    }
}
