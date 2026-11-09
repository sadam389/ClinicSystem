-- =====================================================================
-- SAMPLE DATA SEED SCRIPT: Clinic Appointment and Patient Records System
-- Course: Software Construction and Development (SCD)
-- Password for all seed accounts is set with SHA-256 hash
-- (Pass: AdminPass123!, DoctorPass123!, PatientPass123!)
-- =====================================================================

USE clinic_db;

-- ---------------------------------------------------------------------
-- 1. DEPARTMENTS
-- ---------------------------------------------------------------------
INSERT INTO departments (department_id, name, code, description, consultation_fee) VALUES
(1, 'Cardiology', 'CARD', 'Comprehensive cardiovascular diagnostics, ECG, echocardiograms, and chronic hypertension care.', 80.00),
(2, 'Pediatrics', 'PED', 'Specialized healthcare for infants, children, and adolescents, including immunizations and growth tracking.', 60.00),
(3, 'General Medicine', 'GEN', 'Primary adult care, systemic wellness evaluations, preventative health, and fever clinics.', 45.00),
(4, 'Orthopedics', 'ORTH', 'Musculoskeletal system care, joint pain management, fracture recovery, and posture rehabilitation.', 75.00),
(5, 'Dermatology', 'DERM', 'Clinical skin pathology, eczema management, allergy patch tests, and chronic acne treatment.', 65.00);

-- ---------------------------------------------------------------------
-- 2. USERS (ADMIN, DOCTORS, PATIENTS)
-- Passwords hashed using standard SHA-256 with salt 'STATIC_SALT_MEDICARE_2026'
-- Admin:    AdminPass123!   -> 8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918
-- Doctor:   DoctorPass123!  -> 7d94f29108c903bc047c32b55b93d6118d0421e491c1d856019323fb3aa28014
-- Patient:  PatientPass123! -> 9f83793b4a09b53777714ddbda3fc83478fe6bc9d38d0f33eb41f8ccc4ff0643
-- ---------------------------------------------------------------------
INSERT INTO users (user_id, email, password_hash, salt, role, status) VALUES
(1, 'admin@medicare.org', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'SALT_ADM_001', 'ADMIN', 'ACTIVE'),
(2, 'dr.smith@medicare.org', '7d94f29108c903bc047c32b55b93d6118d0421e491c1d856019323fb3aa28014', 'SALT_DOC_001', 'DOCTOR', 'ACTIVE'),
(3, 'dr.chen@medicare.org', '7d94f29108c903bc047c32b55b93d6118d0421e491c1d856019323fb3aa28014', 'SALT_DOC_002', 'DOCTOR', 'ACTIVE'),
(4, 'dr.patel@medicare.org', '7d94f29108c903bc047c32b55b93d6118d0421e491c1d856019323fb3aa28014', 'SALT_DOC_003', 'DOCTOR', 'ACTIVE'),
(5, 'john.doe@gmail.com', '9f83793b4a09b53777714ddbda3fc83478fe6bc9d38d0f33eb41f8ccc4ff0643', 'SALT_PAT_001', 'PATIENT', 'ACTIVE'),
(6, 'sarah.jenkins@outlook.com', '9f83793b4a09b53777714ddbda3fc83478fe6bc9d38d0f33eb41f8ccc4ff0643', 'SALT_PAT_002', 'PATIENT', 'ACTIVE'),
(7, 'robert.taylor@yahoo.com', '9f83793b4a09b53777714ddbda3fc83478fe6bc9d38d0f33eb41f8ccc4ff0643', 'SALT_PAT_003', 'PATIENT', 'ACTIVE');

-- ---------------------------------------------------------------------
-- 3. DOCTORS
-- ---------------------------------------------------------------------
INSERT INTO doctors (doctor_id, user_id, department_id, license_number, first_name, last_name, phone, qualification, experience_years, room_number, max_daily_patients, is_available) VALUES
(1, 2, 1, 'MED-LIC-88901', 'Marcus', 'Smith', '+1-555-019-2831', 'MD, FACC - Cardiovascular Specialist', 14, 'Room 204', 25, TRUE),
(2, 3, 2, 'MED-LIC-92410', 'Emily', 'Chen', '+1-555-014-9923', 'MD (Pediatrics), FAAP', 9, 'Room 105', 20, TRUE),
(3, 4, 3, 'MED-LIC-77123', 'Rajesh', 'Patel', '+1-555-018-4411', 'MBBS, MD - Internal Medicine', 12, 'Room 102', 30, TRUE);

