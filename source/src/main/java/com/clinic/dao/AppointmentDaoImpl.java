package com.clinic.dao;

import com.clinic.exception.DatabaseException;
import com.clinic.model.Appointment;
import com.clinic.model.AppointmentStatus;
import com.clinic.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDaoImpl implements AppointmentDao {

    private static final String BASE_SELECT =
            "SELECT a.appointment_id, a.appointment_number, a.patient_id, " +
            "CONCAT(p.first_name, ' ', p.last_name) AS patient_name, p.patient_mrn, " +
            "a.doctor_id, CONCAT('Dr. ', d.first_name, ' ', d.last_name) AS doctor_name, " +
            "dep.name AS department_name, a.appointment_date, a.start_time, a.end_time, " +
            "a.status, a.reason_for_visit, a.cancellation_reason, a.booked_at, a.updated_at " +
            "FROM appointments a " +
            "JOIN patients p ON a.patient_id = p.patient_id " +
            "JOIN doctors d ON a.doctor_id = d.doctor_id " +
            "JOIN departments dep ON d.department_id = dep.department_id ";

    @Override
    public Appointment findById(int appointmentId) throws DatabaseException {
        String sql = BASE_SELECT + "WHERE a.appointment_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving appointment ID: " + appointmentId, e);
        }
        return null;
    }

    @Override
    public Appointment findByNumber(String appointmentNumber) throws DatabaseException {
        String sql = BASE_SELECT + "WHERE a.appointment_number = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, appointmentNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error querying appointment: " + appointmentNumber, e);
        }
        return null;
    }

    @Override
    public List<Appointment> findByPatientId(int patientId) throws DatabaseException {
        List<Appointment> list = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE a.patient_id = ? ORDER BY a.appointment_date DESC, a.start_time DESC";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching appointments for patient: " + patientId, e);
        }
        return list;
    }

    @Override
    public List<Appointment> findByDoctorId(int doctorId) throws DatabaseException {
        List<Appointment> list = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE a.doctor_id = ? ORDER BY a.appointment_date DESC, a.start_time ASC";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching appointments for doctor: " + doctorId, e);
        }
        return list;
    }

    @Override
    public List<Appointment> findByDoctorAndDate(int doctorId, Date date) throws DatabaseException {
        List<Appointment> list = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE a.doctor_id = ? AND a.appointment_date = ? ORDER BY a.start_time ASC";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            ps.setDate(2, date);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching doctor schedule for date: " + date, e);
        }
        return list;
    }

    @Override
    public List<Appointment> findAll() throws DatabaseException {
        List<Appointment> list = new ArrayList<>();
        String sql = BASE_SELECT + "ORDER BY a.appointment_date DESC, a.start_time DESC";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving global appointments", e);
        }
        return list;
    }

    @Override
    public boolean isSlotAvailable(int doctorId, Date date, Time startTime) throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ? " +
                     "AND start_time = ? AND status NOT IN ('CANCELLED', 'NO_SHOW')";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            ps.setDate(2, date);
            ps.setTime(3, startTime);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error verifying slot availability", e);
        }
        return false;
    }

    @Override
    public int create(Appointment apt) throws DatabaseException {
        String sql = "INSERT INTO appointments (appointment_number, patient_id, doctor_id, appointment_date, " +
                     "start_time, end_time, status, reason_for_visit) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, apt.getAppointmentNumber());
            ps.setInt(2, apt.getPatientId());
            ps.setInt(3, apt.getDoctorId());
            ps.setDate(4, apt.getAppointmentDate());
            ps.setTime(5, apt.getStartTime());
            ps.setTime(6, apt.getEndTime());
            ps.setString(7, apt.getStatus().name());
            ps.setString(8, apt.getReasonForVisit());

            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    apt.setAppointmentId(keys.getInt(1));
                    return apt.getAppointmentId();
                }
            }
            throw new DatabaseException("Failed to generate appointment ID.");
        } catch (SQLException e) {
            throw new DatabaseException("Error inserting appointment: " + apt.getAppointmentNumber(), e);
        }
    }

    @Override
    public boolean updateStatus(int appointmentId, AppointmentStatus status, String cancellationReason) throws DatabaseException {
        String sql = "UPDATE appointments SET status = ?, cancellation_reason = ? WHERE appointment_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setString(2, cancellationReason);
            ps.setInt(3, appointmentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error updating appointment status: " + appointmentId, e);
        }
    }

    @Override
    public int countTotalAppointments() throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM appointments";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            throw new DatabaseException("Error counting appointments", e);
        }
        return 0;
    }

    private Appointment mapRow(ResultSet rs) throws SQLException {
        Appointment a = new Appointment();
        a.setAppointmentId(rs.getInt("appointment_id"));
        a.setAppointmentNumber(rs.getString("appointment_number"));
        a.setPatientId(rs.getInt("patient_id"));
        a.setPatientName(rs.getString("patient_name"));
        a.setPatientMrn(rs.getString("patient_mrn"));
        a.setDoctorId(rs.getInt("doctor_id"));
        a.setDoctorName(rs.getString("doctor_name"));
        a.setDepartmentName(rs.getString("department_name"));
        a.setAppointmentDate(rs.getDate("appointment_date"));
        a.setStartTime(rs.getTime("start_time"));
        a.setEndTime(rs.getTime("end_time"));
        a.setStatus(AppointmentStatus.fromString(rs.getString("status")));
        a.setReasonForVisit(rs.getString("reason_for_visit"));
        a.setCancellationReason(rs.getString("cancellation_reason"));
        a.setBookedAt(rs.getTimestamp("booked_at"));
        a.setUpdatedAt(rs.getTimestamp("updated_at"));
        return a;
    }
}
