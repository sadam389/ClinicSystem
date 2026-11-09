package com.clinic.servlet;

import com.clinic.model.Appointment;
import com.clinic.service.AppointmentService;
import com.clinic.service.ReportService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ReportServlet", urlPatterns = {"/admin/reports"})
public class ReportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final ReportService reportService = new ReportService();
    private final AppointmentService appointmentService = new AppointmentService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Map<String, Object> metrics = reportService.generateExecutiveDashboardReport();
            List<Appointment> allAppts = appointmentService.getAllAppointments();

            req.setAttribute("metrics", metrics);
            req.setAttribute("appointments", allAppts);
            req.getRequestDispatcher("/admin/reports.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error generating clinical reports: " + e.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }
}
