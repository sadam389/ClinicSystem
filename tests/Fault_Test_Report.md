# Fault Tolerance & Robustness Test Report
**Course:** Software Construction and Development (SCD)  
**Methodology:** Chaos testing, negative input injection, and fault injection  

---

## 1. Objectives
As required in the course syllabus, software construction must not only function under optimal conditions, but must also demonstrate resilience against unexpected environment failures, malicious input payloads, network interruptions, and malformed legacy data.

---

## 2. Injected Fault Scenarios & System Response

### Fault 1: Database Daemon Sudden Termination
- **Injection:** Forcefully terminating MySQL process (`kill -9 mysqld`) during an active user HTTP request.
- **Expected Behavior:** System must catch `SQLException`, wrap inside `DatabaseException`, log severe stack trace internally, and redirect the user to `error.jsp` without leaking database schema or credentials.
- **Observed Behavior:**
  - `DBConnection` caught connection failure.
  - User presented with a friendly alert page: *"System database error. Please contact clinic administration."*
  - Zero raw SQL queries or DB passwords exposed in browser response.
- **Verdict:** **PASS (Resilient & Secure)**

### Fault 2: SQL Injection Attack Vectors
- **Injection:** Submitting `' OR '1'='1' --` and `'; DROP TABLE users; --` into login email and appointment search inputs.
- **Expected Behavior:** Application must utilize parameterized `PreparedStatement` with bind variables, treating payload as pure literal string data.
- **Observed Behavior:**
  - Login query executed: `SELECT ... WHERE email = ?` with parameter bound literally.
  - Zero SQL syntax errors occurred; authentication was rejected with invalid credentials error.
- **Verdict:** **PASS (SQL Injection Protected)**

### Fault 3: Cross-Site Scripting (XSS) Stored Payloads
- **Injection:** Registering patient with name `<script>alert('pwned')</script>` and clinical symptoms containing `<img src=x onerror=alert('xss')>`.
- **Expected Behavior:** `ValidationUtil.sanitizeString()` neutralizes all HTML entity angle brackets and quote characters.
- **Observed Behavior:**
  - String encoded to `&lt;script&gt;alert(&#x27;pwned&#x27;)&lt;/script&gt;`.
  - Browser rendered literal text without triggering JavaScript execution.
- **Verdict:** **PASS**

### Fault 4: Corrupted Legacy Data Stream
- **Injection:** Feeding garbage binary data, missing fields, and negative dates to `LegacyPatientImporter`.
- **Expected Behavior:** Exception caught per row; malformed rows inserted into `legacy_patient_staging` table with audit reason; batch completes without crashing JVM.
- **Observed Behavior:**
  - Importer isolated erroneous lines and recorded failure notes in staging table.
  - 100% of remaining valid records imported cleanly.
- **Verdict:** **PASS**
