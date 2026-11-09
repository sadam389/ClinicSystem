package com.clinic.service;

import com.clinic.dao.DepartmentDao;
import com.clinic.dao.DepartmentDaoImpl;
import com.clinic.dao.DoctorDao;
import com.clinic.dao.DoctorDaoImpl;
import com.clinic.dao.UserDao;
import com.clinic.dao.UserDaoImpl;
import com.clinic.exception.DatabaseException;
import com.clinic.exception.ValidationException;
import com.clinic.model.Department;
import com.clinic.model.Doctor;
import com.clinic.model.DoctorSchedule;
import com.clinic.model.Role;
import com.clinic.model.User;
import com.clinic.util.PasswordUtil;
import com.clinic.util.ValidationUtil;

import java.util.List;

public class DoctorService {
    private final DoctorDao doctorDao;
    private final DepartmentDao departmentDao;
    private final UserDao userDao;

    public DoctorService() {
        this.doctorDao = new DoctorDaoImpl();
        this.departmentDao = new DepartmentDaoImpl();
        this.userDao = new UserDaoImpl();
    }

    public Doctor getDoctorByUserId(int userId) throws DatabaseException {
        return doctorDao.findByUserId(userId);
    }

    public Doctor getDoctorById(int doctorId) throws DatabaseException {
        return doctorDao.findById(doctorId);
    }

    public List<Doctor> getAllDoctors() throws DatabaseException {
        return doctorDao.findAll();
    }

    public List<Department> getAllDepartments() throws DatabaseException {
        return departmentDao.findAll();
    }

    public List<DoctorSchedule> getDoctorSchedules(int doctorId) throws DatabaseException {
        return doctorDao.getSchedulesByDoctorId(doctorId);
    }

    public Doctor addDoctor(String email, String password, int departmentId, String licenseNumber,
                            String firstName, String lastName, String phone, String qualification,
                            int experienceYears, String roomNumber) throws ValidationException, DatabaseException {

        if (!ValidationUtil.isValidEmail(email)) throw new ValidationException("Invalid email format.");
        ValidationUtil.requireNonEmpty("License Number", licenseNumber);
        ValidationUtil.requireNonEmpty("First Name", firstName);
        ValidationUtil.requireNonEmpty("Last Name", lastName);

        String salt = PasswordUtil.generateSalt();
        String hash = PasswordUtil.hashPassword(password, salt);

        User user = new User();
        user.setEmail(email.trim().toLowerCase());
        user.setPasswordHash(hash);
        user.setSalt(salt);
        user.setRole(Role.DOCTOR);
        user.setStatus("ACTIVE");

        int userId = userDao.create(user);

        Doctor doc = new Doctor();
        doc.setUserId(userId);
        doc.setDepartmentId(departmentId);
        doc.setLicenseNumber(licenseNumber);
        doc.setFirstName(ValidationUtil.sanitizeString(firstName));
        doc.setLastName(ValidationUtil.sanitizeString(lastName));
        doc.setPhone(ValidationUtil.sanitizeString(phone));
        doc.setQualification(ValidationUtil.sanitizeString(qualification));
        doc.setExperienceYears(experienceYears);
        doc.setRoomNumber(ValidationUtil.sanitizeString(roomNumber));
        doc.setAvailable(true);

        doctorDao.create(doc);
        return doc;
    }
}
