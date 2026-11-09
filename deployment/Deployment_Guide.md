# Production Deployment, SCM & Disaster Recovery Guide
**Project:** Clinic Appointment and Patient Records System  
**Course:** Software Construction and Development (SCD)  
**Release Version:** v1.0.0  

---

## 1. System Architecture & Topology
The application follows a classic Three-Tier Enterprise Web Topology:
- **Presentation Tier:** JSP 2.3 + CSS3 + ES6 running in modern client browsers.
- **Application Tier:** Apache Tomcat 9.0/10.1 running Java Servlets, business services, and connection pooling.
- **Data Tier:** MySQL 8.0 Community Server running InnoDB database with automated transaction logs.

---

## 2. Step-by-Step Installation & Deployment

### Step 1: Operating System & Prerequisite Setup
1. Install JDK 17+ (Eclipse Temurin or Oracle JDK) and verify:
   ```bash
   java -version
   javac -version
   ```
2. Install Apache Tomcat 9.0.x or 10.1.x and set environment variable:
   - Windows: `set CATALINA_HOME=C:\apache-tomcat-9.0.xx`
   - Linux: `export CATALINA_HOME=/opt/tomcat`
3. Install and start MySQL Server 8.0:
   - Port: `3306`

### Step 2: Database Initialization
Execute the schema creation script followed by sample demonstration data:
```bash
mysql -u root -p < database/schema.sql
mysql -u root -p < database/sample_data.sql
```
Verify database configuration in `source/src/main/resources/db.properties`.

### Step 3: Manual Compilation & WAR Packaging
Navigate to `source/` directory:
- **On Windows:**
  ```cmd
  cd source
  build.bat
  ```
- **On Linux / macOS:**
  ```bash
  cd source
  chmod +x build.sh
  ./build.sh
  ```
The build script outputs `source/dist/ClinicSystem.war`.

### Step 4: Tomcat Deployment
1. Ensure `mysql-connector-j-8.x.jar` is located in `$CATALINA_HOME/lib/`.
2. Copy `source/dist/ClinicSystem.war` to `$CATALINA_HOME/webapps/ClinicSystem.war`.
3. Launch Apache Tomcat:
   - Windows: `%CATALINA_HOME%\bin\startup.bat`
   - Linux: `$CATALINA_HOME/bin/startup.sh`
4. Access web portal:
   `http://localhost:8080/ClinicSystem/`

---

## 3. Git Workflow & SCM Configuration

The project follows the standard **GitFlow** branching strategy:
- `main`: Production-ready release branch (`v1.0.0`).
- `develop`: Main development integration branch.
- `feature/legacy-import`: Feature branch dedicated to legacy code salvaging and data migration.
- `release/v1.0.0`: Pre-release hardening, review inspections, and tag freeze.

### Exact Git Commands for Repository Setup:
```bash
# 1. Initialize local repository
git init
git add .
git commit -m "feat(scd): initial commit of complete clinic management system"

# 2. Establish develop branch
git checkout -b develop
git commit --allow-empty -m "chore(gitflow): initialize develop branch"

# 3. Create feature branch for legacy code salvaging
git checkout -b feature/legacy-import
git add source/src/main/java/com/clinic/legacy/LegacyPatientImporter.java
git commit -m "feat(legacy): refactor and salvage legacy flat-file parser"
git checkout develop
git merge --no-ff feature/legacy-import -m "merge: integrate feature/legacy-import into develop"

# 4. Prepare release branch
git checkout -b release/v1.0.0
git commit --allow-empty -m "chore(release): v1.0.0 freeze and final report compilation"

# 5. Merge into main and tag
git checkout main
git merge --no-ff release/v1.0.0 -m "release: v1.0.0 submission release"
git tag -a v1.0.0 -m "Final submission release for Software Construction and Development"

# 6. Push to remote repository (Replace with student repository URL)
# git remote add origin https://github.com/your-username/ClinicSystem.git
# git push -u origin --all
# git push origin --tags
```

---

## 4. Disaster Recovery & Backup Procedures
Automated backup and restore scripts are provided in the `deployment/` directory:
- Run `backup.bat` / `backup.sh` to generate timestamped SQL dumps (`clinic_db_backup_YYYYMMDD_HHMMSS.sql`).
- Run `restore.bat <dump_file>` / `restore.sh <dump_file>` to restore system state in event of data loss.
