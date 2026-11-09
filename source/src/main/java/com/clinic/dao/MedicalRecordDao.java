package com.clinic.dao;

import com.clinic.exception.DatabaseException;
import com.clinic.model.MedicalRecord;
import java.util.List;

public interface MedicalRecordDao {
    MedicalRecord findById(int recordId) throws DatabaseException;
    MedicalRecord findByAppointmentId(int appointmentId) throws DatabaseException;
    List<MedicalRecord> findByPatientId(int patientId) throws DatabaseException;
    int create(MedicalRecord record) throws DatabaseException;
}
