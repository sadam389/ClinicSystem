<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.clinic.model.Patient, com.clinic.model.Appointment, com.clinic.model.Prescription, java.util.List" %>
<%
    Patient patient = (Patient) request.getAttribute("patient");
    List<Appointment> appointments = (List<Appointment>) request.getAttribute("appointments");
    List<Prescription> prescriptions = (List<Prescription>) request.getAttribute("prescriptions");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Patient Dashboard | <%= patient != null ? patient.getFullName() : "Medicare" %></title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
    <header class="navbar">
        <div class="nav-container">
            <a href="<%= request.getContextPath() %>/patient/dashboard" class="brand">
                <span class="brand-icon">+</span>
                <span>Medicare Portal</span>
            </a>
            <ul class="nav-links">
                <li><a href="<%= request.getContextPath() %>/patient/dashboard" class="active">Dashboard</a></li>
                <li><a href="<%= request.getContextPath() %>/patient/book-appointment">Book Visit</a></li>
                <li><a href="<%= request.getContextPath() %>/patient/history">Medical Records</a></li>
                <li><span class="user-badge"><%= patient != null ? patient.getPatientMrn() : "" %></span></li>
                <li><a href="<%= request.getContextPath() %>/logout" class="btn btn-secondary btn-sm">Logout</a></li>
            </ul>
        </div>
    </header>

    <main class="container">
        <% String msg = request.getParameter("msg");
           if (msg != null) { %>
            <div class="alert alert-success"><span>✓</span> <span><%= msg %></span></div>
        <% } %>

        <div class="card" style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 1rem;">
            <div>
                <h1>Welcome, <%= patient.getFullName() %></h1>
                <p style="color: var(--text-muted); margin-bottom: 0;">
                    Medical Record Number: <strong><%= patient.getPatientMrn() %></strong> | Blood Group: <strong><%= patient.getBloodGroup() %></strong>
                </p>
            </div>
            <a href="<%= request.getContextPath() %>/patient/book-appointment" class="btn btn-primary">+ Book New Appointment</a>
        </div>

        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-icon">📅</div>
                <div>
                    <div class="stat-val"><%= appointments != null ? appointments.size() : 0 %></div>
                    <div class="stat-lbl">Total Appointments</div>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon">💊</div>
                <div>
                    <div class="stat-val"><%= prescriptions != null ? prescriptions.size() : 0 %></div>
                    <div class="stat-lbl">Active Prescriptions</div>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon">⚠️</div>
                <div>
                    <div class="stat-val" style="font-size: 1.15rem; color: #dc2626;"><%= patient.getAllergies() != null ? patient.getAllergies() : "None" %></div>
                    <div class="stat-lbl">Allergies on File</div>
                </div>
            </div>
        </div>

        <div class="card">
            <div class="card-header">
                <h2>Your Appointments</h2>
                <a href="<%= request.getContextPath() %>/patient/book-appointment" class="btn btn-sm btn-primary">Schedule Slot</a>
            </div>
            <div class="table-wrapper">
                <table>
                    <thead>
                        <tr>
                            <th>Ref Number</th>
                            <th>Doctor & Specialty</th>
                            <th>Date</th>
                            <th>Time</th>
                            <th>Reason</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% if (appointments == null || appointments.isEmpty()) { %>
                            <tr><td colspan="6" style="text-align: center; color: var(--text-muted);">No appointments booked yet.</td></tr>
                        <% } else {
                            for (Appointment apt : appointments) { %>
                            <tr>
                                <td><code><%= apt.getAppointmentNumber() %></code></td>
                                <td>
                                    <strong><%= apt.getDoctorName() %></strong><br>
                                    <small style="color: var(--text-muted);"><%= apt.getDepartmentName() %></small>
                                </td>
                                <td><%= apt.getAppointmentDate() %></td>
                                <td><%= apt.getStartTime() %> - <%= apt.getEndTime() %></td>
                                <td><%= apt.getReasonForVisit() %></td>
                                <td>
                                    <span class="badge badge-<%= apt.getStatus().name().toLowerCase() %>">
                                        <%= apt.getStatus() %>
                                    </span>
                                </td>
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
            <p>&copy; 2026 Medicare Clinic System. Patient Portal Module.</p>
        </div>
    </footer>
</body>
</html>
