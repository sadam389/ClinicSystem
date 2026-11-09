@echo off
REM ====================================================================
REM AUTOMATED DATABASE BACKUP SCRIPT (WINDOWS)
REM Database: clinic_db
REM Generates timestamped dump in backups\ directory
REM ====================================================================

set DB_NAME=clinic_db
set DB_USER=root
set BACKUP_DIR=backups

if not exist "%BACKUP_DIR%" mkdir "%BACKUP_DIR%"

for /f "tokens=2-4 delims=/ " %%a in ('date /t') do (set mydate=%%c%%a%%b)
for /f "tokens=1-2 delims=/:" %%a in ('time /t') do (set mytime=%%a%%b)
set BACKUP_FILE=%BACKUP_DIR%\%DB_NAME%_backup_%mydate%_%mytime%.sql

echo Backing up %DB_NAME% to %BACKUP_FILE%...
mysqldump -u %DB_USER% -p --single-transaction --quick --databases %DB_NAME% > "%BACKUP_FILE%"

if %ERRORLEVEL% equ 0 (
    echo Backup completed successfully: %BACKUP_FILE%
) else (
    echo [ERROR] Backup failed. Check MySQL credentials.
)
