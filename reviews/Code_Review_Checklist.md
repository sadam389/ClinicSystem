# Formal Code Review Checklist (Fagan Inspection Standard)
**Course:** Software Construction and Development (SCD)  
**Language:** Java SE 17+ (Servlets & JSP)  

---

## 1. Syntax, Naming & Coding Standards
- [x] **Naming Conventions:** Do class names follow UpperCamelCase (`AppointmentService`), method names follow lowerCamelCase (`bookAppointment`), and constants use UPPER_SNAKE_CASE?
- [x] **Magic Numbers:** Are all status codes, durations, and role strings encapsulated in standard enums (`Role`, `AppointmentStatus`)?
- [x] **File Headers:** Are package statements, licensing, and JavaDoc class summaries present on all Java files?

## 2. Resource Management & Memory Safety
- [x] **Try-with-Resources:** Are all JDBC `Connection`, `PreparedStatement`, and `ResultSet` objects wrapped in automated resource blocks to prevent connection pool leaks?
- [x] **Null Safety:** Are potential null values checked defensively before dereferencing?
- [x] **Stream Closing:** Are file readers in `LegacyPatientImporter` cleanly closed via `try-with-resources`?

## 3. SQL & Database Interactions
- [x] **Parameterized Queries:** Are 100% of dynamic SQL queries constructed using `PreparedStatement` with `?` placeholders? Zero string concatenation permitted.
- [x] **Transaction Handling:** Is `conn.rollback()` invoked inside `catch (SQLException e)` blocks when multi-statement transactions fail?
- [x] **AutoCommit Reset:** Is `conn.setAutoCommit(true)` restored in the `finally` block before returning the connection to the pool?

## 4. JSP & Presentation Hygiene
- [x] **Minimal Scriptlet Logic:** Do JSP views restrict scriptlets to display rendering loops and session checks, delegating all validation and business decisions to servlets?
- [x] **Output Escaping:** Are user-provided strings sanitized before rendering to eliminate stored XSS attacks?
- [x] **CSS Centralization:** Is styling managed exclusively via `style.css` without scattered inline style overrides?

## 5. Reviewer Sign-Off
- **Lead Reviewer:** Member 2 (Quality Assurance Lead)
- **Reviewed Code:** `com.clinic.*` (All Java Models, DAOs, Services, Servlets, Filters, Utilities)
- **Status:** **VERIFIED - READY FOR SYSTEM MERGE**
