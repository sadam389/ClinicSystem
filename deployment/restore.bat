@echo off
REM ====================================================================
REM DATABASE RESTORE SCRIPT (WINDOWS)
REM Usage: restore.bat [path_to_dump.sql]
REM ====================================================================

set DB_USER=root
set DUMP_FILE=%1

if "%DUMP_FILE%"=="" (
    echo Usage: restore.bat path_to_sql_dump.sql
    exit /b 1
)

if not exist "%DUMP_FILE%" (
    echo [ERROR] Dump file does not exist: %DUMP_FILE%
    exit /b 1
)

echo Restoring database from: %DUMP_FILE%...
mysql -u %DB_USER% -p < "%DUMP_FILE%"

if %ERRORLEVEL% equ 0 (
    echo Restoration completed successfully!
) else (
    echo [ERROR] Restore operation failed.
)
