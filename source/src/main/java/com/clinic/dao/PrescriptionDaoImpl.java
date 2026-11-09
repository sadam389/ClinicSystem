package com.clinic.dao;

import com.clinic.exception.DatabaseException;
import com.clinic.model.Prescription;
import com.clinic.model.PrescriptionItem;
import com.clinic.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionDaoImpl implements PrescriptionDao {

    @Override
    public Prescription findById(int prescriptionId) throws DatabaseException {
        String sql = "SELECT p.prescription_id, p.record_id, p.doctor_id, " +
                "CONCAT('Dr. ', d.first_name, ' ', d.last_name) AS doctor_name, " +
                "p.patient_id, CONCAT(pat.first_name, ' ', pat.last_name) AS patient_name, " +
                "mr.diagnosis, p.advice, p.follow_up_date, p.created_at " +
                "FROM prescriptions p " +
                "JOIN doctors d ON p.doctor_id = d.doctor_id " +
                "JOIN patients pat ON p.patient_id = pat.patient_id " +
                "JOIN medical_records mr ON p.record_id = mr.record_id " +
                "WHERE p.prescription_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, prescriptionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Prescription rx = mapRow(rs);
                    rx.setItems(findItemsByPrescriptionId(conn, prescriptionId));
                    return rx;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving prescription: " + prescriptionId, e);
        }
        return null;
    }

    @Override
    public Prescription findByRecordId(int recordId) throws DatabaseException {
        String sql = "SELECT p.prescription_id, p.record_id, p.doctor_id, " +
                "CONCAT('Dr. ', d.first_name, ' ', d.last_name) AS doctor_name, " +
                "p.patient_id, CONCAT(pat.first_name, ' ', pat.last_name) AS patient_name, " +
                "mr.diagnosis, p.advice, p.follow_up_date, p.created_at " +
                "FROM prescriptions p " +
                "JOIN doctors d ON p.doctor_id = d.doctor_id " +
                "JOIN patients pat ON p.patient_id = pat.patient_id " +
                "JOIN medical_records mr ON p.record_id = mr.record_id " +
                "WHERE p.record_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, recordId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Prescription rx = mapRow(rs);
                    rx.setItems(findItemsByPrescriptionId(conn, rx.getPrescriptionId()));
                    return rx;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding prescription by record: " + recordId, e);
        }
        return null;
    }

    @Override
    public List<Prescription> findByPatientId(int patientId) throws DatabaseException {
        List<Prescription> list = new ArrayList<>();
        String sql = "SELECT p.prescription_id, p.record_id, p.doctor_id, " +
                "CONCAT('Dr. ', d.first_name, ' ', d.last_name) AS doctor_name, " +
                "p.patient_id, CONCAT(pat.first_name, ' ', pat.last_name) AS patient_name, " +
                "mr.diagnosis, p.advice, p.follow_up_date, p.created_at " +
                "FROM prescriptions p " +
                "JOIN doctors d ON p.doctor_id = d.doctor_id " +
                "JOIN patients pat ON p.patient_id = pat.patient_id " +
                "JOIN medical_records mr ON p.record_id = mr.record_id " +
                "WHERE p.patient_id = ? ORDER BY p.created_at DESC";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Prescription rx = mapRow(rs);
                    rx.setItems(findItemsByPrescriptionId(conn, rx.getPrescriptionId()));
                    list.add(rx);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching prescriptions for patient: " + patientId, e);
        }
        return list;
    }

    @Override
    public List<Prescription> findByDoctorId(int doctorId) throws DatabaseException {
        List<Prescription> list = new ArrayList<>();
        String sql = "SELECT p.prescription_id, p.record_id, p.doctor_id, " +
                "CONCAT('Dr. ', d.first_name, ' ', d.last_name) AS doctor_name, " +
                "p.patient_id, CONCAT(pat.first_name, ' ', pat.last_name) AS patient_name, " +
                "mr.diagnosis, p.advice, p.follow_up_date, p.created_at " +
                "FROM prescriptions p " +
                "JOIN doctors d ON p.doctor_id = d.doctor_id " +
                "JOIN patients pat ON p.patient_id = pat.patient_id " +
                "JOIN medical_records mr ON p.record_id = mr.record_id " +
                "WHERE p.doctor_id = ? ORDER BY p.created_at DESC";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Prescription rx = mapRow(rs);
                    rx.setItems(findItemsByPrescriptionId(conn, rx.getPrescriptionId()));
                    list.add(rx);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving doctor prescriptions: " + doctorId, e);
        }
        return list;
    }

    @Override
    public int create(Prescription prescription) throws DatabaseException {
        String sql = "INSERT INTO prescriptions (record_id, doctor_id, patient_id, advice, follow_up_date) " +
                     "VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false); // Atomic Transaction for prescription header + line items

            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, prescription.getRecordId());
                ps.setInt(2, prescription.getDoctorId());
                ps.setInt(3, prescription.getPatientId());
                ps.setString(4, prescription.getAdvice());
                ps.setDate(5, prescription.getFollowUpDate());
                ps.executeUpdate();

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        prescription.setPrescriptionId(keys.getInt(1));
                    } else {
                        throw new DatabaseException("Failed to generate prescription ID.");
                    }
                }
            }

            if (prescription.getItems() != null && !prescription.getItems().isEmpty()) {
                String itemSql = "INSERT INTO prescription_items (prescription_id, medicine_name, dosage, frequency, duration_days, instructions) " +
                                 "VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ips = conn.prepareStatement(itemSql)) {
                    for (PrescriptionItem it : prescription.getItems()) {
                        ips.setInt(1, prescription.getPrescriptionId());
                        ips.setString(2, it.getMedicineName());
                        ips.setString(3, it.getDosage());
                        ips.setString(4, it.getFrequency());
                        ips.setInt(5, it.getDurationDays());
                        ips.setString(6, it.getInstructions());
                        ips.addBatch();
                    }
                    ips.executeBatch();
                }
            }

            conn.commit();
            return prescription.getPrescriptionId();
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { /* ignored */ }
            }
            throw new DatabaseException("Transaction failed creating prescription", e);
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ex) { /* ignored */ }
            }
        }
    }

    @Override
    public boolean addItems(int prescriptionId, List<PrescriptionItem> items) throws DatabaseException {
        String sql = "INSERT INTO prescription_items (prescription_id, medicine_name, dosage, frequency, duration_days, instructions) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (PrescriptionItem it : items) {
                ps.setInt(1, prescriptionId);
                ps.setString(2, it.getMedicineName());
                ps.setString(3, it.getDosage());
                ps.setString(4, it.getFrequency());
                ps.setInt(5, it.getDurationDays());
                ps.setString(6, it.getInstructions());
                ps.addBatch();
            }
            int[] results = ps.executeBatch();
            return results.length > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to append prescription items", e);
        }
    }

    private List<PrescriptionItem> findItemsByPrescriptionId(Connection conn, int prescriptionId) throws SQLException {
        List<PrescriptionItem> items = new ArrayList<>();
        String sql = "SELECT item_id, prescription_id, medicine_name, dosage, frequency, duration_days, instructions " +
                     "FROM prescription_items WHERE prescription_id = ? ORDER BY item_id ASC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, prescriptionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PrescriptionItem it = new PrescriptionItem();
                    it.setItemId(rs.getInt("item_id"));
                    it.setPrescriptionId(rs.getInt("prescription_id"));
                    it.setMedicineName(rs.getString("medicine_name"));
                    it.setDosage(rs.getString("dosage"));
                    it.setFrequency(rs.getString("frequency"));
                    it.setDurationDays(rs.getInt("duration_days"));
                    it.setInstructions(rs.getString("instructions"));
                    items.add(it);
                }
            }
        }
        return items;
    }

    private Prescription mapRow(ResultSet rs) throws SQLException {
        Prescription rx = new Prescription();
        rx.setPrescriptionId(rs.getInt("prescription_id"));
        rx.setRecordId(rs.getInt("record_id"));
        rx.setDoctorId(rs.getInt("doctor_id"));
        rx.setDoctorName(rs.getString("doctor_name"));
        rx.setPatientId(rs.getInt("patient_id"));
        rx.setPatientName(rs.getString("patient_name"));
        rx.setDiagnosis(rs.getString("diagnosis"));
        rx.setAdvice(rs.getString("advice"));
        rx.setFollowUpDate(rs.getDate("follow_up_date"));
        rx.setCreatedAt(rs.getTimestamp("created_at"));
        return rx;
    }
}
