package com.clinic.servlet;

import com.clinic.exception.AuthenticationException;
import com.clinic.exception.DatabaseException;
import com.clinic.model.Role;
import com.clinic.model.User;
import com.clinic.service.AuthService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Handles user authentication and session bootstrap.
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login", "/auth/login"})
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("currentUser") != null) {
            User user = (User) session.getAttribute("currentUser");
            redirectToDashboard(user, req, resp);
            return;
        }
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try {
            User user = authService.authenticate(email, password);

            // Establish authenticated session
            HttpSession session = req.getSession(true);
            session.setAttribute("currentUser", user);
            session.setAttribute("userEmail", user.getEmail());
            session.setAttribute("userRole", user.getRole().name());

            redirectToDashboard(user, req, resp);
        } catch (AuthenticationException e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.setAttribute("inputEmail", email);
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        } catch (DatabaseException e) {
            req.setAttribute("errorMessage", "System database error. Please check server logs.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }

    private void redirectToDashboard(User user, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String ctx = req.getContextPath();
        if (user.getRole() == Role.ADMIN) {
            resp.sendRedirect(ctx + "/admin/dashboard");
        } else if (user.getRole() == Role.DOCTOR) {
            resp.sendRedirect(ctx + "/doctor/dashboard");
        } else {
            resp.sendRedirect(ctx + "/patient/dashboard");
        }
    }
}
