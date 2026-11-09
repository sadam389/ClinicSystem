package com.clinic.servlet;

import com.clinic.model.Doctor;
import com.clinic.model.DoctorSchedule;
import com.clinic.model.User;
import com.clinic.service.DoctorService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "DoctorScheduleServlet", urlPatterns = {"/doctor/schedule"})
public class DoctorScheduleServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final DoctorService doctorService = new DoctorService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (User) session.getAttribute("currentUser");

        try {
            Doctor doctor = doctorService.getDoctorByUserId(currentUser.getUserId());
            List<DoctorSchedule> schedules = doctorService.getDoctorSchedules(doctor.getDoctorId());

            req.setAttribute("doctor", doctor);
            req.setAttribute("schedules", schedules);
            req.getRequestDispatcher("/doctor/schedule.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error loading schedule: " + e.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }
}
