# Source Code Structure & Manual Compilation Guide

This repository contains the complete Java Servlets & JSP source code for the **Clinic Appointment and Patient Records System**. In compliance with course specifications for *Software Construction and Development*, no automated build tools (Maven/Gradle) are used. The build process relies on pure `javac` and `jar` packaging scripts.

## Package Architecture (`com.clinic`)

- `model`: Domain entities (`User`, `Patient`, `Doctor`, `Appointment`, `MedicalRecord`, `Prescription`, `DoctorSchedule`, `Department`, `Role`, `AppointmentStatus`).
- `dao`: Data Access Object interfaces and JDBC implementations with transaction safety (`UserDaoImpl`, `AppointmentDaoImpl`, etc.).
- `service`: Core business logic, slot validation, duplicate detection, and encryption orchestration.
- `servlet`: MVC Controllers mapping HTTP requests to JSP views and handling form submissions.
- `filter`: Security and authorization filters (`AuthFilter` for RBAC, `EncodingFilter` for UTF-8).
- `listener`: `AppInitListener` testing database ping on server bootstrap.
- `exception`: Checked exception taxonomy (`AppException`, `ValidationException`, `DatabaseException`).
- `util`: Singleton `DBConnection`, `PasswordUtil` (SHA-256 + salt), and defensive `ValidationUtil`.
- `legacy`: `LegacyPatientImporter` demonstrating code salvaging, refactoring, and format migration.

## Compilation Instructions

1. Ensure MySQL is running and `clinic_db` is created (`mysql -u root -p < ../database/schema.sql`).
2. Add `mysql-connector-j-8.x.jar`, `servlet-api.jar`, and `jsp-api.jar` into the `lib/` directory or set `CATALINA_HOME`.
3. Run `build.bat` (Windows) or `./build.sh` (Linux/macOS).
4. Copy `dist/ClinicSystem.war` into Tomcat's `webapps/` folder.
