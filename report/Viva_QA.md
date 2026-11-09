# Comprehensive Viva Voce Defense Master Guide (40+ Technical Q&As)
**Course:** Software Construction and Development (SCD)  
**System:** Clinic Appointment and Patient Records System  
**Note:** Every single answer cites the concrete source file path in the project where the concept is implemented.

---

## Section A: Process, Methodology & Teamwork (PSP / TSP)

### Q1: Which software process lifecycle model did you select for this project, and what technical factors justified your choice?
**Answer:** We implemented an **Iterative and Incremental Process Model** based on the Unified Process (UP). Unlike linear Waterfall, outpatient clinical workflows possess high domain complexity—such as slot conflict checking and legacy paper record migration—which require empirical feedback. We executed three distinct iterations: Iteration 1 built the core DB schema and authentication; Iteration 2 delivered scheduling and atomic prescriptions; Iteration 3 implemented legacy code salvaging, SCM release branches, and chaos fault-testing.  
*Citation:* See `report/Report_Source.md` (Chapter 1) and `deployment/Deployment_Guide.md`.

### Q2: How were Personal Software Process (PSP) principles applied across individual team members?
**Answer:** Each team member maintained an individual PSP Level 2.1 Time & Defect Logging Sheet. We tracked planned versus actual phase durations, recorded defect injection phases, defect removal phases, defect types, and remediation effort. This enabled us to calculate personal Defect Removal Efficiency (DRE) and Cost of Quality (CoQ).  
*Citation:* See `reviews/Personal_Review_Log_Member1.md`, `reviews/Personal_Review_Log_Member2.md`, and `reviews/Personal_Review_Log_Member3.md`.

### Q3: What levels of process definition were observed in the project?
**Answer:** We adhered to the standard CMMI process levels: Level 1 (Initial ad-hoc prototyping), Level 2 (Managed—introducing version control branching and defect logs), and Level 3 (Defined—standardizing on IEEE 829 for test planning, IEEE 1028 for formal design reviews, and Fagan inspections for code reviews).  
*Citation:* See `report/Report_Source.md` (Chapter 1, Section 1.3).

### Q4: How did the Team Software Process (TSP) govern task allocation and integration?
**Answer:** TSP governed weekly launch meetings, peer inspection assignments, and work-breakdown structures (WBS). Member 1 acted as Lead Architect (Data and Backend); Member 2 as Quality Assurance and Evolution Lead (Testing and Refactoring); Member 3 as UI/UX and Deployment Engineer (JSP, CSS, and GitFlow release engineering).  
*Citation:* See `reviews/Peer_Review_Record_1.md` and `reviews/Peer_Review_Record_2.md`.

---

## Section B: Requirements Engineering & Traceability

### Q5: What requirements elicitation techniques were used to capture clinical workflows?
**Answer:** We employed semi-structured interviews with outpatient clinic receptionists and physicians, paper form artifact analysis (inspecting physical appointment notebooks and paper prescription slips), and user persona journey mapping.  
*Citation:* See `report/Report_Source.md` (Chapter 2, Section 2.1).

### Q6: How does the system guarantee slot conflict prevention at the requirement level?
**Answer:** Requirement FR-03 stipulates that no two appointments can exist for the same doctor at the same date and overlapping start time, excluding cancelled or no-show visits. This is enforced at the database level via a composite unique key and at the service level via pre-insert verification.  
*Citation:* See `database/schema.sql` (line 104, `UNIQUE KEY uq_doctor_slot`) and `source/src/main/java/com/clinic/service/AppointmentService.java` (lines 53-57).

### Q7: Explain the Traceability Matrix maintained in the project.
**Answer:** The Requirements Traceability Matrix (RTM) maps each Functional Requirement (FR) to its corresponding Use Case, domain Model class, Data Access Object, and JUnit Test Case ID. For instance, FR-03 (Appointment Booking) traces to Use Case UC3, Model `Appointment.java`, `AppointmentDao.java`, and Test Case `TC-APT-01`.  
*Citation:* See `report/Report_Source.md` (Chapter 2, Section 2.4) and `tests/Test_Cases.md`.

---

## Section C: Object-Oriented Design & Design Principles

