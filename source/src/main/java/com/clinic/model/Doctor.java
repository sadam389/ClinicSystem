package com.clinic.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Doctor profile model.
 */
public class Doctor implements Serializable {
    private static final long serialVersionUID = 1L;

    private int doctorId;
    private int userId;
    private int departmentId;
    private String departmentName;
    private String licenseNumber;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String qualification;
    private int experienceYears;
    private String roomNumber;
    private int maxDailyPatients;
    private boolean available;
    private Timestamp createdAt;

    public Doctor() {
        this.available = true;
        this.maxDailyPatients = 20;
    }

    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getDepartmentId() { return departmentId; }
    public void setDepartmentId(int departmentId) { this.departmentId = departmentId; }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFullName() {
        return "Dr. " + firstName + " " + lastName;
    }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }

    public int getExperienceYears() { return experienceYears; }
    public void setExperienceYears(int experienceYears) { this.experienceYears = experienceYears; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public int getMaxDailyPatients() { return maxDailyPatients; }
    public void setMaxDailyPatients(int maxDailyPatients) { this.maxDailyPatients = maxDailyPatients; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
