# Comprehensive Master Test Plan (IEEE 829 Standard)
**Project:** Clinic Appointment and Patient Records System  
**Course:** Software Construction and Development (SCD)  
**Academic Year:** 2026 / 2027  

---

## 1. Introduction & Objectives
The primary objective of this Test Plan is to define the testing strategy, scope, environment, and acceptance criteria for verifying that the Clinic Appointment and Patient Records System satisfies all functional and non-functional requirements. The system must eliminate appointment overlaps, guarantee patient data privacy under RBAC, preserve data integrity during prescription transactions, and provide robust fault-tolerance against network dropouts and bad inputs.

## 2. Test Items & Modules under Scope
1. **Authentication & Access Control:**
   - Registration of Patients with Medical Record Number (MRN) generation.
   - Login authentication via salted SHA-256 password hashing.
   - RBAC filter validation across `/patient/*`, `/doctor/*`, and `/admin/*`.
2. **Scheduling & Appointment Booking:**
   - Real-time double-booking prevention.
   - Slot validation across doctor availability windows.
   - Cancellation lifecycle rules and status transitions.
3. **Medical Records & E-Prescribing:**
   - Two-phase database transaction binding `medical_records` to `prescriptions` and `prescription_items`.
   - Vital signs validation (blood pressure regex, pulse, temperature ranges).
4. **Legacy Code Salvaging:**
   - Backward-compatible parsing of legacy semicolon-delimited patient records.
   - Staging table insertion for malformed records without aborting batch jobs.

## 3. Features Not Tested
- Direct hardware driver integrations for automated receipt printing.
- Third-party insurance billing clearinghouse APIs (deferred to Phase II).

## 4. Test Strategy & Levels
- **Unit Testing (JUnit):** White-box testing focusing on `ValidationUtil`, `PasswordUtil`, service layer calculation, and DAO isolation with mock databases.
- **Integration Testing:** Verification of JDBC connection pools, database constraints, multi-table transactions (`PrescriptionDaoImpl`), and HTTP Servlet requests/responses via mock servlet contexts.
- **System Testing:** End-to-end black-box walkthroughs mimicking real-world clinic workflows (patient booking -> doctor consultation -> prescription issue -> administrative metrics).
- **Fault-Tolerance & Robustness Testing:** Intentional injection of SQL injection strings, XSS scripts, duplicate primary keys, null payloads, and sudden database shutdowns.

## 5. Pass/Fail Criteria
- **100%** of Critical (Severity 1) and High (Severity 2) defects must be resolved and verified.
- **>= 95%** overall automated unit and integration test pass rate.
- Zero unhandled exceptions or stack trace leaks to client browsers (`error.jsp` catch-all enforced).

## 6. Test Deliverables
1. `Test_Cases.md` (Detailed traceability and step-by-step inputs)
2. `Unit_Test_Report.md` (Automated test execution logs)
3. `Integration_Test_Report.md` (Component interaction logs)
4. `System_Test_Report.md` (End-to-end scenario validations)
5. `Fault_Test_Report.md` (Chaos & vulnerability injection tests)
6. `Defect_Summary.md` (Defect metrics, density, and resolution tracking)