### Q8: How is the Single Responsibility Principle (SRP) applied in your architecture?
**Answer:** Each class possesses exactly one reason to change. `ValidationUtil.java` is solely responsible for input format hygiene; `DBConnection.java` handles connection acquisition; `UserDaoImpl.java` executes SQL for user records; and `AuthService.java` orchestrates hashing and business rules. Servlets never execute SQL, and DAOs never handle HTTP sessions.  
*Citation:* See `source/src/main/java/com/clinic/util/ValidationUtil.java` and `source/src/main/java/com/clinic/dao/UserDaoImpl.java`.

### Q9: How did you implement the Open/Closed Principle (OCP) and Dependency Inversion Principle (DIP)?
**Answer:** We coded to interfaces rather than concrete classes. Services depend on `UserDao`, `AppointmentDao`, and `DoctorDao` interfaces rather than direct implementations. During unit testing, mock DAOs (`MockAppointmentDao`) are injected without altering the core `AppointmentService` source code.  
*Citation:* See `source/src/main/java/com/clinic/dao/AppointmentDao.java` and `source/src/test/java/com/clinic/AppointmentServiceTest.java` (lines 55-75).

### Q10: How does the Singleton Design Pattern operate in your database layer?
**Answer:** `DBConnection.java` utilizes a thread-safe Double-Checked Locking Singleton with a `volatile` instance variable to ensure only one connection pool manager is created throughout the Tomcat JVM lifecycle.  
*Citation:* See `source/src/main/java/com/clinic/util/DBConnection.java` (lines 20-33).

### Q11: Explain how the MVC (Model-View-Controller) architectural pattern is structured in this Java EE webapp.
**Answer:** 
- **Model:** Java Beans in `com.clinic.model.*` (`Appointment.java`, `Patient.java`, etc.).
- **View:** JavaServer Pages in `webapp/patient/`, `webapp/doctor/`, `webapp/admin/`.
- **Controller:** HTTP Servlets in `com.clinic.servlet.*` (`BookAppointmentServlet.java`, `LoginServlet.java`) that process client inputs, invoke services, and forward request attributes to JSPs.  
*Citation:* See `source/src/main/java/com/clinic/servlet/BookAppointmentServlet.java` and `source/src/main/webapp/patient/book-appointment.jsp`.

---

## Section D: Architectural Evaluation

### Q12: Compare your Layered MVC architecture with a 2-Tier Basic Servlet/JSP architecture.
**Answer:** In a Basic 2-Tier setup, servlets or JSP scriptlets directly execute JDBC queries and render HTML. While simpler for tiny prototypes, it exhibits high coupling, untestable business logic, and severe security liabilities. Our 3-Tier Layered Architecture isolates DAOs, Services, Controllers, and Views, allowing independent unit testing, seamless database driver replacements, and strict separation of concerns.  
*Citation:* See `report/Report_Source.md` (Chapter 4).

### Q13: How does the system enforce security at the architectural boundary?
**Answer:** Security is enforced via Java Servlet Filters (`AuthFilter.java`). Before any protected request reaches a JSP or servlet under `/patient/*`, `/doctor/*`, or `/admin/*`, `AuthFilter` intercepts the request, validates the active `HttpSession`, verifies the user's `Role` enum, and sets anti-caching HTTP response headers.  
*Citation:* See `source/src/main/java/com/clinic/filter/AuthFilter.java`.

---

## Section E: Robustness, Input Validation & Fault Tolerance

### Q14: How are custom exceptions organized in the system?
**Answer:** We engineered a checked exception taxonomy rooted at `AppException.java`. Specialized domain subclasses include `ValidationException.java` (carrying user-correctable validation error messages), `DatabaseException.java` (wrapping fatal JDBC/SQL errors), and `AuthenticationException.java` (handling credential failures).  
*Citation:* See `source/src/main/java/com/clinic/exception/AppException.java` and `ValidationException.java`.

### Q15: How do you prevent SQL Injection vulnerabilities?
**Answer:** 100% of SQL queries across all DAO implementations utilize parameterized `PreparedStatement` with bind variables (`?`). User inputs are never concatenated directly into query strings.  
*Citation:* See `source/src/main/java/com/clinic/dao/AppointmentDaoImpl.java` (lines 110-128).

