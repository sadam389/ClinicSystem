package com.clinic.servlet;

import com.clinic.exception.AppException;
import com.clinic.model.Department;
import com.clinic.model.Doctor;
import com.clinic.service.DoctorService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ManageDoctorServlet", urlPatterns = {"/admin/manage-doctors"})
public class ManageDoctorServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final DoctorService doctorService = new DoctorService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Doctor> doctors = doctorService.getAllDoctors();
            List<Department> departments = doctorService.getAllDepartments();

            req.setAttribute("doctors", doctors);
            req.setAttribute("departments", departments);
            req.getRequestDispatcher("/admin/manage-doctors.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error loading doctor registry: " + e.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        int departmentId = Integer.parseInt(req.getParameter("departmentId"));
        String licenseNumber = req.getParameter("licenseNumber");
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String phone = req.getParameter("phone");
        String qualification = req.getParameter("qualification");
        int experienceYears = Integer.parseInt(req.getParameter("experienceYears"));
        String roomNumber = req.getParameter("roomNumber");

        try {
            doctorService.addDoctor(email, password, departmentId, licenseNumber, firstName, lastName, phone, qualification, experienceYears, roomNumber);
            resp.sendRedirect(req.getContextPath() + "/admin/manage-doctors?msg=Doctor credentialed successfully.");
        } catch (AppException e) {
            req.setAttribute("errorMessage", e.getMessage());
            doGet(req, resp);
        }
    }
}