-- ---------------------------------------------------------------------
-- 4. DOCTOR SCHEDULES
-- ---------------------------------------------------------------------
INSERT INTO doctor_schedules (schedule_id, doctor_id, day_of_week, start_time, end_time, slot_duration_minutes, is_active) VALUES
(1, 1, 'MONDAY', '09:00:00', '13:00:00', 30, TRUE),
(2, 1, 'WEDNESDAY', '09:00:00', '13:00:00', 30, TRUE),
(3, 1, 'FRIDAY', '09:00:00', '13:00:00', 30, TRUE),
(4, 2, 'TUESDAY', '10:00:00', '15:00:00', 30, TRUE),
(5, 2, 'THURSDAY', '10:00:00', '15:00:00', 30, TRUE),
(6, 3, 'MONDAY', '08:30:00', '14:30:00', 30, TRUE),
(7, 3, 'TUESDAY', '08:30:00', '14:30:00', 30, TRUE),
(8, 3, 'THURSDAY', '08:30:00', '14:30:00', 30, TRUE);

-- ---------------------------------------------------------------------
-- 5. PATIENTS
-- ---------------------------------------------------------------------
INSERT INTO patients (patient_id, user_id, patient_mrn, first_name, last_name, date_of_birth, gender, phone, address, blood_group, emergency_contact, emergency_phone, allergies) VALUES
(1, 5, 'MRN-2026-0001', 'John', 'Doe', '1988-04-12', 'MALE', '+1-555-234-5678', '742 Evergreen Terrace, Springfield', 'O+', 'Mary Doe (Spouse)', '+1-555-234-9999', 'Penicillin, Shellfish'),
(2, 6, 'MRN-2026-0002', 'Sarah', 'Jenkins', '1995-09-23', 'FEMALE', '+1-555-345-6789', '124 Elm Street, Riverdale', 'A+', 'Thomas Jenkins (Father)', '+1-555-345-1122', 'Sulfa drugs, Latex'),
(3, 7, 'MRN-2026-0003', 'Robert', 'Taylor', '1976-11-05', 'MALE', '+1-555-456-7890', '58 Baker Way, Metro City', 'B-', 'Eleanor Taylor (Sister)', '+1-555-456-8800', 'None known');

-- ---------------------------------------------------------------------
-- 6. APPOINTMENTS
-- ---------------------------------------------------------------------
INSERT INTO appointments (appointment_id, appointment_number, patient_id, doctor_id, appointment_date, start_time, end_time, status, reason_for_visit, cancellation_reason) VALUES
(1, 'APT-20261101-001', 1, 1, '2026-11-02', '09:30:00', '10:00:00', 'COMPLETED', 'Routine hypertension follow-up and ECG evaluation.', NULL),
(2, 'APT-20261104-002', 2, 2, '2026-11-05', '10:30:00', '11:00:00', 'COMPLETED', 'Persistent allergic cough and seasonal bronchial irritation.', NULL),
(3, 'APT-20261110-003', 1, 3, '2026-11-12', '09:00:00', '09:30:00', 'CONFIRMED', 'Annual general physical checkup and routine blood panel review.', NULL),
(4, 'APT-20261115-004', 3, 1, '2026-11-16', '11:00:00', '11:30:00', 'CONFIRMED', 'Chest tightness upon mild exertion, initial cardiac screening.', NULL),
(5, 'APT-20261101-005', 2, 3, '2026-11-03', '12:00:00', '12:30:00', 'CANCELLED', 'Mild viral fever', 'Patient requested reschedule due to work travel');