### Q16: How is Cross-Site Scripting (XSS) mitigated?
**Answer:** `ValidationUtil.sanitizeString()` neutralizes all dangerous HTML characters (`<`, `>`, `"`, `'`) into their corresponding HTML entities (`&lt;`, `&gt;`, `&quot;`, `&#x27;`). In addition, client-side input validations prevent malformed submissions.  
*Citation:* See `source/src/main/java/com/clinic/util/ValidationUtil.java` (lines 58-65).

### Q17: How is transaction atomicity guaranteed when creating prescriptions with line items?
**Answer:** When issuing a prescription, multiple database rows must be created in `prescriptions` and `prescription_items`. In `PrescriptionDaoImpl.java`, we invoke `conn.setAutoCommit(false)`. If any line item batch insert fails, `conn.rollback()` is executed in the `catch` block, guaranteeing no orphan records exist.  
*Citation:* See `source/src/main/java/com/clinic/dao/PrescriptionDaoImpl.java` (lines 148-185).

### Q18: What occurs if the MySQL database terminates unexpectedly during runtime?
**Answer:** `DBConnection.java` catches the `SQLException`, wraps it in a friendly `DatabaseException`, logs the full stack trace to the server log via `java.util.logging.Logger`, and forwards the client to `error.jsp` with a generic, safe notice, suppressing sensitive database connection credentials or schema internals.  
*Citation:* See `source/src/main/webapp/error.jsp` and `tests/Fault_Test_Report.md` (Fault 1).

---

## Section F: Code Quality, Refactoring & Legacy Code Salvaging

### Q19: What specific code smells did you identify in early iterations, and how were they refactored?
**Answer:** We identified two major smells:
1. **God Method & Duplicated Code:** Initial servlets performed raw parameter parsing, DB queries, and error formatting in single 150-line methods. We refactored by extracting business logic into `AppointmentService` and validation into `ValidationUtil`.
2. **Insecure Storage Smell:** Early user authentication used unsalted hashes. We refactored `PasswordUtil` to generate random 16-byte cryptographic salts with SHA-256.  
*Citation:* See `report/Report_Source.md` (Chapter 6, Section 6.1) and `source/src/main/java/com/clinic/util/PasswordUtil.java`.

### Q20: What is "Code Salvaging" and how was it demonstrated in this project?
**Answer:** Code salvaging involves extracting working, valuable business logic from abandoned or legacy software and integrating it into modern clean architecture without inheriting technical debt. We salvaged the clinic's old flat-file patient parser into `LegacyPatientImporter.java`, insulating it with modern regex normalization, ISO date converters, and database staging buffers.  
*Citation:* See `source/src/main/java/com/clinic/legacy/LegacyPatientImporter.java`.

### Q21: How does the legacy importer handle backward compatibility for dates?
**Answer:** Legacy outpatient cards recorded dates as `DD/MM/YYYY`, whereas modern MySQL requires ISO-8601 (`YYYY-MM-DD`). In `LegacyPatientImporter.parseLegacyDate()`, the system dynamically inspects delimiters; if `/` is present, it parses using `DateTimeFormatter.ofPattern("d/M/yyyy")` and converts to `java.sql.Date`.  
*Citation:* See `source/src/main/java/com/clinic/legacy/LegacyPatientImporter.java` (lines 154-165).

### Q22: What happens if a legacy record has corrupted or missing attributes?
**Answer:** Rather than aborting the entire batch import, the importer catches the per-line exception, logs the defect, and writes the raw row to `legacy_patient_staging` with status `FAILED` and an explanation note, allowing the remaining valid patients to import cleanly.  
*Citation:* See `source/src/main/java/com/clinic/legacy/LegacyPatientImporter.java` (lines 167-180).

---

## Section G: Software Evolution & Lehman's Laws

### Q23: How was Lehman's Law of "Continuing Change" observed in this project?
**Answer:** As outpatient clinical operations evolved from paper files to digital portals, user requirements continuously expanded—requiring dynamic e-prescribing, multi-slot search, and admin oversight. An application that does not adapt to clinical workflows becomes obsolete.  
*Citation:* See `report/Report_Source.md` (Chapter 6, Section 6.4).

