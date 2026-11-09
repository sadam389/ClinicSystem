package com.clinic.util;

import com.clinic.exception.ValidationException;
import java.util.regex.Pattern;

/**
 * Defensive input validation and sanitization utility.
 * Prevents SQL Injection vectors, Cross-Site Scripting (XSS), and malformed clinical input.
 */
public class ValidationUtil {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[+]?[0-9\\s-]{7,20}$");
    private static final Pattern MRN_PATTERN = Pattern.compile("^MRN-[0-9]{4}-[0-9]{4}$");
    private static final Pattern DATE_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
    private static final Pattern TIME_PATTERN = Pattern.compile("^([01]?[0-9]|2[0-3]):[0-5][0-9](:[0-5][0-9])?$");

    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidPhone(String phone) {
        if (phone == null) return false;
        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    public static boolean isValidDate(String dateStr) {
        if (dateStr == null) return false;
        return DATE_PATTERN.matcher(dateStr.trim()).matches();
    }

    public static boolean isValidTime(String timeStr) {
        if (timeStr == null) return false;
        return TIME_PATTERN.matcher(timeStr.trim()).matches();
    }

    public static boolean isValidMRN(String mrn) {
        if (mrn == null) return false;
        return MRN_PATTERN.matcher(mrn.trim()).matches();
    }

    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) return false;
        boolean hasUpper = false, hasLower = false, hasDigit = false, hasSpecial = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else hasSpecial = true;
        }
        return hasUpper && hasLower && hasDigit && hasSpecial;
    }

    public static String sanitizeString(String input) {
        if (input == null) return "";
        return input.trim()
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }

    public static void requireNonEmpty(String fieldName, String value) throws ValidationException {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(fieldName + " is mandatory and cannot be left blank.");
        }
    }
}
