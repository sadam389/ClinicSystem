-- =====================================================================
-- DATABASE SCHEMA: Clinic Appointment and Patient Records System
-- Course: Software Construction and Development (SCD)
-- Target RDBMS: MySQL 8.0+ / MariaDB 10.5+
-- =====================================================================

DROP DATABASE IF EXISTS clinic_db;
CREATE DATABASE clinic_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE clinic_db;

-- ---------------------------------------------------------------------
-- TABLE: users
-- Core authentication table for all system actors
-- ---------------------------------------------------------------------
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    salt VARCHAR(64) NOT NULL,
    role ENUM('PATIENT', 'DOCTOR', 'ADMIN') NOT NULL,
    status ENUM('ACTIVE', 'SUSPENDED', 'PENDING') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_email (email),
    INDEX idx_user_role (role)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- TABLE: departments
-- Clinical specialties and departments
-- ---------------------------------------------------------------------
CREATE TABLE departments (
    department_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    code VARCHAR(20) NOT NULL UNIQUE,
    description TEXT,
    consultation_fee DECIMAL(10,2) NOT NULL DEFAULT 50.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- TABLE: doctors
-- Doctor profiles linked to user credentials
-- ---------------------------------------------------------------------
CREATE TABLE doctors (
    doctor_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL UNIQUE,
    department_id INT NOT NULL,
    license_number VARCHAR(50) NOT NULL UNIQUE,
    first_name VARCHAR(60) NOT NULL,
    last_name VARCHAR(60) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    qualification VARCHAR(100) NOT NULL,
    experience_years INT NOT NULL DEFAULT 1,
    room_number VARCHAR(20) NOT NULL,
    max_daily_patients INT NOT NULL DEFAULT 20,
    is_available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_doctor_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_doctor_department FOREIGN KEY (department_id) REFERENCES departments(department_id),
    INDEX idx_doctor_name (last_name, first_name)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- TABLE: doctor_schedules
-- Weekly consultation slots and time boundaries
-- ---------------------------------------------------------------------
CREATE TABLE doctor_schedules (
    schedule_id INT AUTO_INCREMENT PRIMARY KEY,
    doctor_id INT NOT NULL,
    day_of_week ENUM('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY') NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    slot_duration_minutes INT NOT NULL DEFAULT 30,
    is_active BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_schedule_doctor FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id) ON DELETE CASCADE,
    UNIQUE KEY uq_doctor_day_time (doctor_id, day_of_week, start_time)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- TABLE: patients
-- Patient demographic and core medical metadata
-- ---------------------------------------------------------------------
CREATE TABLE patients (
    patient_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL UNIQUE,
    patient_mrn VARCHAR(30) NOT NULL UNIQUE, -- Medical Record Number
    first_name VARCHAR(60) NOT NULL,
    last_name VARCHAR(60) NOT NULL,
    date_of_birth DATE NOT NULL,
    gender ENUM('MALE', 'FEMALE', 'OTHER') NOT NULL,
    phone VARCHAR(20) NOT NULL,
    address VARCHAR(255) NOT NULL,
    blood_group ENUM('A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-') NOT NULL,
    emergency_contact VARCHAR(100) NOT NULL,
    emergency_phone VARCHAR(20) NOT NULL,
    allergies TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_patient_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_patient_mrn (patient_mrn),
    INDEX idx_patient_phone (phone)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- TABLE: appointments
-- Outpatient appointment bookings and state transitions
-- ---------------------------------------------------------------------
CREATE TABLE appointments (
    appointment_id INT AUTO_INCREMENT PRIMARY KEY,
    appointment_number VARCHAR(32) NOT NULL UNIQUE,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    appointment_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    status ENUM('PENDING', 'CONFIRMED', 'COMPLETED', 'CANCELLED', 'NO_SHOW') DEFAULT 'PENDING',
    reason_for_visit VARCHAR(255) NOT NULL,
    cancellation_reason VARCHAR(255),
    booked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_apt_patient FOREIGN KEY (patient_id) REFERENCES patients(patient_id),
    CONSTRAINT fk_apt_doctor FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id),
    UNIQUE KEY uq_doctor_slot (doctor_id, appointment_date, start_time),
    INDEX idx_apt_date (appointment_date),
    INDEX idx_apt_status (status)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- TABLE: medical_records
-- Patient visit notes, vital signs, and clinical diagnostic records
-- ---------------------------------------------------------------------
CREATE TABLE medical_records (
    record_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    appointment_id INT UNIQUE,
    diagnosis VARCHAR(255) NOT NULL,
    symptoms TEXT NOT NULL,
    blood_pressure VARCHAR(20),
    pulse_bpm INT,
    temperature_celsius DECIMAL(4,1),
    weight_kg DECIMAL(5,2),
    clinical_notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_rec_patient FOREIGN KEY (patient_id) REFERENCES patients(patient_id),
    CONSTRAINT fk_rec_doctor FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id),
    CONSTRAINT fk_rec_appointment FOREIGN KEY (appointment_id) REFERENCES appointments(appointment_id) ON DELETE SET NULL,
    INDEX idx_rec_patient (patient_id)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- TABLE: prescriptions
-- Digital prescription headers issued by doctors
-- ---------------------------------------------------------------------
CREATE TABLE prescriptions (
    prescription_id INT AUTO_INCREMENT PRIMARY KEY,
    record_id INT NOT NULL UNIQUE,
    doctor_id INT NOT NULL,
    patient_id INT NOT NULL,
    advice TEXT,
    follow_up_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_rx_record FOREIGN KEY (record_id) REFERENCES medical_records(record_id) ON DELETE CASCADE,
    CONSTRAINT fk_rx_doctor FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id),
    CONSTRAINT fk_rx_patient FOREIGN KEY (patient_id) REFERENCES patients(patient_id)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- TABLE: prescription_items
-- Specific medication items, dosages, frequencies, and durations
-- ---------------------------------------------------------------------
CREATE TABLE prescription_items (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    prescription_id INT NOT NULL,
    medicine_name VARCHAR(120) NOT NULL,
    dosage VARCHAR(50) NOT NULL,
    frequency VARCHAR(50) NOT NULL,      -- e.g., '1-0-1', 'Once daily'
    duration_days INT NOT NULL DEFAULT 7,
    instructions VARCHAR(255),           -- e.g., 'After meals'
    CONSTRAINT fk_rx_item_parent FOREIGN KEY (prescription_id) REFERENCES prescriptions(prescription_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- TABLE: audit_logs
-- Immutable security and change control logging
-- ---------------------------------------------------------------------
CREATE TABLE audit_logs (
    log_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    action VARCHAR(80) NOT NULL,
    entity_name VARCHAR(50) NOT NULL,
    entity_id VARCHAR(50),
    details TEXT,
    ip_address VARCHAR(45),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_audit_time (timestamp),
    INDEX idx_audit_action (action)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- TABLE: legacy_patient_staging
-- Buffer table for salvaged paper/CSV legacy patient records
-- ---------------------------------------------------------------------
CREATE TABLE legacy_patient_staging (
    staging_id INT AUTO_INCREMENT PRIMARY KEY,
    raw_mrn VARCHAR(50),
    raw_full_name VARCHAR(120),
    raw_dob VARCHAR(30),
    raw_phone VARCHAR(50),
    raw_address TEXT,
    import_status ENUM('STAGED', 'PROCESSED', 'FAILED', 'DUPLICATE') DEFAULT 'STAGED',
    validation_notes TEXT,
    imported_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;