### Q24: How was Lehman's Law of "Increasing Complexity" observed and managed?
**Answer:** As features were added (e.g. prescription line items linked to appointments and diagnoses), cyclomatic complexity naturally increased. We actively combated entropy by refactoring common logic into utility classes (`ValidationUtil`, `PasswordUtil`) and enforcing strict DAO interfaces.  
*Citation:* See `report/Report_Source.md` (Chapter 6, Section 6.4).

### Q25: Explain a concrete Change Request (CR) implemented during the project and its impact analysis.
**Answer:** **CR-04: Addition of Emergency Contact & Known Allergies to Patient Profile.**  
- **Impact Analysis:**
  - DB: Altered `patients` table to add `emergency_contact`, `emergency_phone`, and `allergies` columns.
  - Model: Updated `Patient.java` with getters/setters and serialization.
  - DAO: Updated `PatientDaoImpl` SQL insert and select mappings.
  - UI: Updated `register.jsp` and `dashboard.jsp`.
  - Tests: Updated `AuthServiceTest` to pass allergy parameters.  
*Citation:* See `database/schema.sql` (lines 75-80) and `source/src/main/java/com/clinic/model/Patient.java`.

---

## Section H: Formal Reviews & Inspections

### Q26: What is the difference between a Walkthrough and a Formal Inspection under IEEE 1028?
**Answer:** A walkthrough is author-led, informal, and focused on knowledge transfer and high-level design review. A Formal Inspection (Fagan standard) is moderator-led, follows strict entry/exit checklists, requires pre-meeting preparation, tracks defect density per KLOC, and produces formal sign-off records.  
*Citation:* See `reviews/Peer_Review_Record_1.md` and `reviews/Code_Review_Checklist.md`.

### Q27: What was the most critical defect discovered during Peer Review Session 1?
**Answer:** Defect `REV1-01` in `PrescriptionDaoImpl.java`. AutoCommit was set to false, but an unhandled SQL exception during line item insertion omitted `conn.rollback()`, risking orphaned locks in MySQL InnoDB. It was fixed immediately by wrapping the entire transaction in a safe try-catch-finally block.  
*Citation:* See `reviews/Peer_Review_Record_1.md` (Item REV1-01).

### Q28: What was Defect REV2-01 identified in Peer Review Session 2?
**Answer:** Browser back-button caching allowed unauthenticated users to view cached patient dashboard pages after logging out. We resolved this by configuring `AuthFilter.java` to inject `Cache-Control: no-cache, no-store, must-revalidate`.  
*Citation:* See `reviews/Peer_Review_Record_2.md` (Item REV2-01).

---

## Section I: Testing, Verification & Metrics

### Q29: What is the difference between Unit, Integration, and System testing in your project?
**Answer:** 
- **Unit Testing:** Tests isolated classes (`ValidationUtilTest`, `AppointmentServiceTest`) using mock DAOs without touching a network database.
- **Integration Testing:** Tests interactions between servlets, services, DAOs, connection pools, and MySQL database constraints.
- **System Testing:** Tests full end-to-end user journeys (patient self-registration through appointment completion) across client browsers.  
*Citation:* See `tests/Unit_Test_Report.md`, `tests/Integration_Test_Report.md`, and `tests/System_Test_Report.md`.

### Q30: What was your measured Defect Density, and how does it compare to industry benchmarks?
**Answer:** We recorded 6 defects across ~2,800 lines of code, resulting in a Defect Density of **2.14 defects / KLOC**. Industry averages for mature software projects range between 2.0 and 5.0 defects / KLOC, confirming high construction quality.  
*Citation:* See `tests/Defect_Summary.md` (Section 2).

### Q31: What is Defect Removal Efficiency (DRE) and what was your team's score?
**Answer:** DRE is the percentage of defects detected and resolved prior to production release: $\text{DRE} = \frac{\text{Pre-Release Defects}}{\text{Pre-Release Defects} + \text{Post-Release Defects}} \times 100\%$. With 6 defects detected and resolved during personal logs, peer reviews, and automated tests, and 0 defects in release `v1.0.0`, our DRE is **100%**.  
*Citation:* See `tests/Defect_Summary.md`.

