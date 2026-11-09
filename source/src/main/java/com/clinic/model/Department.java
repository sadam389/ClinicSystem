package com.clinic.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Clinical Department entity (Cardiology, Pediatrics, etc.).
 */
public class Department implements Serializable {
    private static final long serialVersionUID = 1L;

    private int departmentId;
    private String name;
    private String code;
    private String description;
    private BigDecimal consultationFee;
    private Timestamp createdAt;

    public Department() {}

    public Department(int departmentId, String name, String code, String description, BigDecimal consultationFee) {
        this.departmentId = departmentId;
        this.name = name;
        this.code = code;
        this.description = description;
        this.consultationFee = consultationFee;
    }

    public int getDepartmentId() { return departmentId; }
    public void setDepartmentId(int departmentId) { this.departmentId = departmentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getConsultationFee() { return consultationFee; }
    public void setConsultationFee(BigDecimal consultationFee) { this.consultationFee = consultationFee; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
