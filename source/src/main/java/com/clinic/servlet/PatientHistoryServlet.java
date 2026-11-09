package com.clinic.servlet;

import com.clinic.model.MedicalRecord;
import com.clinic.model.Patient;
import com.clinic.model.Prescription;
import com.clinic.model.User;
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

@WebServlet(name = "PatientHistoryServlet", urlPatterns = {"/patient/history"})
public class PatientHistoryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final PatientService patientService = new PatientService();
    private final PrescriptionService prescriptionService = new PrescriptionService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (User) session.getAttribute("currentUser");

        try {
            Patient patient = patientService.getPatientByUserId(currentUser.getUserId());
            List<MedicalRecord> history = patientService.getPatientHistory(patient.getPatientId());
            List<Prescription> prescriptions = prescriptionService.getPrescriptionsForPatient(patient.getPatientId());

            req.setAttribute("patient", patient);
            req.setAttribute("history", history);
            req.setAttribute("prescriptions", prescriptions);

            req.getRequestDispatcher("/patient/history.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error loading patient history: " + e.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }
}
