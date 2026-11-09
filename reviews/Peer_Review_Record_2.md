# Peer Review Record 2: Security, Filters & Legacy Importer Inspection
**Course:** Software Construction and Development (SCD)  
**Inspection Type:** Formal Fagan Code Inspection  
**Date:** 2026-11-02  
**Location:** Capstone Software Engineering Lab (Room 304)  
**Artifact Inspected:** `com.clinic.filter.AuthFilter`, `com.clinic.legacy.LegacyPatientImporter`, `com.clinic.util.ValidationUtil`  

---

## 1. Inspection Team Roles
- **Moderator:** Member 1 (Lead Architect)
- **Author:** Member 2 (QA & Evolution Lead)
- **Reader:** Member 3 (UI / Systems Engineer)
- **Recorder:** Member 1

---

## 2. Pre-Meeting Preparation Metrics
- Member 1 Preparation Time: 50 minutes
- Member 2 Preparation Time: 30 minutes
- Member 3 Preparation Time: 45 minutes
- Lines of Code Inspected: 480 LOC

---

## 3. Discovered Defects & Action Items

| Item # | Severity | File & Location | Defect Description | Assigned Fix | Verified By |
|:---|:---|:---|:---|:---|:---|
| **REV2-01** | High | `AuthFilter.java` (line 42) | Back-button browser caching allowed viewing rendered patient dashboards after logout. | Added HTTP response headers `Cache-Control: no-cache, no-store, must-revalidate` and `Pragma: no-cache`. | Member 1 |
| **REV2-02** | High | `ValidationUtil.java` (line 64) | Sanitization only escaped `<` and `>`, leaving single quotes unescaped for attribute injection. | Added `replace("'", "&#x27;")` and `replace("\"", "&quot;")`. | Member 3 |
| **REV2-03** | Medium | `LegacyPatientImporter.java` (line 115) | Date parser threw `NullPointerException` if legacy record row contained trailing whitespace without delimiters. | Added `trim()` guard and regex pattern checking before invoking `LocalDate.parse()`. | Member 1 |

---

## 4. Inspection Outcome & Sign-Off
- **Verdict:** **ACCEPTED & MERGED INTO RELEASE/V1.0.0**
- **Moderator Sign-Off:** Member 1
- **Author Sign-Off:** Member 2
- **Reader Sign-Off:** Member 3
