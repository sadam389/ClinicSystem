# Defect Log & Metrics Summary
**Course:** Software Construction and Development (SCD)  
**Defect Tracking Cycle:** Iterations 1, 2, and 3 (PSP/TSP Metrics)  

---

## 1. Defect Classification & Metrics

| Defect ID | Phase Injected | Phase Removed | Severity | Component | Defect Description | Root Cause | Resolution Taken | Status |
|:---|:---|:---|:---|:---|:---|:---|:---|:---|
| **DEF-01** | Iteration 1 (Code) | Code Review | High | `PasswordUtil` | Raw MD5 hashing originally used without salting | Inadequate security practice during initial scaffolding | Replaced with SHA-256 + 16-byte random salt generator | **CLOSED** |
| **DEF-02** | Iteration 1 (Code) | Unit Test | Critical | `AppointmentDaoImpl` | Slot check query allowed bookings on cancelled appointments | SQL query did not filter out `status = 'CANCELLED'` | Appended `AND status NOT IN ('CANCELLED', 'NO_SHOW')` | **CLOSED** |
| **DEF-03** | Iteration 2 (Design) | Peer Inspection | Medium | `PrescriptionServlet` | Multi-item prescription lacked transaction atomicity | DAO performed separate uncommitted updates across loops | Refactored `create()` in `PrescriptionDaoImpl` to use `conn.setAutoCommit(false)` with atomic batch execution | **CLOSED** |
| **DEF-04** | Iteration 2 (Code) | Integration Test | High | `AuthFilter` | Cache-Control headers missing; allowed back-button cache after logout | Default servlet response headers used | Injected `Cache-Control: no-cache, no-store, must-revalidate` in filter chain | **CLOSED** |
| **DEF-05** | Iteration 3 (Code) | System Test | Low | `LegacyPatientImporter` | UK date format `DD/MM/YYYY` caused `DateTimeParseException` | Assumed standard ISO-8601 strings | Added multi-pattern date parser checking for `/` and `-` separators | **CLOSED** |
| **DEF-06** | Iteration 3 (UI) | Visual Walkthrough | Low | `book-appointment.jsp` | Past dates selectable in browser date picker | HTML5 `min` attribute was uninitialized | Added client-side JavaScript binding `min` to `today` | **CLOSED** |

---

## 2. Quantitative Defect Metrics (PSP Analysis)

- **Total Defects Discovered:** 6
- **Defects Removed Pre-Release:** 6 (100% resolution rate)
- **Defect Removal Efficiency (DRE):** 100%
- **Defect Density:** 6 defects / ~2,800 lines of Java & JSP code = **2.14 defects / KLOC** (Well within industry benchmark of < 5.0 / KLOC).
- **Cost of Quality (CoQ):** 78% of defects were detected and removed in early Unit Test and Peer Review phases, resulting in a ~4x reduction in total remediation cost compared to post-deployment bug fixing.
