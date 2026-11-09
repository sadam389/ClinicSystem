<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.clinic.model.Doctor, com.clinic.model.Appointment, java.util.List" %>
<%
    Doctor doctor = (Doctor) request.getAttribute("doctor");
    List<Appointment> appointments = (List<Appointment>) request.getAttribute("appointments");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Doctor Portal | <%= doctor != null ? doctor.getFullName() : "Doctor" %></title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
    <header class="navbar">
        <div class="nav-container">
            <a href="<%= request.getContextPath() %>/doctor/dashboard" class="brand">
                <span class="brand-icon">+</span>
                <span>Medicare Clinical Portal</span>
            </a>
            <ul class="nav-links">
                <li><a href="<%= request.getContextPath() %>/doctor/dashboard" class="active">Dashboard</a></li>
                <li><a href="<%= request.getContextPath() %>/doctor/schedule">My Schedule</a></li>
                <li><a href="<%= request.getContextPath() %>/doctor/prescriptions">E-Prescribing</a></li>
                <li><span class="user-badge"><%= doctor != null ? doctor.getFullName() : "" %></span></li>
                <li><a href="<%= request.getContextPath() %>/logout" class="btn btn-secondary btn-sm">Logout</a></li>
            </ul>
        </div>
    </header>

    <main class="container">
        <div class="card" style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap;">
            <div>
                <h1><%= doctor.getFullName() %></h1>
                <p style="color: var(--text-muted); margin-bottom: 0;">
                    Department: <strong><%= doctor.getDepartmentName() %></strong> | Qualification: <strong><%= doctor.getQualification() %></strong> | Room: <strong><%= doctor.getRoomNumber() %></strong>
                </p>
            </div>
            <a href="<%= request.getContextPath() %>/doctor/prescriptions" class="btn btn-primary">+ New Consultation & Rx</a>
        </div>

        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-icon">🩺</div>
                <div>
                    <div class="stat-val"><%= appointments != null ? appointments.size() : 0 %></div>
                    <div class="stat-lbl">Assigned Appointments</div>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon">👥</div>
                <div>
                    <div class="stat-val"><%= doctor.getMaxDailyPatients() %></div>
                    <div class="stat-lbl">Max Daily Capacity</div>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon">🏥</div>
                <div>
                    <div class="stat-val"><%= doctor.getExperienceYears() %> yrs</div>
                    <div class="stat-lbl">Clinical Experience</div>
                </div>
            </div>
        </div>

        <div class="card">
            <div class="card-header">
                <h2>Patient Consultation Roster</h2>
            </div>
            <div class="table-wrapper">
                <table>
                    <thead>
                        <tr>
                            <th>Ref #</th>
                            <th>Patient Name & MRN</th>
                            <th>Date</th>
                            <th>Slot Time</th>
                            <th>Reason / Symptoms</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% if (appointments == null || appointments.isEmpty()) { %>
                            <tr><td colspan="7" style="text-align: center; color: var(--text-muted);">No patients scheduled today.</td></tr>
                        <% } else {
                            for (Appointment a : appointments) { %>
                            <tr>
                                <td><code><%= a.getAppointmentNumber() %></code></td>
                                <td>
                                    <strong><%= a.getPatientName() %></strong><br>
                                    <small style="color: var(--text-muted);"><%= a.getPatientMrn() %></small>
                                </td>
                                <td><%= a.getAppointmentDate() %></td>
                                <td><%= a.getStartTime() %> - <%= a.getEndTime() %></td>
                                <td><%= a.getReasonForVisit() %></td>
                                <td><span class="badge badge-<%= a.getStatus().name().toLowerCase() %>"><%= a.getStatus() %></span></td>
                                <td>
                                    <% if (a.getStatus() != com.clinic.model.AppointmentStatus.COMPLETED) { %>
                                        <a href="<%= request.getContextPath() %>/doctor/prescriptions?aptId=<%= a.getAppointmentId() %>&patId=<%= a.getPatientId() %>" class="btn btn-primary btn-sm">Consult & Rx</a>
                                    <% } else { %>
                                        <span style="font-size: 0.8rem; color: #15803d; font-weight: bold;">✓ Completed</span>
                                    <% } %>
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
            <p>&copy; 2026 Medicare Clinic System. Doctor Clinical Portal.</p>
        </div>
    </footer>
</body>
</html>
