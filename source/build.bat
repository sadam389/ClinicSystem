@echo off
REM ====================================================================
REM MANUAL COMPILATION & WAR PACKAGING SCRIPT (WINDOWS)
REM Project: Clinic Appointment and Patient Records System
REM Course: Software Construction and Development (SCD)
REM No Maven/Gradle required - Pure javac & jar build pipeline
REM ====================================================================

echo [1/5] Initializing Build Environment...
set SRC_DIR=src\main\java
set RES_DIR=src\main\resources
set WEB_DIR=src\main\webapp
set BIN_DIR=bin
set DIST_DIR=dist
set LIB_DIR=lib

REM Check JDK presence
where javac >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo [ERROR] JDK 'javac' was not found in your PATH.
    echo Please install JDK 17+ and set JAVA_HOME.
    exit /b 1
)

echo [2/5] Cleaning output directories...
if exist "%BIN_DIR%" rmdir /s /q "%BIN_DIR%"
if exist "%DIST_DIR%" rmdir /s /q "%DIST_DIR%"
mkdir "%BIN_DIR%"
mkdir "%DIST_DIR%"
mkdir "%DIST_DIR%\WEB-INF\classes"

echo [3/5] Resolving dependencies from %LIB_DIR% and Tomcat...
set CLASSPATH=.;%LIB_DIR%\*
if defined CATALINA_HOME (
    set CLASSPATH=%CLASSPATH%;%CATALINA_HOME%\lib\*
    echo Found CATALINA_HOME at %CATALINA_HOME%
)

echo [4/5] Compiling Java source files...
REM Generate list of all Java files
dir /s /b "%SRC_DIR%\*.java" > sources.txt
javac -encoding UTF-8 -cp "%CLASSPATH%" -d "%DIST_DIR%\WEB-INF\classes" @sources.txt
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Compilation failed. Inspect javac error logs above.
    del sources.txt
    exit /b 1
)
del sources.txt

echo [5/5] Assembling Web Archive (WAR)...
REM Copy webapp resources
xcopy /E /I /Y "%WEB_DIR%\*" "%DIST_DIR%\"
REM Copy classpath resources (e.g., db.properties)
xcopy /E /I /Y "%RES_DIR%\*" "%DIST_DIR%\WEB-INF\classes\"
REM Copy JDBC and library jars into WEB-INF/lib
if exist "%LIB_DIR%" (
    if not exist "%DIST_DIR%\WEB-INF\lib" mkdir "%DIST_DIR%\WEB-INF\lib"
    xcopy /Y "%LIB_DIR%\*.jar" "%DIST_DIR%\WEB-INF\lib\" >nul 2>&1
)

cd "%DIST_DIR%"
jar -cvf ..\dist\ClinicSystem.war * >nul
cd ..

echo ====================================================================
echo BUILD SUCCESSFUL!
echo Output Artifact: dist\ClinicSystem.war
echo Ready for deployment to Apache Tomcat webapps/ directory.
echo ====================================================================
