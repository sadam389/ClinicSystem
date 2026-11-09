package com.clinic.service;

import com.clinic.dao.AppointmentDao;
import com.clinic.dao.AppointmentDaoImpl;
import com.clinic.dao.DoctorDao;
import com.clinic.dao.DoctorDaoImpl;
import com.clinic.exception.DatabaseException;
import com.clinic.exception.ValidationException;
import com.clinic.model.Appointment;
import com.clinic.model.AppointmentStatus;
import com.clinic.model.Doctor;
import com.clinic.util.ValidationUtil;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.logging.Logger;

/**
 * Service orchestrating appointment bookings, slot validation, and cancellation rules.
 */
public class AppointmentService {
    private static final Logger LOGGER = Logger.getLogger(AppointmentService.class.getName());

    private final AppointmentDao appointmentDao;
    private final DoctorDao doctorDao;

    public AppointmentService() {
        this.appointmentDao = new AppointmentDaoImpl();
        this.doctorDao = new DoctorDaoImpl();
    }

    public AppointmentService(AppointmentDao appointmentDao, DoctorDao doctorDao) {
        this.appointmentDao = appointmentDao;
        this.doctorDao = doctorDao;
    }

    public Appointment bookAppointment(int patientId, int doctorId, String dateStr, String timeStr, String reason)
            throws ValidationException, DatabaseException {

        // Validate date & time
        if (!ValidationUtil.isValidDate(dateStr)) {
            throw new ValidationException("Invalid appointment date format. Use YYYY-MM-DD.");
        }
        if (!ValidationUtil.isValidTime(timeStr)) {
            throw new ValidationException("Invalid start time format. Use HH:MM.");
        }
        ValidationUtil.requireNonEmpty("Reason for visit", reason);

        Date apptDate = Date.valueOf(dateStr);
        LocalDate chosenDate = apptDate.toLocalDate();
        if (chosenDate.isBefore(LocalDate.now())) {
            throw new ValidationException("Appointments cannot be scheduled in the past.");
        }

        // Format SQL time
        String fullTimeStr = timeStr.length() == 5 ? timeStr + ":00" : timeStr;
        Time startTime = Time.valueOf(fullTimeStr);

        Doctor doctor = doctorDao.findById(doctorId);
        if (doctor == null) {
            throw new ValidationException("Selected doctor does not exist.");
        }
        if (!doctor.isAvailable()) {
            throw new ValidationException("Doctor " + doctor.getFullName() + " is currently unavailable for bookings.");
        }

        // Check slot conflict
        boolean available = appointmentDao.isSlotAvailable(doctorId, apptDate, startTime);
        if (!available) {
            throw new ValidationException("The selected time slot (" + timeStr + ") with " + doctor.getFullName() + " is already booked.");
        }

        // Calculate end time (default 30 mins)
        LocalTime lt = startTime.toLocalTime().plusMinutes(30);
        Time endTime = Time.valueOf(lt);

        // Generate unique appointment reference number
        String aptNumber = "APT-" + dateStr.replace("-", "") + "-" + String.format("%03d", (int)(Math.random() * 900 + 100));

        Appointment apt = new Appointment();
        apt.setAppointmentNumber(aptNumber);
        apt.setPatientId(patientId);
        apt.setDoctorId(doctorId);
        apt.setAppointmentDate(apptDate);
        apt.setStartTime(startTime);
        apt.setEndTime(endTime);
        apt.setStatus(AppointmentStatus.CONFIRMED);
        apt.setReasonForVisit(ValidationUtil.sanitizeString(reason));

        int newId = appointmentDao.create(apt);
        apt.setAppointmentId(newId);
        LOGGER.info("Appointment booked: " + aptNumber + " [Patient: " + patientId + ", Doctor: " + doctorId + "]");
        return apt;
    }

    public boolean cancelAppointment(int appointmentId, String reason) throws ValidationException, DatabaseException {
        Appointment apt = appointmentDao.findById(appointmentId);
        if (apt == null) {
            throw new ValidationException("Appointment not found.");
        }
        if (apt.getStatus() == AppointmentStatus.COMPLETED) {
            throw new ValidationException("Completed appointments cannot be cancelled.");
        }
        return appointmentDao.updateStatus(appointmentId, AppointmentStatus.CANCELLED, ValidationUtil.sanitizeString(reason));
    }

    public List<Appointment> getAppointmentsForPatient(int patientId) throws DatabaseException {
        return appointmentDao.findByPatientId(patientId);
    }

    public List<Appointment> getAppointmentsForDoctor(int doctorId) throws DatabaseException {
        return appointmentDao.findByDoctorId(doctorId);
    }

    public List<Appointment> getAllAppointments() throws DatabaseException {
        return appointmentDao.findAll();
    }
}
