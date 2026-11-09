# Formal Design Review Checklist (IEEE 1028 Inspection Standard)
**Course:** Software Construction and Development (SCD)  
**System:** Clinic Appointment and Patient Records System  

---

## 1. Architectural Integrity & Cohesion
- [x] **Layer Separation:** Is there a clean separation between Presentation (JSP/Servlet), Business Service, and Data Access (DAO)?
- [x] **No Leaky Abstractions:** Do Servlets refrain from writing raw SQL or directly invoking JDBC methods?
- [x] **High Cohesion:** Does each class have a single, clearly articulated responsibility (Single Responsibility Principle)?
- [x] **Low Coupling:** Are DAO implementations accessed via interfaces (`UserDao`, `AppointmentDao`), allowing seamless mocking in unit tests?

## 2. Security & Access Control Design
- [x] **Defense in Depth:** Are protected resources guarded by an intercepting HTTP filter (`AuthFilter`) rather than relying on UI link hiding?
- [x] **Password Protection:** Are credentials hashed using modern cryptographically strong algorithms (SHA-256 + individual 16-byte random salts)?
- [x] **Input Neutralization:** Is all user input sanitized against SQL injection (parameterized queries) and Cross-Site Scripting (HTML entity escaping)?

## 3. Concurrency & Transaction Safety
- [x] **Slot Conflict Prevention:** Does the database enforce unique constraints or does the service layer explicitly check for slot availability before insert?
- [x] **Atomic Multi-Entity Operations:** Are compound operations (such as recording a clinical consultation, generating a prescription, and adding prescription line items) wrapped in an explicit database transaction (`conn.setAutoCommit(false)`)?

## 4. Robustness & Exception Design
- [x] **Structured Exception Hierarchy:** Are exceptions partitioned cleanly into user-correctable errors (`ValidationException`), system faults (`DatabaseException`), and security faults (`AuthenticationException`)?
- [x] **Information Disclosure:** Are internal database schemas and raw stack traces suppressed on production error pages (`error.jsp`)?

## 5. Review Disposition
- **Outcome:** **APPROVED WITH MINOR EDITS**
- **Action Items Recorded:** Ensured transaction rollback was explicitly handled in `PrescriptionDaoImpl.java` during line item insertion failures.
