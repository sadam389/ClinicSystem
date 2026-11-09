package com.clinic.servlet;

import com.clinic.exception.AppException;
import com.clinic.model.Appointment;
import com.clinic.model.Doctor;
import com.clinic.model.Prescription;
import com.clinic.model.PrescriptionItem;
import com.clinic.model.User;
import com.clinic.service.AppointmentService;
import com.clinic.service.DoctorService;
import com.clinic.service.PrescriptionService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "PrescriptionServlet", urlPatterns = {"/doctor/prescriptions", "/doctor/create-prescription"})
public class PrescriptionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final DoctorService doctorService = new DoctorService();
    private final PrescriptionService prescriptionService = new PrescriptionService();
    private final AppointmentService appointmentService = new AppointmentService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (User) session.getAttribute("currentUser");

        try {
            Doctor doctor = doctorService.getDoctorByUserId(currentUser.getUserId());
            List<Prescription> prescriptions = prescriptionService.getPrescriptionsForDoctor(doctor.getDoctorId());
            List<Appointment> pendingAppointments = appointmentService.getAppointmentsForDoctor(doctor.getDoctorId());

            req.setAttribute("doctor", doctor);
            req.setAttribute("prescriptions", prescriptions);
            req.setAttribute("pendingAppointments", pendingAppointments);
            req.getRequestDispatcher("/doctor/prescriptions.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error loading prescriptions: " + e.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (User) session.getAttribute("currentUser");

        try {
            Doctor doctor = doctorService.getDoctorByUserId(currentUser.getUserId());
            int appointmentId = Integer.parseInt(req.getParameter("appointmentId"));
            int patientId = Integer.parseInt(req.getParameter("patientId"));
            String diagnosis = req.getParameter("diagnosis");
            String symptoms = req.getParameter("symptoms");
            String bp = req.getParameter("bloodPressure");
            String pulseStr = req.getParameter("pulseBpm");
            Integer pulse = (pulseStr != null && !pulseStr.isEmpty()) ? Integer.parseInt(pulseStr) : null;
            String tempStr = req.getParameter("temperature");
            BigDecimal temp = (tempStr != null && !tempStr.isEmpty()) ? new BigDecimal(tempStr) : null;
            String weightStr = req.getParameter("weight");
            BigDecimal weight = (weightStr != null && !weightStr.isEmpty()) ? new BigDecimal(weightStr) : null;
            String notes = req.getParameter("clinicalNotes");
            String advice = req.getParameter("advice");
            String followUpDate = req.getParameter("followUpDate");

            // Extract medicines array
            String[] medNames = req.getParameterValues("medicineName");
            String[] dosages = req.getParameterValues("dosage");
            String[] frequencies = req.getParameterValues("frequency");
            String[] durations = req.getParameterValues("duration");
            String[] instructions = req.getParameterValues("instructions");

            List<PrescriptionItem> items = new ArrayList<>();
            if (medNames != null) {
                for (int i = 0; i < medNames.length; i++) {
                    if (medNames[i] != null && !medNames[i].trim().isEmpty()) {
                        int dur = 7;
                        try {
                            if (durations != null && durations[i] != null) dur = Integer.parseInt(durations[i]);
                        } catch (Exception ignored) {}
                        items.add(new PrescriptionItem(
                                medNames[i].trim(),
                                dosages != null ? dosages[i] : "1 tab",
                                frequencies != null ? frequencies[i] : "1-0-1",
                                dur,
                                instructions != null ? instructions[i] : "After food"
                        ));
                    }
                }
            }

            prescriptionService.createPrescriptionWithRecord(
                    appointmentId, doctor.getDoctorId(), patientId,
                    diagnosis, symptoms, bp, pulse, temp, weight, notes,
                    advice, followUpDate, items
            );

            resp.sendRedirect(req.getContextPath() + "/doctor/prescriptions?msg=Prescription recorded and finalized!");
        } catch (AppException e) {
            req.setAttribute("errorMessage", e.getMessage());
            doGet(req, resp);
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error submitting prescription: " + e.getMessage());
            doGet(req, resp);
        }
    }
}