-- ---------------------------------------------------------------------
-- 7. MEDICAL RECORDS
-- ---------------------------------------------------------------------
INSERT INTO medical_records (record_id, patient_id, doctor_id, appointment_id, diagnosis, symptoms, blood_pressure, pulse_bpm, temperature_celsius, weight_kg, clinical_notes) VALUES
(1, 1, 1, 1, 'Stage 1 Essential Hypertension (Controlled)', 'Occasional occipital morning headache, mild fatigue under workload stress.', '134/86', 74, 36.7, 82.5, 'Normal sinus rhythm confirmed on 12-lead resting ECG. Patient responds well to current ACE inhibitor regimen. Advised dietary sodium moderation and 30 mins daily walking.'),
(2, 2, 2, 2, 'Allergic Rhinosinusitis & Hyperreactive Airway', 'Paroxysmal sneezing, clear rhinorrhea, nighttime dry cough.', '112/70', 80, 36.9, 58.2, 'Nasal mucosa shows pale boggy turbinates. Lungs clear to auscultation bilaterally. No wheezing on forced expiration. Antihistamine and nasal steroid course prescribed.');

-- ---------------------------------------------------------------------
-- 8. PRESCRIPTIONS & ITEMS
-- ---------------------------------------------------------------------
INSERT INTO prescriptions (prescription_id, record_id, doctor_id, patient_id, advice, follow_up_date) VALUES
(1, 1, 1, 1, 'Continue low-sodium diet (<2g/day). Log BP readings morning and night. Return immediately if chest pain occurs.', '2026-12-05'),
(2, 2, 2, 2, 'Avoid damp dust, pet dander, and cold dry winds. Rinse mouth after nasal inhalations.', '2026-11-20');

INSERT INTO prescription_items (item_id, prescription_id, medicine_name, dosage, frequency, duration_days, instructions) VALUES
(1, 1, 'Lisinopril Oral Tablet', '10 mg', '1-0-0 (Once daily in morning)', 30, 'Take with or without food. Avoid potassium supplements.'),
(2, 1, 'Hydrochlorothiazide', '12.5 mg', '1-0-0 (Morning)', 30, 'Take early morning to prevent nocturnal diuresis.'),
(3, 2, 'Levocetirizine Dihydrochloride', '5 mg', '0-0-1 (At bedtime)', 10, 'May cause mild drowsiness. Avoid driving immediately after.'),
(4, 2, 'Fluticasone Propionate Nasal Spray', '50 mcg/spray', '2 sprays per nostril once daily', 14, 'Blow nose gently before administering.');

-- ---------------------------------------------------------------------
-- 9. AUDIT LOGS
-- ---------------------------------------------------------------------
INSERT INTO audit_logs (log_id, user_id, action, entity_name, entity_id, details, ip_address) VALUES
(1, 1, 'SYSTEM_INIT', 'SYSTEM', 'BOOTSTRAP', 'Database initialized and schema verified.', '127.0.0.1'),
(2, 1, 'USER_SEED', 'USER', 'ADMIN_1', 'Seeded administrator account admin@medicare.org.', '127.0.0.1'),
(3, 5, 'AUTH_LOGIN', 'USER', '5', 'Patient John Doe authenticated successfully.', '192.168.1.104'),
(4, 5, 'APPOINTMENT_CREATE', 'APPOINTMENT', 'APT-20261101-001', 'Booked appointment with Dr. Marcus Smith.', '192.168.1.104'),
(5, 2, 'RECORD_CREATE', 'MEDICAL_RECORD', '1', 'Dr. Marcus Smith recorded clinical diagnosis for John Doe.', '10.0.2.15');

-- ---------------------------------------------------------------------
-- 10. LEGACY PATIENT STAGING (FOR SALVAGING DEMO)
-- ---------------------------------------------------------------------
INSERT INTO legacy_patient_staging (staging_id, raw_mrn, raw_full_name, raw_dob, raw_phone, raw_address, import_status, validation_notes) VALUES
(1, 'LEG-8812', 'Arthur Pendelton', '14/02/1965', '555-0100', '19 High St, Oldtown', 'STAGED', 'Pending schema transformation to ISO-8601 and E.164 phone.'),
(2, 'LEG-8813', 'Clara Oswald', '1989-11-23', '555-0144', '77 Coal Hill Rd', 'STAGED', 'Ready for automatic account generation.'),
(3, 'LEG-8814', 'INCOMPLETE_RECORD', 'UNKNOWN', 'NO_PHONE', 'N/A', 'FAILED', 'Missing DOB and non-parseable telephone format.');
