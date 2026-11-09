<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.clinic.model.User" %>
<%
    User currentUser = (User) session.getAttribute("currentUser");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Medicare Clinic | Appointment & Health Records</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
    <header class="navbar">
        <div class="nav-container">
            <a href="<%= request.getContextPath() %>/" class="brand">
                <span class="brand-icon">+</span>
                <span>Medicare Clinical Portal</span>
            </a>
            <ul class="nav-links">
                <% if (currentUser == null) { %>
                    <li><a href="<%= request.getContextPath() %>/login.jsp">Login</a></li>
                    <li><a href="<%= request.getContextPath() %>/register.jsp" class="btn btn-primary btn-sm" style="color:white;">Register Patient</a></li>
                <% } else { %>
                    <li><span class="user-badge"><%= currentUser.getEmail() %> (<%= currentUser.getRole() %>)</span></li>
                    <li><a href="<%= request.getContextPath() %>/<%= currentUser.getRole().name().toLowerCase() %>/dashboard" class="btn btn-primary btn-sm" style="color:white;">Go to Dashboard</a></li>
                    <li><a href="<%= request.getContextPath() %>/logout" class="btn btn-secondary btn-sm">Logout</a></li>
                <% } %>
            </ul>
        </div>
    </header>

    <main class="container">
        <div style="text-align: center; padding: 3rem 1rem;">
            <span class="badge badge-confirmed" style="font-size: 0.85rem; margin-bottom: 1rem;">Software Construction & Development Capstone</span>
            <h1 style="font-size: 2.75rem; margin-bottom: 1rem; color: var(--secondary);">
                Modern Clinical Care & Digital Patient Health Records
            </h1>
            <p style="font-size: 1.15rem; color: var(--text-muted); max-width: 720px; margin: 0 auto 2rem auto;">
                A clean, secure, multi-tier Java Servlet & JSP platform eliminating paper registers with conflict-free scheduling, electronic health records, automated prescriptions, and audit tracking.
            </p>
            <div style="display: flex; gap: 1rem; justify-content: center; flex-wrap: wrap;">
                <a href="<%= request.getContextPath() %>/login.jsp" class="btn btn-primary" style="padding: 0.75rem 2rem; font-size: 1rem;">Portal Sign In &rarr;</a>
                <a href="<%= request.getContextPath() %>/register.jsp" class="btn btn-secondary" style="padding: 0.75rem 2rem; font-size: 1rem;">New Patient Registration</a>
            </div>
        </div>

        <div class="stats-grid" style="margin-top: 2rem;">
            <div class="card" style="text-align: center;">
                <div style="font-size: 2.25rem; margin-bottom: 0.5rem;">📅</div>
                <h3>Smart Scheduling</h3>
                <p style="color: var(--text-muted); font-size: 0.9rem; margin-top: 0.5rem;">
                    Eliminates double bookings with real-time slot conflict checking and doctor availability calendars.
                </p>
            </div>
            <div class="card" style="text-align: center;">
                <div style="font-size: 2.25rem; margin-bottom: 0.5rem;">📋</div>
                <h3>Electronic Health Records</h3>
                <p style="color: var(--text-muted); font-size: 0.9rem; margin-top: 0.5rem;">
                    Full chronological patient consultation history, vital signs (BP, pulse, temp), and diagnosis notes.
                </p>
            </div>
            <div class="card" style="text-align: center;">
                <div style="font-size: 2.25rem; margin-bottom: 0.5rem;">💊</div>
                <h3>Digital Prescriptions</h3>
                <p style="color: var(--text-muted); font-size: 0.9rem; margin-top: 0.5rem;">
                    Structured multi-item dosage schedules, duration calculations, and printable clinical advice.
                </p>
            </div>
            <div class="card" style="text-align: center;">
                <div style="font-size: 2.25rem; margin-bottom: 0.5rem;">🔄</div>
                <h3>Legacy Data Salvaging</h3>
                <p style="color: var(--text-muted); font-size: 0.9rem; margin-top: 0.5rem;">
                    Refactored staging pipeline converting old delimited text files into standardized database records.
                </p>
            </div>
        </div>

        <div class="card" style="background: #f1f5f9; border-left: 4px solid var(--primary);">
            <h3>Default Test Accounts (from sample_data.sql):</h3>
            <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(280px, 1fr)); gap: 1rem; margin-top: 1rem; font-size: 0.9rem;">
                <div>
                    <strong>Administrator:</strong><br>
                    Email: <code>admin@medicare.org</code><br>
                    Password: <code>AdminPass123!</code>
                </div>
                <div>
                    <strong>Doctor (Cardiology):</strong><br>
                    Email: <code>dr.smith@medicare.org</code><br>
                    Password: <code>DoctorPass123!</code>
                </div>
                <div>
                    <strong>Patient:</strong><br>
                    Email: <code>john.doe@gmail.com</code><br>
                    Password: <code>PatientPass123!</code>
                </div>
            </div>
        </div>
    </main>

    <footer>
        <div class="container">
            <p>&copy; 2026 Medicare Clinic System. Software Construction and Development Semester Project.</p>
        </div>
    </footer>
</body>
</html>
