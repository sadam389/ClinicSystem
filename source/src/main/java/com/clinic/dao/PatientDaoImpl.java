package com.clinic.dao;

import com.clinic.exception.DatabaseException;
import com.clinic.model.Patient;
import com.clinic.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDaoImpl implements PatientDao {

    @Override
    public Patient findById(int patientId) throws DatabaseException {
        String sql = "SELECT p.patient_id, p.user_id, p.patient_mrn, p.first_name, p.last_name, " +
                "p.date_of_birth, p.gender, p.phone, u.email, p.address, p.blood_group, " +
                "p.emergency_contact, p.emergency_phone, p.allergies, p.created_at " +
                "FROM patients p JOIN users u ON p.user_id = u.user_id WHERE p.patient_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving patient by ID: " + patientId, e);
        }
        return null;
    }

    @Override
    public Patient findByUserId(int userId) throws DatabaseException {
        String sql = "SELECT p.patient_id, p.user_id, p.patient_mrn, p.first_name, p.last_name, " +
                "p.date_of_birth, p.gender, p.phone, u.email, p.address, p.blood_group, " +
                "p.emergency_contact, p.emergency_phone, p.allergies, p.created_at " +
                "FROM patients p JOIN users u ON p.user_id = u.user_id WHERE p.user_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding patient by user ID: " + userId, e);
        }
        return null;
    }

    @Override
    public Patient findByMRN(String mrn) throws DatabaseException {
        String sql = "SELECT p.patient_id, p.user_id, p.patient_mrn, p.first_name, p.last_name, " +
                "p.date_of_birth, p.gender, p.phone, u.email, p.address, p.blood_group, " +
                "p.emergency_contact, p.emergency_phone, p.allergies, p.created_at " +
                "FROM patients p JOIN users u ON p.user_id = u.user_id WHERE p.patient_mrn = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, mrn);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding patient by MRN: " + mrn, e);
        }
        return null;
    }

    @Override
    public List<Patient> findAll() throws DatabaseException {
        List<Patient> list = new ArrayList<>();
        String sql = "SELECT p.patient_id, p.user_id, p.patient_mrn, p.first_name, p.last_name, " +
                "p.date_of_birth, p.gender, p.phone, u.email, p.address, p.blood_group, " +
                "p.emergency_contact, p.emergency_phone, p.allergies, p.created_at " +
                "FROM patients p JOIN users u ON p.user_id = u.user_id ORDER BY p.last_name ASC";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving patient directory", e);
        }
        return list;
    }

    @Override
    public int create(Patient p) throws DatabaseException {
        String sql = "INSERT INTO patients (user_id, patient_mrn, first_name, last_name, date_of_birth, " +
                "gender, phone, address, blood_group, emergency_contact, emergency_phone, allergies) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, p.getUserId());
            ps.setString(2, p.getPatientMrn());
            ps.setString(3, p.getFirstName());
            ps.setString(4, p.getLastName());
            ps.setDate(5, p.getDateOfBirth());
            ps.setString(6, p.getGender());
            ps.setString(7, p.getPhone());
            ps.setString(8, p.getAddress());
            ps.setString(9, p.getBloodGroup());
            ps.setString(10, p.getEmergencyContact());
            ps.setString(11, p.getEmergencyPhone());
            ps.setString(12, p.getAllergies());

            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    p.setPatientId(keys.getInt(1));
                    return p.getPatientId();
                }
            }
            throw new DatabaseException("Failed to generate patient primary key.");
        } catch (SQLException e) {
            throw new DatabaseException("Error creating patient record: " + p.getPatientMrn(), e);
        }
    }

    @Override
    public boolean update(Patient p) throws DatabaseException {
        String sql = "UPDATE patients SET first_name = ?, last_name = ?, phone = ?, address = ?, " +
                "emergency_contact = ?, emergency_phone = ?, allergies = ? WHERE patient_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getFirstName());
            ps.setString(2, p.getLastName());
            ps.setString(3, p.getPhone());
            ps.setString(4, p.getAddress());
            ps.setString(5, p.getEmergencyContact());
            ps.setString(6, p.getEmergencyPhone());
            ps.setString(7, p.getAllergies());
            ps.setInt(8, p.getPatientId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error updating patient: " + p.getPatientId(), e);
        }
    }

    @Override
    public int countTotalPatients() throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM patients";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            throw new DatabaseException("Error counting total patients", e);
        }
        return 0;
    }

    private Patient mapRow(ResultSet rs) throws SQLException {
        Patient p = new Patient();
        p.setPatientId(rs.getInt("patient_id"));
        p.setUserId(rs.getInt("user_id"));
        p.setPatientMrn(rs.getString("patient_mrn"));
        p.setFirstName(rs.getString("first_name"));
        p.setLastName(rs.getString("last_name"));
        p.setDateOfBirth(rs.getDate("date_of_birth"));
        p.setGender(rs.getString("gender"));
        p.setPhone(rs.getString("phone"));
        p.setEmail(rs.getString("email"));
        p.setAddress(rs.getString("address"));
        p.setBloodGroup(rs.getString("blood_group"));
        p.setEmergencyContact(rs.getString("emergency_contact"));
        p.setEmergencyPhone(rs.getString("emergency_phone"));
        p.setAllergies(rs.getString("allergies"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        return p;
    }
}
