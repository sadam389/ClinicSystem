#!/usr/bin/env bash
# ====================================================================
# AUTOMATED DATABASE BACKUP SCRIPT (LINUX / MACOS)
# Database: clinic_db
# ====================================================================

set -e

DB_NAME="clinic_db"
DB_USER="root"
BACKUP_DIR="backups"
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
BACKUP_FILE="${BACKUP_DIR}/${DB_NAME}_backup_${TIMESTAMP}.sql"

mkdir -p "${BACKUP_DIR}"

echo "Starting automated database backup for: ${DB_NAME}..."
mysqldump -u "${DB_USER}" -p --single-transaction --quick --databases "${DB_NAME}" > "${BACKUP_FILE}"

echo "✓ Database backup successfully generated at: ${BACKUP_FILE}"
ls -lh "${BACKUP_FILE}"
