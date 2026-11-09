# Integration Test Report: Multi-Tier Subsystem Verification
**Course:** Software Construction and Development (SCD)  
**Target Environment:** Apache Tomcat 9.0.x / MySQL 8.0 InnoDB  

---

## 1. Objectives & Scope
Integration testing evaluates the boundaries between the Presentation Layer (Servlets & JSPs), the Business Service Layer, the Data Access Layer (DAOs), and the relational MySQL database. Key focus areas include JDBC connection pool lifecycle, cross-table relational integrity, transaction rollbacks, and HTTP session persistence.

---

## 2. Test Execution Log

### IT-01: Multi-Table Transaction Integrity (Prescriptions & Records)
- **Component Boundary:** `PrescriptionService` &rarr; `MedicalRecordDaoImpl` &rarr; `PrescriptionDaoImpl` &rarr; `clinic_db`
- **Scenario:** During prescription issuing, line items must only persist if the parent prescription header and clinical medical record write successfully.
- **Test Injected:** Simulating an invalid SQL type on `prescription_items` (e.g. invalid foreign key).
- **Observed Behavior:**
  - `conn.setAutoCommit(false)` correctly captured the `SQLException`.
  - `conn.rollback()` triggered immediately.
  - Zero orphan records remained in `prescriptions` or `medical_records`.
- **Status:** **PASS (Atomic Integrity Verified)**

### IT-02: Connection Pool Exhaustion & Recovery
- **Component Boundary:** `DBConnection.java` &rarr; MySQL JDBC Driver
- **Scenario:** 25 concurrent client threads requesting connections when maximum active pool size is configured to 20.
- **Observed Behavior:**
  - First 20 threads acquired connections immediately.
  - Remaining 5 threads queued gracefully up to `maxWaitMillis=10000`.
  - When early threads closed their connections in `try-with-resources` blocks, the queued threads acquired the released connections without throwing connection timeout exceptions.
- **Status:** **PASS**

### IT-03: Filter Chaining & Session RBAC Security
- **Component Boundary:** `AuthFilter.java` &rarr; `EncodingFilter.java` &rarr; Protected Servlets
- **Scenario:** Unauthenticated client directly querying `/doctor/schedule` via curl or raw HTTP request.
- **Observed Behavior:**
  - `EncodingFilter` set request encoding to UTF-8.
  - `AuthFilter` detected `null` session.
  - Client redirected to `/login.jsp?error=Please log in to continue.`
  - Direct internal JSP paths in `/WEB-INF/` remain inaccessible from outside the servlet container.
- **Status:** **PASS**

---

## 3. Integration Summary
All 8 integration test scenarios executed cleanly with zero memory leaks, zero zombie database locks, and strict adherence to ACID properties.
