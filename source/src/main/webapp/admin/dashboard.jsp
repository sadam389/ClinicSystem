<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.clinic.model.Doctor, com.clinic.model.Patient, com.clinic.model.Appointment, java.util.List, java.util.Map" %>
<%
    Map<String, Object> metrics = (Map<String, Object>) request.getAttribute("metrics");
    List<Appointment> recentAppointments = (List<Appointment>) request.getAttribute("recentAppointments");
    List<Doctor> doctors = (List<Doctor>) request.getAttribute("doctors");
    List<Patient> patients = (List<Patient>) request.getAttribute("patients");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Administration Center | Medicare Clinical Portal</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
    <header class="navbar">
        <div class="nav-container">
            <a href="<%= request.getContextPath() %>/admin/dashboard" class="brand">
                <span class="brand-icon">+</span>
                <span>Medicare Admin Console</span>
            </a>
            <ul class="nav-links">
                <li><a href="<%= request.getContextPath() %>/admin/dashboard" class="active">Overview</a></li>
                <li><a href="<%= request.getContextPath() %>/admin/manage-doctors">Credential Doctors</a></li>
                <li><a href="<%= request.getContextPath() %>/admin/reports">Clinical Analytics & Legacy</a></li>
                <li><a href="<%= request.getContextPath() %>/logout" class="btn btn-secondary btn-sm">Logout</a></li>
            </ul>
        </div>
    </header>

    <main class="container">
        <div class="card" style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap;">
            <div>
                <h1>Executive Operations Dashboard</h1>
                <p style="color: var(--text-muted); margin-bottom: 0;">Comprehensive clinical workflow monitoring, scheduling metrics, and credentialing</p>
            </div>
            <div style="display: flex; gap: 0.5rem;">
                <a href="<%= request.getContextPath() %>/admin/manage-doctors" class="btn btn-primary">+ Add New Doctor</a>
                <a href="<%= request.getContextPath() %>/admin/reports" class="btn btn-secondary">Run Reports</a>
            </div>
        </div>

        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-icon">🏥</div>
                <div>
                    <div class="stat-val"><%= metrics != null ? metrics.get("totalDoctors") : 0 %></div>
                    <div class="stat-lbl">Active Physicians</div>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon">👤</div>
                <div>
                    <div class="stat-val"><%= metrics != null ? metrics.get("totalPatients") : 0 %></div>
                    <div class="stat-lbl">Registered Patients</div>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon">📅</div>
                <div>
                    <div class="stat-val"><%= metrics != null ? metrics.get("totalAppointments") : 0 %></div>
                    <div class="stat-lbl">Booked Appointments</div>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon">📈</div>
                <div>
                    <div class="stat-val"><%= metrics != null ? metrics.get("completionRate") : 0 %>%</div>
                    <div class="stat-lbl">Completion Efficiency</div>
                </div>
            </div>
        </div>

        <div class="card">
            <div class="card-header">
                <h2>All Outpatient Clinic Bookings</h2>
            </div>
            <div class="table-wrapper">
                <table>
                    <thead>
                        <tr>
                            <th>Appointment #</th>
                            <th>Patient Name</th>
                            <th>Doctor & Specialty</th>
                            <th>Date & Time</th>
                            <th>Reason</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% if (recentAppointments == null || recentAppointments.isEmpty()) { %>
                            <tr><td colspan="6" style="text-align: center; color: var(--text-muted);">No appointment records found.</td></tr>
                        <% } else {
                            for (Appointment a : recentAppointments) { %>
                            <tr>
                                <td><code><%= a.getAppointmentNumber() %></code></td>
                                <td><strong><%= a.getPatientName() %></strong></td>
                                <td><%= a.getDoctorName() %> (<%= a.getDepartmentName() %>)</td>
                                <td><%= a.getAppointmentDate() %> <%= a.getStartTime() %></td>
                                <td><%= a.getReasonForVisit() %></td>
                                <td><span class="badge badge-<%= a.getStatus().name().toLowerCase() %>"><%= a.getStatus() %></span></td>
                            </tr>
                        <%  }
                        } %>
                    </tbody>
                </table>
            </div>
        </div>
    </main>

    <footer>
        <div class="container">
            <p>&copy; 2026 Medicare Clinic System. System Administration Module.</p>
        </div>
    </footer>
</body>
</html>
