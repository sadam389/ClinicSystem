# Personal Software Process (PSP) Log — Member 3 (UI/UX & Deployment)
**Name:** Member 3  
**Role:** JSP Views, Responsive CSS, Client JavaScript, SCM/GitFlow, Deployment Scripts  

---

## 1. Time & Effort Tracking (PSP Level 2.1)

| Date | Phase | Task Description | Planned Time (min) | Actual Time (min) | Interruption (min) | Defect Count | Notes |
|:---|:---|:---|:---|:---|:---|:---|:---|
| 2026-10-15 | Design | UI Wireframes & Layout CSS Architecture | 150 | 160 | 10 | 0 | Modern healthcare visual scheme |
| 2026-10-20 | Code | Created JSP templates (`index`, `login`, `register`) | 120 | 115 | 0 | 1 | Added HTML5 client-side validation |
| 2026-10-24 | Code | Implemented Doctor & Patient Portals | 180 | 195 | 20 | 1 | Dynamic JS prescription row adder |
| 2026-10-28 | Code | Manual compilation scripts (`build.bat` / `.sh`) | 90 | 105 | 0 | 1 | Resolved classpath delimiter in Windows |
| 2026-11-04 | Review | Coordinated formal Peer Review Session 2 | 60 | 60 | 0 | 0 | Cross-browser responsive checks |

---

## 2. Personal Defect Injection & Removal Analysis
- **Total Injected:** 3
- **Detected via Visual Checks & Manual Builds:** 3
- **Primary Root Cause:** Platform differences between Windows (`%CLASSPATH%`, semicolon separator) and Linux (`$CLASSPATH`, colon separator).
- **Process Improvement Action:** Maintained dual test environments (Windows Command Prompt and Ubuntu bash) to guarantee cross-platform packaging.
