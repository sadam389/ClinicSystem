package com.clinic.service;

import com.clinic.dao.AppointmentDao;
import com.clinic.dao.AppointmentDaoImpl;
import com.clinic.dao.MedicalRecordDao;
import com.clinic.dao.MedicalRecordDaoImpl;
import com.clinic.dao.PrescriptionDao;
import com.clinic.dao.PrescriptionDaoImpl;
import com.clinic.exception.DatabaseException;
import com.clinic.exception.ValidationException;
import com.clinic.model.AppointmentStatus;
import com.clinic.model.MedicalRecord;
import com.clinic.model.Prescription;
import com.clinic.model.PrescriptionItem;
import com.clinic.util.ValidationUtil;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class PrescriptionService {
    private final PrescriptionDao prescriptionDao;
    private final MedicalRecordDao medicalRecordDao;
    private final AppointmentDao appointmentDao;

    public PrescriptionService() {
        this.prescriptionDao = new PrescriptionDaoImpl();
        this.medicalRecordDao = new MedicalRecordDaoImpl();
        this.appointmentDao = new AppointmentDaoImpl();
    }

    public Prescription createPrescriptionWithRecord(
            int appointmentId, int doctorId, int patientId,
            String diagnosis, String symptoms, String bp, Integer pulse,
            BigDecimal temp, BigDecimal weight, String notes,
            String advice, String followUpDateStr, List<PrescriptionItem> items)
            throws ValidationException, DatabaseException {

        ValidationUtil.requireNonEmpty("Diagnosis", diagnosis);
        ValidationUtil.requireNonEmpty("Symptoms", symptoms);

        // 1. Create clinical medical record
        MedicalRecord mr = new MedicalRecord();
        mr.setAppointmentId(appointmentId > 0 ? appointmentId : null);
        mr.setDoctorId(doctorId);
        mr.setPatientId(patientId);
        mr.setDiagnosis(ValidationUtil.sanitizeString(diagnosis));
        mr.setSymptoms(ValidationUtil.sanitizeString(symptoms));
        mr.setBloodPressure(bp);
        mr.setPulseBpm(pulse);
        mr.setTemperatureCelsius(temp);
        mr.setWeightKg(weight);
        mr.setClinicalNotes(ValidationUtil.sanitizeString(notes));

        int recordId = medicalRecordDao.create(mr);

        // 2. Create Prescription
        Prescription rx = new Prescription();
        rx.setRecordId(recordId);
        rx.setDoctorId(doctorId);
        rx.setPatientId(patientId);
        rx.setAdvice(ValidationUtil.sanitizeString(advice));
        if (followUpDateStr != null && !followUpDateStr.trim().isEmpty() && ValidationUtil.isValidDate(followUpDateStr)) {
            rx.setFollowUpDate(Date.valueOf(followUpDateStr.trim()));
        }
        rx.setItems(items);

        int prescriptionId = prescriptionDao.create(rx);
        rx.setPrescriptionId(prescriptionId);

        // 3. Mark appointment as COMPLETED if linked
        if (appointmentId > 0) {
            appointmentDao.updateStatus(appointmentId, AppointmentStatus.COMPLETED, null);
        }

        return rx;
    }

    public List<Prescription> getPrescriptionsForPatient(int patientId) throws DatabaseException {
        return prescriptionDao.findByPatientId(patientId);
    }

    public List<Prescription> getPrescriptionsForDoctor(int doctorId) throws DatabaseException {
        return prescriptionDao.findByDoctorId(doctorId);
    }

    public Prescription getPrescriptionById(int prescriptionId) throws DatabaseException {
        return prescriptionDao.findById(prescriptionId);
    }
}
