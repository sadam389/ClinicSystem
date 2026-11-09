package com.clinic.dao;

import com.clinic.exception.DatabaseException;
import com.clinic.model.Role;
import com.clinic.model.User;
import com.clinic.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {

    @Override
    public User findById(int userId) throws DatabaseException {
        String sql = "SELECT user_id, email, password_hash, salt, role, status, created_at, updated_at FROM users WHERE user_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find user by ID: " + userId, e);
        }
        return null;
    }

    @Override
    public User findByEmail(String email) throws DatabaseException {
        String sql = "SELECT user_id, email, password_hash, salt, role, status, created_at, updated_at FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to query user by email: " + email, e);
        }
        return null;
    }

    @Override
    public int create(User user) throws DatabaseException {
        String sql = "INSERT INTO users (email, password_hash, salt, role, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getSalt());
            ps.setString(4, user.getRole().name());
            ps.setString(5, user.getStatus() != null ? user.getStatus() : "ACTIVE");

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        user.setUserId(keys.getInt(1));
                        return user.getUserId();
                    }
                }
            }
            throw new DatabaseException("Creating user failed, no ID obtained.");
        } catch (SQLException e) {
            throw new DatabaseException("Error creating user record: " + user.getEmail(), e);
        }
    }

    @Override
    public boolean updatePassword(int userId, String newHash, String salt) throws DatabaseException {
        String sql = "UPDATE users SET password_hash = ?, salt = ? WHERE user_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newHash);
            ps.setString(2, salt);
            ps.setInt(3, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error updating password for user ID: " + userId, e);
        }
    }

    @Override
    public boolean updateStatus(int userId, String status) throws DatabaseException {
        String sql = "UPDATE users SET status = ? WHERE user_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error updating status for user ID: " + userId, e);
        }
    }

    @Override
    public List<User> findAll() throws DatabaseException {
        List<User> list = new ArrayList<>();
        String sql = "SELECT user_id, email, password_hash, salt, role, status, created_at, updated_at FROM users ORDER BY user_id ASC";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching user list", e);
        }
        return list;
    }

    private User mapRow(ResultSet rs) throws SQLException {
        User u = new User();
        u.setUserId(rs.getInt("user_id"));
        u.setEmail(rs.getString("email"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setSalt(rs.getString("salt"));
        u.setRole(Role.fromString(rs.getString("role")));
        u.setStatus(rs.getString("status"));
        u.setCreatedAt(rs.getTimestamp("created_at"));
        u.setUpdatedAt(rs.getTimestamp("updated_at"));
        return u;
    }
}
