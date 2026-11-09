# Formal Test Cases Specification
**Course:** Software Construction and Development (SCD)  
**System:** Clinic Appointment and Patient Records System  

| Test Case ID | Requirement ID | Module / Class Under Test | Test Description | Preconditions | Input Data | Expected Result | Actual Result | Status |
|:---|:---|:---|:---|:---|:---|:---|:---|:---|
| **TC-AUTH-01** | FR-01 | `AuthService.java` | Authenticate valid patient credentials | Registered user exists in DB | `john.doe@gmail.com`, `PatientPass123!` | Login successful, session populated with PATIENT role | Session created, redirected to `/patient/dashboard` | **PASS** |
| **TC-AUTH-02** | FR-01 | `AuthService.java` | Reject incorrect password | Registered user exists | `john.doe@gmail.com`, `WrongPass!` | Throws `AuthenticationException` ("Invalid email or password") | Error message shown on login.jsp, HTTP 200 | **PASS** |
| **TC-AUTH-03** | FR-01 | `AuthFilter.java` | Prevent patient from accessing `/admin/*` | Patient logged in | GET `/admin/dashboard` | HTTP 302 redirect to `error.jsp` with 403 Forbidden | Access denied error page rendered | **PASS** |
| **TC-REG-01** | FR-02 | `AuthService.java` | Self-register patient profile | New email address | Valid patient form data | Generates unique MRN (e.g. `MRN-2026-XXXX`), hashes password, saves to DB | User & Patient records created in DB | **PASS** |
| **TC-REG-02** | FR-02 | `ValidationUtil.java` | Reject invalid email format | None | Email: `bad-email@` | Throws `ValidationException` | Form rejected with field prompt | **PASS** |
| **TC-APT-01** | FR-03 | `AppointmentService.java` | Book valid future appointment slot | Patient logged in, doctor available | Date: tomorrow, Time: 10:00, Reason: "Checkup" | Generates reference `APT-XXXX`, slot reserved, status CONFIRMED | Slot confirmed, status badge green | **PASS** |
| **TC-APT-02** | FR-03 | `AppointmentService.java` | Block double booking on identical slot | Slot 10:00 already booked | Same Doctor, same Date, Time: 10:00 | Throws `ValidationException` ("Slot already booked") | Error banner displayed to patient | **PASS** |
| **TC-APT-03** | FR-03 | `AppointmentService.java` | Reject past date booking | None | Date: `2020-01-01` | Throws `ValidationException` ("Cannot schedule in past") | Booking prevented | **PASS** |
| **TC-RX-01** | FR-04 | `PrescriptionService.java` | Record consultation and issue Rx | Appointment CONFIRMED | Diagnosis: "Strep Throat", 2 Med Items | Atomic transaction creates MedicalRecord + Prescription + 2 Items, marks Appointment COMPLETED | DB committed atomically; appointment set to COMPLETED | **PASS** |
| **TC-RX-02** | FR-04 | `PrescriptionDaoImpl.java` | Atomic rollback on partial failure | None | Corrupted item payload | Triggers `conn.rollback()`, leaves no orphan records | Zero orphan records in database | **PASS** |
| **TC-LEG-01** | FR-05 | `LegacyPatientImporter.java` | Salvage delimited legacy string | Staged legacy file | `LEG-901;William Oswald;14/08/1975;555-1234;12 Hill St;O+` | Parses DD/MM/YYYY to ISO-8601, creates User and Patient | Record imported successfully | **PASS** |
| **TC-LEG-02** | FR-05 | `LegacyPatientImporter.java` | Fault tolerance on malformed line | Staged legacy file | `LEG-999;INVALID_NO_DOB_OR_PHONE` | Logs error, writes to `legacy_patient_staging`, continues batch | Batch completes without terminating | **PASS** |
| **TC-SEC-01** | NFR-01 | `ValidationUtil.java` | XSS HTML sanitization | None | `<script>alert(1)</script>` | Converted to `&lt;script&gt;alert(1)&lt;/script&gt;` | Sanitized without script execution | **PASS** |
