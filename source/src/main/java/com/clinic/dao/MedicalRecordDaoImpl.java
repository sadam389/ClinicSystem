package com.clinic.dao;

import com.clinic.exception.DatabaseException;
import com.clinic.model.MedicalRecord;
import com.clinic.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecordDaoImpl implements MedicalRecordDao {

    private static final String BASE_SELECT =
            "SELECT mr.record_id, mr.patient_id, CONCAT(p.first_name, ' ', p.last_name) AS patient_name, " +
            "p.patient_mrn, mr.doctor_id, CONCAT('Dr. ', d.first_name, ' ', d.last_name) AS doctor_name, " +
            "mr.appointment_id, mr.diagnosis, mr.symptoms, mr.blood_pressure, mr.pulse_bpm, " +
            "mr.temperature_celsius, mr.weight_kg, mr.clinical_notes, mr.created_at " +
            "FROM medical_records mr " +
            "JOIN patients p ON mr.patient_id = p.patient_id " +
            "JOIN doctors d ON mr.doctor_id = d.doctor_id ";

    @Override
    public MedicalRecord findById(int recordId) throws DatabaseException {
        String sql = BASE_SELECT + "WHERE mr.record_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, recordId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving medical record: " + recordId, e);
        }
        return null;
    }

    @Override
    public MedicalRecord findByAppointmentId(int appointmentId) throws DatabaseException {
        String sql = BASE_SELECT + "WHERE mr.appointment_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding record by appointment: " + appointmentId, e);
        }
        return null;
    }

    @Override
    public List<MedicalRecord> findByPatientId(int patientId) throws DatabaseException {
        List<MedicalRecord> list = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE mr.patient_id = ? ORDER BY mr.created_at DESC";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving patient records: " + patientId, e);
        }
        return list;
    }

    @Override
    public int create(MedicalRecord record) throws DatabaseException {
        String sql = "INSERT INTO medical_records (patient_id, doctor_id, appointment_id, diagnosis, " +
                     "symptoms, blood_pressure, pulse_bpm, temperature_celsius, weight_kg, clinical_notes) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, record.getPatientId());
            ps.setInt(2, record.getDoctorId());
            if (record.getAppointmentId() != null) ps.setInt(3, record.getAppointmentId());
            else ps.setNull(3, Types.INTEGER);
            ps.setString(4, record.getDiagnosis());
            ps.setString(5, record.getSymptoms());
            ps.setString(6, record.getBloodPressure());
            if (record.getPulseBpm() != null) ps.setInt(7, record.getPulseBpm());
            else ps.setNull(7, Types.INTEGER);
            ps.setBigDecimal(8, record.getTemperatureCelsius());
            ps.setBigDecimal(9, record.getWeightKg());
            ps.setString(10, record.getClinicalNotes());

            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    record.setRecordId(keys.getInt(1));
                    return record.getRecordId();
                }
            }
            throw new DatabaseException("Failed to generate medical record ID.");
        } catch (SQLException e) {
            throw new DatabaseException("Error creating medical record", e);
        }
    }

    private MedicalRecord mapRow(ResultSet rs) throws SQLException {
        MedicalRecord mr = new MedicalRecord();
        mr.setRecordId(rs.getInt("record_id"));
        mr.setPatientId(rs.getInt("patient_id"));
        mr.setPatientName(rs.getString("patient_name"));
        mr.setPatientMrn(rs.getString("patient_mrn"));
        mr.setDoctorId(rs.getInt("doctor_id"));
        mr.setDoctorName(rs.getString("doctor_name"));
        int aptId = rs.getInt("appointment_id");
        if (!rs.wasNull()) mr.setAppointmentId(aptId);
        mr.setDiagnosis(rs.getString("diagnosis"));
        mr.setSymptoms(rs.getString("symptoms"));
        mr.setBloodPressure(rs.getString("blood_pressure"));
        int pulse = rs.getInt("pulse_bpm");
        if (!rs.wasNull()) mr.setPulseBpm(pulse);
        mr.setTemperatureCelsius(rs.getBigDecimal("temperature_celsius"));
        mr.setWeightKg(rs.getBigDecimal("weight_kg"));
        mr.setClinicalNotes(rs.getString("clinical_notes"));
        mr.setCreatedAt(rs.getTimestamp("created_at"));
        return mr;
    }
}
