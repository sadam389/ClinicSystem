#!/usr/bin/env bash
# ====================================================================
# MANUAL COMPILATION & WAR PACKAGING SCRIPT (LINUX / MACOS)
# Project: Clinic Appointment and Patient Records System
# Course: Software Construction and Development (SCD)
# Pure javac & jar build pipeline
# ====================================================================

set -e

SRC_DIR="src/main/java"
RES_DIR="src/main/resources"
WEB_DIR="src/main/webapp"
BIN_DIR="bin"
DIST_DIR="dist"
LIB_DIR="lib"

echo "=== [1/5] Checking Build Environment ==="
if ! command -v javac &> /dev/null; then
    echo "[ERROR] javac not found. Please install JDK 17+ and export JAVA_HOME."
    exit 1
fi

echo "=== [2/5] Cleaning and preparing target directories ==="
rm -rf "${BIN_DIR}" "${DIST_DIR}"
mkdir -p "${BIN_DIR}"
mkdir -p "${DIST_DIR}/WEB-INF/classes"
mkdir -p "${DIST_DIR}/WEB-INF/lib"

echo "=== [3/5] Resolving Classpath ==="
CLASSPATH=".:${LIB_DIR}/*"
if [ -n "${CATALINA_HOME}" ]; then
    CLASSPATH="${CLASSPATH}:${CATALINA_HOME}/lib/*"
    echo "Identified CATALINA_HOME at ${CATALINA_HOME}"
fi

echo "=== [4/5] Compiling Java Sources ==="
find "${SRC_DIR}" -name "*.java" > sources.txt
javac -encoding UTF-8 -cp "${CLASSPATH}" -d "${DIST_DIR}/WEB-INF/classes" @sources.txt
rm sources.txt

echo "=== [5/5] Packaging ClinicSystem.war ==="
cp -r "${WEB_DIR}/"* "${DIST_DIR}/"
cp -r "${RES_DIR}/"* "${DIST_DIR}/WEB-INF/classes/"
if [ -d "${LIB_DIR}" ]; then
    cp "${LIB_DIR}"/*.jar "${DIST_DIR}/WEB-INF/lib/" 2>/dev/null || true
fi

cd "${DIST_DIR}"
jar -cvf ../dist/ClinicSystem.war * > /dev/null
cd ..

echo "=========================================================="
echo " BUILD SUCCESSFUL!"
echo " Artifact generated: dist/ClinicSystem.war"
echo " Deploy by copying to: \$CATALINA_HOME/webapps/"
echo "=========================================================="
