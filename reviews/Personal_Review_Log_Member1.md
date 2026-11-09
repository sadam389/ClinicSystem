# Personal Software Process (PSP) Log — Member 1 (Lead Architect & Backend)
**Name:** Member 1  
**Role:** Database Schema Design, Model Architecture, JDBC DAOs, Core Servlets  

---

## 1. Time & Effort Tracking (PSP Level 2.1)

| Date | Phase | Task Description | Planned Time (min) | Actual Time (min) | Interruption (min) | Defect Count | Notes |
|:---|:---|:---|:---|:---|:---|:---|:---|
| 2026-10-12 | Design | Relational schema design (`clinic_db`) | 180 | 165 | 15 | 1 | Foreign key cascade adjustments |
| 2026-10-14 | Code | Model and DAO Interface definitions | 120 | 135 | 0 | 0 | Pure POJO models with Serialization |
| 2026-10-17 | Code | `DBConnection` Singleton & connection pool | 90 | 110 | 20 | 1 | Thread safety double-check locking |
| 2026-10-21 | Code | `UserDaoImpl` & `DoctorDaoImpl` JDBC queries | 210 | 240 | 10 | 2 | Handled nullable foreign keys |
| 2026-10-25 | Code | `AppointmentDaoImpl` slot conflict logic | 150 | 180 | 0 | 1 | Filtered out CANCELLED appointments |
| 2026-11-01 | Review | Self-review against Code Review Checklist | 60 | 55 | 0 | 1 | Added try-with-resources on Statements |

---

## 2. Personal Defect Injection & Removal Analysis
- **Total Personal Defects Injected:** 6
- **Defects Detected During Personal Review:** 5 (83.3% pre-peer removal efficiency)
- **Primary Root Cause:** Minor edge conditions in complex SQL queries (e.g. including cancelled appointments in slot overlap queries).
- **Process Improvement Action:** Established a pre-coding SQL query verification routine in MySQL Workbench before embedding strings into Java source code.
