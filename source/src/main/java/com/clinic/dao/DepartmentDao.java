package com.clinic.dao;

import com.clinic.exception.DatabaseException;
import com.clinic.model.Department;
import java.util.List;

public interface DepartmentDao {
    Department findById(int departmentId) throws DatabaseException;
    List<Department> findAll() throws DatabaseException;
    int create(Department department) throws DatabaseException;
}
