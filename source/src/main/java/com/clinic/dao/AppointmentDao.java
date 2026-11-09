package com.clinic.dao;

import com.clinic.exception.DatabaseException;
import com.clinic.model.Appointment;
import com.clinic.model.AppointmentStatus;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

public interface AppointmentDao {
    Appointment findById(int appointmentId) throws DatabaseException;
    Appointment findByNumber(String appointmentNumber) throws DatabaseException;
    List<Appointment> findByPatientId(int patientId) throws DatabaseException;
    List<Appointment> findByDoctorId(int doctorId) throws DatabaseException;
    List<Appointment> findByDoctorAndDate(int doctorId, Date date) throws DatabaseException;
    List<Appointment> findAll() throws DatabaseException;
    boolean isSlotAvailable(int doctorId, Date date, Time startTime) throws DatabaseException;
    int create(Appointment appointment) throws DatabaseException;
    boolean updateStatus(int appointmentId, AppointmentStatus status, String cancellationReason) throws DatabaseException;
    int countTotalAppointments() throws DatabaseException;
}
