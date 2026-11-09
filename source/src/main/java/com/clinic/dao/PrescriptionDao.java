package com.clinic.dao;

import com.clinic.exception.DatabaseException;
import com.clinic.model.Prescription;
import com.clinic.model.PrescriptionItem;
import java.util.List;

public interface PrescriptionDao {
    Prescription findById(int prescriptionId) throws DatabaseException;
    Prescription findByRecordId(int recordId) throws DatabaseException;
    List<Prescription> findByPatientId(int patientId) throws DatabaseException;
    List<Prescription> findByDoctorId(int doctorId) throws DatabaseException;
    int create(Prescription prescription) throws DatabaseException;
    boolean addItems(int prescriptionId, List<PrescriptionItem> items) throws DatabaseException;
}
