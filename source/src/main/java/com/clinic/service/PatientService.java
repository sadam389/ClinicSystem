package com.clinic.service;

import com.clinic.dao.PatientDao;
import com.clinic.dao.PatientDaoImpl;
import com.clinic.dao.MedicalRecordDao;
import com.clinic.dao.MedicalRecordDaoImpl;
import com.clinic.exception.DatabaseException;
import com.clinic.model.Patient;
import com.clinic.model.MedicalRecord;

import java.util.List;

public class PatientService {
    private final PatientDao patientDao;
    private final MedicalRecordDao medicalRecordDao;

    public PatientService() {
        this.patientDao = new PatientDaoImpl();
        this.medicalRecordDao = new MedicalRecordDaoImpl();
    }

    public Patient getPatientByUserId(int userId) throws DatabaseException {
        return patientDao.findByUserId(userId);
    }

    public Patient getPatientById(int patientId) throws DatabaseException {
        return patientDao.findById(patientId);
    }

    public List<Patient> getAllPatients() throws DatabaseException {
        return patientDao.findAll();
    }

    public List<MedicalRecord> getPatientHistory(int patientId) throws DatabaseException {
        return medicalRecordDao.findByPatientId(patientId);
    }
}
