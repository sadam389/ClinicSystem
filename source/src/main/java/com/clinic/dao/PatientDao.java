package com.clinic.dao;

import com.clinic.exception.DatabaseException;
import com.clinic.model.Patient;
import java.util.List;

public interface PatientDao {
    Patient findById(int patientId) throws DatabaseException;
    Patient findByUserId(int userId) throws DatabaseException;
    Patient findByMRN(String mrn) throws DatabaseException;
    List<Patient> findAll() throws DatabaseException;
    int create(Patient patient) throws DatabaseException;
    boolean update(Patient patient) throws DatabaseException;
    int countTotalPatients() throws DatabaseException;
}
