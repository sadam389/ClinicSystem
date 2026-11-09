package com.clinic.dao;

import com.clinic.exception.DatabaseException;
import com.clinic.model.Doctor;
import com.clinic.model.DoctorSchedule;
import java.util.List;

public interface DoctorDao {
    Doctor findById(int doctorId) throws DatabaseException;
    Doctor findByUserId(int userId) throws DatabaseException;
    List<Doctor> findAll() throws DatabaseException;
    List<Doctor> findByDepartmentId(int departmentId) throws DatabaseException;
    int create(Doctor doctor) throws DatabaseException;
    boolean update(Doctor doctor) throws DatabaseException;
    List<DoctorSchedule> getSchedulesByDoctorId(int doctorId) throws DatabaseException;
    boolean addSchedule(DoctorSchedule schedule) throws DatabaseException;
}
