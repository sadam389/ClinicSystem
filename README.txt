================================================================================
CLINIC APPOINTMENT AND PATIENT RECORDS SYSTEM
Course: Software Construction and Development (SCD) - Semester Capstone Project
Academic Term: Fall 2026 / Spring 2027
================================================================================

PROJECT OVERVIEW:
-----------------
The Clinic Appointment and Patient Records System is an enterprise-grade web application
designed to replace error-prone paper registers in outpatient clinical environments.
It provides role-based access for Patients, Doctors, and Clinic Administrators with
automated scheduling, conflict-free appointment booking, electronic health records (EHR),
prescription generation, legacy data salvaging, and clinical auditing.

PROJECT TEAM MEMBERS:
---------------------
1. Member 1: Lead Architect & Backend Engineer (Process, DB Design, Servlets & DAOs)
2. Member 2: Quality Assurance & Evolution Engineer (Refactoring, Testing, PSP/TSP, Reviews)
3. Member 3: UI/UX & Deployment Engineer (JSP Webapp, SCM & Git Workflow, Deployment & Scripts)

TECHNOLOGY STACK:
-----------------
- Programming Language: Java SE 17+ (Object-Oriented, strict SOLID & design patterns)
- Web Specification: Java Servlet 4.0 & JavaServer Pages (JSP) 2.3
- Database Engine: MySQL 8.0+ Community Edition (InnoDB, UTF8mb4)
- Database Connectivity: JDBC (Java Database Connectivity) with HikariCP/Connection Pool
- Application Server: Apache Tomcat 9.0.x / 10.1.x
- Build System: Pure manual compilation scripts (build.bat / build.sh) — No Maven/Gradle required
- Testing Framework: JUnit 4.13 / 5.x for unit, integration, and fault tolerance suites
- Version Control: Git with GitFlow branching model (main, develop, feature, release)

DEFAULT DEMO LOGIN CREDENTIALS:
--------------------------------
All sample accounts are pre-seeded in database/sample_data.sql:

1. System Administrator:
   - Email:    admin@medicare.org
   - Password: AdminPass123!
   - Role:     ADMIN
   - Access:   Doctor credentialing, department metrics, audit logs, system reports

2. Medical Doctors:
   - Doctor 1 (Cardiology):
     * Email:    dr.smith@medicare.org
     * Password: DoctorPass123!
     * Role:     DOCTOR
     * Schedule: Mon - Fri (09:00 - 14:00)
   - Doctor 2 (Pediatrics):
     * Email:    dr.chen@medicare.org
     * Password: DoctorPass123!
     * Role:     DOCTOR
     * Schedule: Mon, Wed, Fri (11:00 - 17:00)
   - Doctor 3 (General Medicine):
     * Email:    dr.patel@medicare.org
     * Password: DoctorPass123!
     * Role:     DOCTOR
     * Schedule: Tue, Thu, Sat (08:30 - 13:30)

3. Registered Patients:
   - Patient 1:
     * Email:    john.doe@gmail.com
     * Password: PatientPass123!
     * Role:     PATIENT
     * Record:   Hypertension, regular consultations, 3 appointments on record
   - Patient 2:
     * Email:    sarah.jenkins@outlook.com
     * Password: PatientPass123!
     * Role:     PATIENT
     * Record:   Seasonal allergic rhinitis, active medication history

================================================================================
QUICK START GUIDE (STEP-BY-STEP):
================================================================================

PREREQUISITES:
--------------
1. JDK 17 or higher installed (`java -version` and `javac -version`)
2. Apache Tomcat 9.0 or 10.1 installed and `CATALINA_HOME` set
3. MySQL Server 8.0+ running on port 3306

STEP 1: DATABASE SETUP
----------------------
Open your MySQL terminal or MySQL Workbench and execute:
   mysql -u root -p < database/schema.sql
   mysql -u root -p < database/sample_data.sql

Verify database connection settings in:
   source/src/main/resources/db.properties
   (Adjust `db.url`, `db.user`, and `db.password` if your local MySQL settings differ)

STEP 2: COMPILATION & PACKAGING
--------------------------------
On Windows:
   cd source
   build.bat

On Linux / macOS:
   cd source
   chmod +x build.sh
   ./build.sh

The build script will:
- Clean and create output directories (`bin/`, `dist/`)
- Compile all Java source files in correct dependency order
- Copy webapp resources, WEB-INF, JSP files, CSS, JS, and db.properties
- Package everything into `dist/ClinicSystem.war`

STEP 3: DEPLOYMENT TO TOMCAT
----------------------------
1. Copy `source/dist/ClinicSystem.war` into `%CATALINA_HOME%/webapps/`
2. Ensure `mysql-connector-j-8.x.jar` is present in `%CATALINA_HOME%/lib/` or `WEB-INF/lib/`
3. Start Apache Tomcat:
   Windows: `%CATALINA_HOME%\bin\startup.bat`
   Linux:   `$CATALINA_HOME/bin/startup.sh`
4. Open your browser and navigate to:
   http://localhost:8080/ClinicSystem/

PROJECT DIRECTORY HIGHLIGHTS:
----------------------------
- report/Report_Source.md : Comprehensive 45+ page formal academic report
- report/Viva_QA.md       : Complete 40+ question-answer defense guide with code citations
- diagrams/               : 9 Draw.io XML architectural and design diagrams
- tests/                  : Test plans, test cases, and fault-tolerance injection reports
- reviews/                : Formal personal (PSP) and peer review checklists and inspection logs
- deployment/             : Backup, restore scripts, and zero-downtime deployment guides
================================================================================
