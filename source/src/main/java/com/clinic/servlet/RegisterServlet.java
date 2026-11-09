package com.clinic.servlet;

import com.clinic.exception.AppException;
import com.clinic.model.Patient;
import com.clinic.service.AuthService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Patient self-registration endpoint.
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/register", "/auth/register"})
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String dob = req.getParameter("dob");
        String gender = req.getParameter("gender");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String bloodGroup = req.getParameter("bloodGroup");
        String emergencyContact = req.getParameter("emergencyContact");
        String emergencyPhone = req.getParameter("emergencyPhone");
        String allergies = req.getParameter("allergies");

        try {
            Patient p = authService.registerPatient(
                    email, password, firstName, lastName, dob, gender,
                    phone, address, bloodGroup, emergencyContact, emergencyPhone, allergies
            );

            resp.sendRedirect(req.getContextPath() + "/login.jsp?msg=Account created successfully! Your MRN is " + p.getPatientMrn() + ". Please login.");
        } catch (AppException e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.setAttribute("email", email);
            req.setAttribute("firstName", firstName);
            req.setAttribute("lastName", lastName);
            req.setAttribute("dob", dob);
            req.setAttribute("gender", gender);
            req.setAttribute("phone", phone);
            req.setAttribute("address", address);
            req.setAttribute("bloodGroup", bloodGroup);
            req.setAttribute("emergencyContact", emergencyContact);
            req.setAttribute("emergencyPhone", emergencyPhone);
            req.setAttribute("allergies", allergies);
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
        }
    }
}
