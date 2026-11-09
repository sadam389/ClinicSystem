package com.clinic.servlet;

import com.clinic.model.Appointment;
import com.clinic.model.Patient;
import com.clinic.model.Prescription;
import com.clinic.model.User;
import com.clinic.service.AppointmentService;
import com.clinic.service.PatientService;
import com.clinic.service.PrescriptionService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "PatientDashboardServlet", urlPatterns = {"/patient/dashboard"})
public class PatientDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final PatientService patientService = new PatientService();
    private final AppointmentService appointmentService = new AppointmentService();
    private final PrescriptionService prescriptionService = new PrescriptionService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (User) session.getAttribute("currentUser");

        try {
            Patient patient = patientService.getPatientByUserId(currentUser.getUserId());
            if (patient == null) {
                resp.sendRedirect(req.getContextPath() + "/error.jsp?msg=Patient profile not linked.");
                return;
            }

            List<Appointment> appointments = appointmentService.getAppointmentsForPatient(patient.getPatientId());
            List<Prescription> prescriptions = prescriptionService.getPrescriptionsForPatient(patient.getPatientId());

            req.setAttribute("patient", patient);
            req.setAttribute("appointments", appointments);
            req.setAttribute("prescriptions", prescriptions);

            req.getRequestDispatcher("/patient/dashboard.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error loading patient dashboard: " + e.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }
}