---

## Section J: Configuration Management, Deployment & Evolution

### Q32: Explain your GitFlow branching strategy.
**Answer:** We used GitFlow: `main` hosts production releases (`v1.0.0`); `develop` serves as the nightly integration branch; `feature/legacy-import` was branched off `develop` to isolate salvaged legacy migration code; and `release/v1.0.0` was used for pre-release defect freezing.  
*Citation:* See `deployment/Deployment_Guide.md` (Section 3).

### Q33: Why did you not use Maven or Gradle for compiling this project?
**Answer:** The course specification for Software Construction and Development explicitly required manual compilation without build automation tools. This demonstrates foundational mastery of the Java compiler (`javac`), classpath configuration, manifest handling, and Web Archive packaging (`jar -cvf`).  
*Citation:* See `source/build.bat` and `source/build.sh`.

### Q34: How do your build scripts manage operating system differences?
**Answer:** In `build.bat`, Windows uses semicolons (`;`) for classpath separation and `%CATALINA_HOME%` environment expansion. In `build.sh`, Linux/macOS uses colons (`:`) and `$CATALINA_HOME`. Both scripts compile source files listed in a temporary `sources.txt` file to avoid command-line argument length limits.  
*Citation:* See `source/build.bat` and `source/build.sh`.

### Q35: Describe your database disaster recovery procedure.
**Answer:** We provide automated shell scripts: `backup.sh` / `backup.bat` executes `mysqldump --single-transaction --quick` to produce consistent point-in-time SQL snapshots. In the event of server corruption, `restore.sh` / `restore.bat` streams the SQL dump back into MySQL InnoDB.  
*Citation:* See `deployment/backup.sh` and `deployment/restore.sh`.

---

## Section K: Technical Architecture Deep-Dive

### Q36: How does the Java Servlet container know which servlet handles which URL?
**Answer:** Servlets use Java EE `@WebServlet` annotations (e.g. `@WebServlet(name="LoginServlet", urlPatterns={"/login", "/auth/login"})`). In addition, `web.xml` provides global configuration including session timeouts, welcome files, and custom error page mappings.  
*Citation:* See `source/src/main/java/com/clinic/servlet/LoginServlet.java` and `source/src/main/webapp/WEB-INF/web.xml`.

### Q37: How does `AppInitListener` improve application reliability?
**Answer:** It implements `ServletContextListener` and runs automatically when Apache Tomcat bootstraps. It immediately checks database connectivity using `DBConnection.getInstance().getConnection()`. If the database is down or credentials are misconfigured, it logs a severe alert and flags `dbStatus` in the `ServletContext`.  
*Citation:* See `source/src/main/java/com/clinic/listener/AppInitListener.java`.

### Q38: How does password salting protect clinic users against rainbow table attacks?
**Answer:** In `PasswordUtil.java`, every registered user receives a unique 16-byte random hexadecimal salt generated by `SecureRandom`. The hash is computed as $\text{SHA-256}(\text{password} + \text{salt})$. Even if two users share the same password, their stored hashes in MySQL are completely distinct.  
*Citation:* See `source/src/main/java/com/clinic/util/PasswordUtil.java` (lines 14-38).

### Q39: What role does the Medical Record Number (MRN) play in clinical data integrity?
**Answer:** The MRN (e.g. `MRN-2026-0001`) serves as an immutable natural identifier for patient physical and electronic files, distinct from the internal database integer surrogate primary key `patient_id`. It prevents patient identity mix-ups during family bookings or name changes.  
*Citation:* See `database/schema.sql` (line 58) and `source/src/main/java/com/clinic/model/Patient.java`.

### Q40: What future enhancements would you architect for Version 2.0.0?
**Answer:** 
1. Integration of HL7 / FHIR (Fast Healthcare Interoperability Resources) REST APIs for hospital-wide laboratory exchange.
2. Two-Factor Authentication (2FA) via Time-Based One-Time Passwords (TOTP) for physician logins.
3. Automated SMS / Email appointment reminders via asynchronous worker queues.  
*Citation:* See `report/Report_Source.md` (Chapter 9).
