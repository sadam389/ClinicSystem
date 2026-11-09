package com.clinic.dao;

import com.clinic.exception.DatabaseException;
import com.clinic.model.Department;
import com.clinic.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoImpl implements DepartmentDao {

    @Override
    public Department findById(int departmentId) throws DatabaseException {
        String sql = "SELECT department_id, name, code, description, consultation_fee, created_at FROM departments WHERE department_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, departmentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving department: " + departmentId, e);
        }
        return null;
    }

    @Override
    public List<Department> findAll() throws DatabaseException {
        List<Department> list = new ArrayList<>();
        String sql = "SELECT department_id, name, code, description, consultation_fee, created_at FROM departments ORDER BY name ASC";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching departments", e);
        }
        return list;
    }

    @Override
    public int create(Department department) throws DatabaseException {
        String sql = "INSERT INTO departments (name, code, description, consultation_fee) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, department.getName());
            ps.setString(2, department.getCode());
            ps.setString(3, department.getDescription());
            ps.setBigDecimal(4, department.getConsultationFee());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    department.setDepartmentId(keys.getInt(1));
                    return department.getDepartmentId();
                }
            }
            throw new DatabaseException("Creating department failed, no ID obtained.");
        } catch (SQLException e) {
            throw new DatabaseException("Error creating department: " + department.getName(), e);
        }
    }

    private Department mapRow(ResultSet rs) throws SQLException {
        Department d = new Department();
        d.setDepartmentId(rs.getInt("department_id"));
        d.setName(rs.getString("name"));
        d.setCode(rs.getString("code"));
        d.setDescription(rs.getString("description"));
        d.setConsultationFee(rs.getBigDecimal("consultation_fee"));
        d.setCreatedAt(rs.getTimestamp("created_at"));
        return d;
    }
}
