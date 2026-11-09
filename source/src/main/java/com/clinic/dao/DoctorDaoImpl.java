package com.clinic.dao;

import com.clinic.exception.DatabaseException;
import com.clinic.model.Doctor;
import com.clinic.model.DoctorSchedule;
import com.clinic.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorDaoImpl implements DoctorDao {

    @Override
    public Doctor findById(int doctorId) throws DatabaseException {
        String sql = "SELECT d.doctor_id, d.user_id, d.department_id, dep.name AS department_name, " +
                "d.license_number, d.first_name, d.last_name, d.phone, u.email, d.qualification, " +
                "d.experience_years, d.room_number, d.max_daily_patients, d.is_available, d.created_at " +
                "FROM doctors d " +
                "JOIN users u ON d.user_id = u.user_id " +
                "JOIN departments dep ON d.department_id = dep.department_id " +
                "WHERE d.doctor_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding doctor by ID: " + doctorId, e);
        }
        return null;
    }

    @Override
    public Doctor findByUserId(int userId) throws DatabaseException {
        String sql = "SELECT d.doctor_id, d.user_id, d.department_id, dep.name AS department_name, " +
                "d.license_number, d.first_name, d.last_name, d.phone, u.email, d.qualification, " +
                "d.experience_years, d.room_number, d.max_daily_patients, d.is_available, d.created_at " +
                "FROM doctors d " +
                "JOIN users u ON d.user_id = u.user_id " +
                "JOIN departments dep ON d.department_id = dep.department_id " +
                "WHERE d.user_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding doctor by user ID: " + userId, e);
        }
        return null;
    }

    @Override
    public List<Doctor> findAll() throws DatabaseException {
        List<Doctor> list = new ArrayList<>();
        String sql = "SELECT d.doctor_id, d.user_id, d.department_id, dep.name AS department_name, " +
                "d.license_number, d.first_name, d.last_name, d.phone, u.email, d.qualification, " +
                "d.experience_years, d.room_number, d.max_daily_patients, d.is_available, d.created_at " +
                "FROM doctors d " +
                "JOIN users u ON d.user_id = u.user_id " +
                "JOIN departments dep ON d.department_id = dep.department_id " +
                "ORDER BY d.last_name ASC";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching doctor list", e);
        }
        return list;
    }

    @Override
    public List<Doctor> findByDepartmentId(int departmentId) throws DatabaseException {
        List<Doctor> list = new ArrayList<>();
        String sql = "SELECT d.doctor_id, d.user_id, d.department_id, dep.name AS department_name, " +
                "d.license_number, d.first_name, d.last_name, d.phone, u.email, d.qualification, " +
                "d.experience_years, d.room_number, d.max_daily_patients, d.is_available, d.created_at " +
                "FROM doctors d " +
                "JOIN users u ON d.user_id = u.user_id " +
                "JOIN departments dep ON d.department_id = dep.department_id " +
                "WHERE d.department_id = ? AND d.is_available = TRUE " +
                "ORDER BY d.last_name ASC";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, departmentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching doctors for department: " + departmentId, e);
        }
        return list;
    }

    @Override
    public int create(Doctor doctor) throws DatabaseException {
        String sql = "INSERT INTO doctors (user_id, department_id, license_number, first_name, last_name, " +
                "phone, qualification, experience_years, room_number, max_daily_patients, is_available) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, doctor.getUserId());
            ps.setInt(2, doctor.getDepartmentId());
            ps.setString(3, doctor.getLicenseNumber());
            ps.setString(4, doctor.getFirstName());
            ps.setString(5, doctor.getLastName());
            ps.setString(6, doctor.getPhone());
            ps.setString(7, doctor.getQualification());
            ps.setInt(8, doctor.getExperienceYears());
            ps.setString(9, doctor.getRoomNumber());
            ps.setInt(10, doctor.getMaxDailyPatients());
            ps.setBoolean(11, doctor.isAvailable());

            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    doctor.setDoctorId(keys.getInt(1));
                    return doctor.getDoctorId();
                }
            }
            throw new DatabaseException("Failed to obtain doctor ID.");
        } catch (SQLException e) {
            throw new DatabaseException("Error creating doctor record: " + doctor.getFullName(), e);
        }
    }

    @Override
    public boolean update(Doctor doctor) throws DatabaseException {
        String sql = "UPDATE doctors SET department_id = ?, phone = ?, qualification = ?, " +
                "experience_years = ?, room_number = ?, max_daily_patients = ?, is_available = ? " +
                "WHERE doctor_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctor.getDepartmentId());
            ps.setString(2, doctor.getPhone());
            ps.setString(3, doctor.getQualification());
            ps.setInt(4, doctor.getExperienceYears());
            ps.setString(5, doctor.getRoomNumber());
            ps.setInt(6, doctor.getMaxDailyPatients());
            ps.setBoolean(7, doctor.isAvailable());
            ps.setInt(8, doctor.getDoctorId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error updating doctor profile: " + doctor.getDoctorId(), e);
        }
    }

    @Override
    public List<DoctorSchedule> getSchedulesByDoctorId(int doctorId) throws DatabaseException {
        List<DoctorSchedule> list = new ArrayList<>();
        String sql = "SELECT schedule_id, doctor_id, day_of_week, start_time, end_time, slot_duration_minutes, is_active " +
                "FROM doctor_schedules WHERE doctor_id = ? AND is_active = TRUE ORDER BY FIELD(day_of_week, 'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY')";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DoctorSchedule ds = new DoctorSchedule();
                    ds.setScheduleId(rs.getInt("schedule_id"));
                    ds.setDoctorId(rs.getInt("doctor_id"));
                    ds.setDayOfWeek(rs.getString("day_of_week"));
                    ds.setStartTime(rs.getTime("start_time"));
                    ds.setEndTime(rs.getTime("end_time"));
                    ds.setSlotDurationMinutes(rs.getInt("slot_duration_minutes"));
                    ds.setActive(rs.getBoolean("is_active"));
                    list.add(ds);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving doctor schedules: " + doctorId, e);
        }
        return list;
    }

    @Override
    public boolean addSchedule(DoctorSchedule schedule) throws DatabaseException {
        String sql = "INSERT INTO doctor_schedules (doctor_id, day_of_week, start_time, end_time, slot_duration_minutes, is_active) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, schedule.getDoctorId());
            ps.setString(2, schedule.getDayOfWeek());
            ps.setTime(3, schedule.getStartTime());
            ps.setTime(4, schedule.getEndTime());
            ps.setInt(5, schedule.getSlotDurationMinutes());
            ps.setBoolean(6, schedule.isActive());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error adding schedule for doctor: " + schedule.getDoctorId(), e);
        }
    }

    private Doctor mapRow(ResultSet rs) throws SQLException {
        Doctor d = new Doctor();
        d.setDoctorId(rs.getInt("doctor_id"));
        d.setUserId(rs.getInt("user_id"));
        d.setDepartmentId(rs.getInt("department_id"));
        d.setDepartmentName(rs.getString("department_name"));
        d.setLicenseNumber(rs.getString("license_number"));
        d.setFirstName(rs.getString("first_name"));
        d.setLastName(rs.getString("last_name"));
        d.setPhone(rs.getString("phone"));
        d.setEmail(rs.getString("email"));
        d.setQualification(rs.getString("qualification"));
        d.setExperienceYears(rs.getInt("experience_years"));
        d.setRoomNumber(rs.getString("room_number"));
        d.setMaxDailyPatients(rs.getInt("max_daily_patients"));
        d.setAvailable(rs.getBoolean("is_available"));
        d.setCreatedAt(rs.getTimestamp("created_at"));
        return d;
    }
}
