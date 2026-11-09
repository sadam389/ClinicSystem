package com.clinic;

import com.clinic.util.ValidationUtil;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit Test Suite: Defensive Input Validation and Sanitization.
 */
public class ValidationUtilTest {

    @Test
    public void testValidEmails() {
        assertTrue(ValidationUtil.isValidEmail("doctor@medicare.org"));
        assertTrue(ValidationUtil.isValidEmail("john.doe@clinic-network.com"));
        assertTrue(ValidationUtil.isValidEmail("user_123@sub.domain.co.uk"));
    }

    @Test
    public void testInvalidEmails() {
        assertFalse(ValidationUtil.isValidEmail("plainaddress"));
        assertFalse(ValidationUtil.isValidEmail("@missingusername.com"));
        assertFalse(ValidationUtil.isValidEmail("user@.com.my"));
        assertFalse(ValidationUtil.isValidEmail(null));
    }

    @Test
    public void testPasswordComplexity() {
        // Must contain upper, lower, digit, and special char, min 8 chars
        assertTrue(ValidationUtil.isValidPassword("AdminPass123!"));
        assertTrue(ValidationUtil.isValidPassword("Doctor@2026"));
        assertFalse(ValidationUtil.isValidPassword("weak"));
        assertFalse(ValidationUtil.isValidPassword("nouppercase123!"));
        assertFalse(ValidationUtil.isValidPassword("NOLOWERCASE123!"));
        assertFalse(ValidationUtil.isValidPassword("NoSpecialChars123"));
    }

    @Test
    public void testDateFormatValidation() {
        assertTrue(ValidationUtil.isValidDate("2026-11-09"));
        assertTrue(ValidationUtil.isValidDate("1990-01-15"));
        assertFalse(ValidationUtil.isValidDate("09/11/2026"));
        assertFalse(ValidationUtil.isValidDate("2026-1-1"));
        assertFalse(ValidationUtil.isValidDate("invalid-date"));
    }

    @Test
    public void testStringSanitizationAgainstXSS() {
        String input = "<script>alert('xss');</script>";
        String sanitized = ValidationUtil.sanitizeString(input);
        assertFalse(sanitized.contains("<script>"));
        assertTrue(sanitized.contains("&lt;script&gt;"));
    }
}
