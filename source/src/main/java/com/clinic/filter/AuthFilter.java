package com.clinic.filter;

import com.clinic.model.Role;
import com.clinic.model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Security Interceptor & Role-Based Access Control (RBAC) Filter.
 * Restricts access to /patient/*, /doctor/*, and /admin/* endpoints.
 */
@WebFilter(filterName = "AuthFilter", urlPatterns = {"/patient/*", "/doctor/*", "/admin/*"})
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;

        String requestURI = httpRequest.getRequestURI();

        // 1. Check if user is logged in
        if (currentUser == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp?error=Please log in to continue.");
            return;
        }

        // 2. Enforce Role boundaries
        Role userRole = currentUser.getRole();

        if (requestURI.contains("/patient/") && userRole != Role.PATIENT && userRole != Role.ADMIN) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/error.jsp?code=403&msg=Access denied: Patient role required.");
            return;
        }

        if (requestURI.contains("/doctor/") && userRole != Role.DOCTOR && userRole != Role.ADMIN) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/error.jsp?code=403&msg=Access denied: Doctor credentials required.");
            return;
        }

        if (requestURI.contains("/admin/") && userRole != Role.ADMIN) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/error.jsp?code=403&msg=Access denied: Administrator privileges required.");
            return;
        }

        // Prevent browser caching of protected views
        httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        httpResponse.setHeader("Pragma", "no-cache");
        httpResponse.setDateHeader("Expires", 0);

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
