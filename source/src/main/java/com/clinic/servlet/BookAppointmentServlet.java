package com.clinic.servlet;

import com.clinic.exception.AppException;
import com.clinic.model.Department;
import com.clinic.model.Doctor;
import com.clinic.model.Patient;
import com.clinic.model.User;
import com.clinic.service.AppointmentService;
import com.clinic.service.DoctorService;
import com.clinic.service.PatientService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "BookAppointmentServlet", urlPatterns = {"/patient/book-appointment"})
public class BookAppointmentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final AppointmentService appointmentService = new AppointmentService();
    private final DoctorService doctorService = new DoctorService();
    private final PatientService patientService = new PatientService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Doctor> doctors = doctorService.getAllDoctors();
            List<Department> departments = doctorService.getAllDepartments();

            req.setAttribute("doctors", doctors);
            req.setAttribute("departments", departments);
            req.getRequestDispatcher("/patient/book-appointment.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Unable to load booking directory: " + e.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (User) session.getAttribute("currentUser");

        try {
            Patient patient = patientService.getPatientByUserId(currentUser.getUserId());
            int doctorId = Integer.parseInt(req.getParameter("doctorId"));
            String appointmentDate = req.getParameter("appointmentDate");
            String appointmentTime = req.getParameter("appointmentTime");
            String reason = req.getParameter("reason");

            appointmentService.bookAppointment(patient.getPatientId(), doctorId, appointmentDate, appointmentTime, reason);

            resp.sendRedirect(req.getContextPath() + "/patient/dashboard?msg=Appointment booked successfully!");
        } catch (AppException e) {
            req.setAttribute("errorMessage", e.getMessage());
            doGet(req, resp);
        } catch (NumberFormatException e) {
            req.setAttribute("errorMessage", "Please select a valid doctor.");
            doGet(req, resp);
        }
    }
}
