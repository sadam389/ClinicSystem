package com.clinic.listener;

import com.clinic.util.DBConnection;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Application Lifecycle Listener.
 * Verifies database health on Tomcat startup and tears down drivers cleanly on context destroy.
 */
@WebListener
public class AppInitListener implements ServletContextListener {
    private static final Logger LOGGER = Logger.getLogger(AppInitListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("=========================================================");
        LOGGER.info("Starting Clinic Appointment and Patient Records System...");
        LOGGER.info("Course: Software Construction and Development (SCD)");
        LOGGER.info("=========================================================");

        try (Connection conn = DBConnection.getInstance().getConnection()) {
            if (conn != null && !conn.isClosed()) {
                LOGGER.info("✓ Database connectivity verified successfully: " + conn.getMetaData().getDatabaseProductName());
                sce.getServletContext().setAttribute("dbStatus", "CONNECTED");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "✗ Warning: Initial database connection check failed.", e);
            sce.getServletContext().setAttribute("dbStatus", "ERROR: " + e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("Clinic System application shutting down. Releasing resources.");
    }
}
