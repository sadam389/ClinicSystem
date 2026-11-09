package com.clinic.service;

import com.clinic.dao.AppointmentDao;
import com.clinic.dao.AppointmentDaoImpl;
import com.clinic.dao.DoctorDao;
import com.clinic.dao.DoctorDaoImpl;
import com.clinic.dao.PatientDao;
import com.clinic.dao.PatientDaoImpl;
import com.clinic.exception.DatabaseException;
import com.clinic.model.Appointment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportService {
    private final AppointmentDao appointmentDao;
    private final PatientDao patientDao;
    private final DoctorDao doctorDao;

    public ReportService() {
        this.appointmentDao = new AppointmentDaoImpl();
        this.patientDao = new PatientDaoImpl();
        this.doctorDao = new DoctorDaoImpl();
    }

    public Map<String, Object> generateExecutiveDashboardReport() throws DatabaseException {
        Map<String, Object> metrics = new HashMap<>();
        int totalPatients = patientDao.countTotalPatients();
        int totalAppointments = appointmentDao.countTotalAppointments();
        int totalDoctors = doctorDao.findAll().size();

        List<Appointment> allAppts = appointmentDao.findAll();
        int completedCount = 0;
        int cancelledCount = 0;
        int confirmedCount = 0;

        for (Appointment a : allAppts) {
            switch (a.getStatus()) {
                case COMPLETED: completedCount++; break;
                case CANCELLED: cancelledCount++; break;
                case CONFIRMED: confirmedCount++; break;
                default: break;
            }
        }

        metrics.put("totalPatients", totalPatients);
        metrics.put("totalDoctors", totalDoctors);
        metrics.put("totalAppointments", totalAppointments);
        metrics.put("completedAppointments", completedCount);
        metrics.put("cancelledAppointments", cancelledCount);
        metrics.put("upcomingAppointments", confirmedCount);
        metrics.put("completionRate", totalAppointments > 0 ? (completedCount * 100 / totalAppointments) : 0);

        return metrics;
    }
}
