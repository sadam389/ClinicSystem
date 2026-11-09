package com.clinic.service;

import com.clinic.dao.PatientDao;
import com.clinic.dao.PatientDaoImpl;
import com.clinic.dao.UserDao;
import com.clinic.dao.UserDaoImpl;
import com.clinic.exception.AuthenticationException;
import com.clinic.exception.DatabaseException;
import com.clinic.exception.ValidationException;
import com.clinic.model.Patient;
import com.clinic.model.Role;
import com.clinic.model.User;
import com.clinic.util.PasswordUtil;
import com.clinic.util.ValidationUtil;

import java.sql.Date;
import java.util.logging.Logger;

/**
 * Service encapsulating authentication, session validation, and account registration.
 */
public class AuthService {
    private static final Logger LOGGER = Logger.getLogger(AuthService.class.getName());

    private final UserDao userDao;
    private final PatientDao patientDao;

    public AuthService() {
        this.userDao = new UserDaoImpl();
        this.patientDao = new PatientDaoImpl();
    }

    public AuthService(UserDao userDao, PatientDao patientDao) {
        this.userDao = userDao;
        this.patientDao = patientDao;
    }

    public User authenticate(String email, String password) throws AuthenticationException, DatabaseException {
        if (!ValidationUtil.isValidEmail(email)) {
            throw new AuthenticationException("Please enter a valid email address.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new AuthenticationException("Password is required.");
        }

        User user = userDao.findByEmail(email.trim().toLowerCase());
        if (user == null) {
            LOGGER.warning("Authentication failed: user not found -> " + email);
            throw new AuthenticationException("Invalid email or password.");
        }

        if (!"ACTIVE".equalsIgnoreCase(user.getStatus())) {
            throw new AuthenticationException("Your account is currently " + user.getStatus() + ". Please contact administrator.");
        }

        boolean verified = PasswordUtil.verifyPassword(password, user.getPasswordHash(), user.getSalt());
        if (!verified) {
            LOGGER.warning("Authentication failed: password mismatch -> " + email);
            throw new AuthenticationException("Invalid email or password.");
        }

        LOGGER.info("User authenticated successfully: " + email + " [Role: " + user.getRole() + "]");
        return user;
    }

    public Patient registerPatient(String email, String password, String firstName, String lastName,
                                   String dobStr, String gender, String phone, String address,
                                   String bloodGroup, String emergencyContact, String emergencyPhone,
                                   String allergies) throws ValidationException, DatabaseException {

        // Validate fields
        if (!ValidationUtil.isValidEmail(email)) throw new ValidationException("Invalid email format.");
        if (password == null || password.length() < 8) throw new ValidationException("Password must be at least 8 characters.");
        ValidationUtil.requireNonEmpty("First Name", firstName);
        ValidationUtil.requireNonEmpty("Last Name", lastName);
        if (!ValidationUtil.isValidDate(dobStr)) throw new ValidationException("Date of birth must be YYYY-MM-DD.");
        if (!ValidationUtil.isValidPhone(phone)) throw new ValidationException("Valid phone number required.");

        User existing = userDao.findByEmail(email.trim().toLowerCase());
        if (existing != null) {
            throw new ValidationException("An account with email " + email + " already exists.");
        }

        // Generate credentials
        String salt = PasswordUtil.generateSalt();
        String hash = PasswordUtil.hashPassword(password, salt);

        User user = new User();
        user.setEmail(email.trim().toLowerCase());
        user.setPasswordHash(hash);
        user.setSalt(salt);
        user.setRole(Role.PATIENT);
        user.setStatus("ACTIVE");

        int userId = userDao.create(user);

        // Generate Medical Record Number
        String mrn = "MRN-" + (2026) + "-" + String.format("%04d", (int)(Math.random() * 9000 + 1000));

        Patient patient = new Patient();
        patient.setUserId(userId);
        patient.setPatientMrn(mrn);
        patient.setFirstName(ValidationUtil.sanitizeString(firstName));
        patient.setLastName(ValidationUtil.sanitizeString(lastName));
        patient.setDateOfBirth(Date.valueOf(dobStr));
        patient.setGender(gender != null ? gender.toUpperCase() : "OTHER");
        patient.setPhone(ValidationUtil.sanitizeString(phone));
        patient.setAddress(ValidationUtil.sanitizeString(address));
        patient.setBloodGroup(bloodGroup != null ? bloodGroup : "O+");
        patient.setEmergencyContact(ValidationUtil.sanitizeString(emergencyContact));
        patient.setEmergencyPhone(ValidationUtil.sanitizeString(emergencyPhone));
        patient.setAllergies(ValidationUtil.sanitizeString(allergies));

        patientDao.create(patient);
        LOGGER.info("Registered new patient: " + mrn + " for user " + email);
        return patient;
    }
}
