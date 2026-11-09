package com.clinic.servlet;

import com.clinic.model.Appointment;
import com.clinic.model.Doctor;
import com.clinic.model.User;
import com.clinic.service.AppointmentService;
import com.clinic.service.DoctorService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "DoctorDashboardServlet", urlPatterns = {"/doctor/dashboard"})
public class DoctorDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final DoctorService doctorService = new DoctorService();
    private final AppointmentService appointmentService = new AppointmentService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (User) session.getAttribute("currentUser");

        try {
            Doctor doctor = doctorService.getDoctorByUserId(currentUser.getUserId());
            if (doctor == null) {
                resp.sendRedirect(req.getContextPath() + "/error.jsp?msg=Doctor profile not linked.");
                return;
            }

            List<Appointment> appointments = appointmentService.getAppointmentsForDoctor(doctor.getDoctorId());

            req.setAttribute("doctor", doctor);
            req.setAttribute("appointments", appointments);
            req.getRequestDispatcher("/doctor/dashboard.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error loading doctor dashboard: " + e.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }
}
