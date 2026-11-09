package com.clinic.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Core User entity for authentication and authorization.
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private int userId;
    private String email;
    private String passwordHash;
    private String salt;
    private Role role;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public User() {
        this.status = "ACTIVE";
    }

    public User(int userId, String email, String passwordHash, String salt, Role role, String status) {
        this.userId = userId;
        this.email = email;
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.role = role;
        this.status = status;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getSalt() { return salt; }
    public void setSalt(String salt) { this.salt = salt; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", status='" + status + '\'' +
                '}';
    }
}
