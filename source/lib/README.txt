================================================================================
LIB FOLDER INSTRUCTIONS (DEPENDENCY JARS)
================================================================================

For manual compilation without Maven or Gradle, place the following standard
JAR files in this directory:

1. mysql-connector-j-8.x.x.jar (or mysql-connector-java-8.0.x.jar)
   Download: https://dev.mysql.com/downloads/connector/j/
   Purpose: JDBC driver enabling Java communication with MySQL 8.0+.

2. servlet-api.jar (Java Servlet Specification 4.0 or 5.0)
   Location: Pre-packaged inside your Tomcat installation at:
   %CATALINA_HOME%\lib\servlet-api.jar

3. jsp-api.jar (JavaServer Pages 2.3)
   Location: Pre-packaged inside your Tomcat installation at:
   %CATALINA_HOME%\lib\jsp-api.jar

4. junit-4.13.2.jar and hamcrest-core-1.3.jar (Optional, for running tests)
   Download: https://repo1.maven.org/maven2/junit/junit/4.13.2/

NOTE:
If CATALINA_HOME is defined in your system environment variables, the provided
build.bat and build.sh scripts will automatically load servlet-api.jar and
jsp-api.jar directly from your Tomcat installation!
================================================================================
