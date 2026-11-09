#!/usr/bin/env bash
# ====================================================================
# DATABASE RESTORE SCRIPT (LINUX / MACOS)
# Usage: ./restore.sh [path_to_dump.sql]
# ====================================================================

set -e

DB_USER="root"
DUMP_FILE="$1"

if [ -z "${DUMP_FILE}" ]; then
    echo "Usage: ./restore.sh [path_to_sql_dump.sql]"
    exit 1
fi

if [ ! -f "${DUMP_FILE}" ]; then
    echo "[ERROR] Dump file not found: ${DUMP_FILE}"
    exit 1
fi

echo "Restoring database from ${DUMP_FILE}..."
mysql -u "${DB_USER}" -p < "${DUMP_FILE}"

echo "✓ Database restore successfully completed."
