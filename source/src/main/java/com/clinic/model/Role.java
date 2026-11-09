package com.clinic.model;

/**
 * System roles for Role-Based Access Control (RBAC).
 */
public enum Role {
    PATIENT,
    DOCTOR,
    ADMIN;

    public static Role fromString(String roleStr) {
        if (roleStr == null) return null;
        try {
            return Role.valueOf(roleStr.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
