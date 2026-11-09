# Peer Review Record 1: Architecture & Data Access Layer Inspection
**Course:** Software Construction and Development (SCD)  
**Inspection Type:** Formal Fagan Code Inspection  
**Date:** 2026-10-24  
**Location:** Capstone Software Engineering Lab (Room 304)  
**Artifact Inspected:** `com.clinic.dao.*` and `com.clinic.util.DBConnection`  

---

## 1. Inspection Team Roles
- **Moderator:** Member 2 (QA Lead)
- **Author:** Member 1 (Lead Architect)
- **Reader:** Member 3 (UI / Systems Engineer)
- **Recorder:** Member 2

---

## 2. Pre-Meeting Preparation Metrics
- Member 1 Preparation Time: 45 minutes
- Member 2 Preparation Time: 60 minutes
- Member 3 Preparation Time: 40 minutes
- Lines of Code Inspected: 620 LOC

---

## 3. Discovered Defects & Action Items

| Item # | Severity | File & Location | Defect Description | Assigned Fix | Verified By |
|:---|:---|:---|:---|:---|:---|
| **REV1-01** | High | `PrescriptionDaoImpl.java` (line 122) | AutoCommit was set to `false`, but in the event of an exception during item batch insertion, `conn.rollback()` was missing. | Wrapped in `try-catch` with explicit `conn.rollback()` in `finally/catch`. | Member 2 |
| **REV1-02** | Medium | `DBConnection.java` (line 38) | `db.properties` loading path used raw file path instead of classloader resource stream, causing `FileNotFoundException` in packaged WARs. | Changed to `getClass().getClassLoader().getResourceAsStream("db.properties")`. | Member 3 |
| **REV1-03** | Low | `UserDaoImpl.java` (line 89) | Missing index hint on query searching by user email. | Verified `idx_user_email` is explicitly declared in `schema.sql`. | Member 1 |

---

## 4. Inspection Outcome & Sign-Off
- **Verdict:** **RE-INSPECTION NOT REQUIRED** (Modifications verified asynchronously by Moderator).
- **Moderator Sign-Off:** Member 2
- **Author Sign-Off:** Member 1
