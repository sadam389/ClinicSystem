# Personal Software Process (PSP) Log — Member 2 (QA & Evolution Lead)
**Name:** Member 2  
**Role:** Quality Assurance, Test Automation, Refactoring, Legacy Code Salvaging  

---

## 1. Time & Effort Tracking (PSP Level 2.1)

| Date | Phase | Task Description | Planned Time (min) | Actual Time (min) | Interruption (min) | Defect Count | Notes |
|:---|:---|:---|:---|:---|:---|:---|:---|
| 2026-10-18 | Test | Formulated Master Test Plan (IEEE 829) | 120 | 110 | 10 | 0 | Matrix aligned with functional reqs |
| 2026-10-22 | Code | Implemented JUnit test cases (`ValidationUtilTest`) | 90 | 85 | 0 | 1 | Discovered weak password regex |
| 2026-10-26 | Refactor | Salvaged `LegacyPatientImporter.java` | 180 | 210 | 15 | 2 | Handled UK vs ISO date parsing |
| 2026-10-29 | Test | Executed Fault-Tolerance chaos tests | 120 | 140 | 0 | 1 | Verified error.jsp exception masking |
| 2026-11-03 | Review | Coordinated formal Peer Review Session 1 | 90 | 95 | 0 | 2 | Multi-statement transaction atomicity |

---

## 2. Personal Defect Injection & Removal Analysis
- **Total Injected:** 4
- **Detected via Unit Tests & Self-Review:** 4 (100% pre-release removal)
- **Primary Root Cause:** Format variations in legacy flat-file patient streams (e.g., varying date formats between UK and US dates).
- **Process Improvement Action:** Enhanced defensive regex sanitizers and added fallback staging tables to guarantee zero data loss during salvaging.
