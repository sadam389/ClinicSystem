<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.clinic.model.Doctor, com.clinic.model.DoctorSchedule, java.util.List" %>
<%
    Doctor doctor = (Doctor) request.getAttribute("doctor");
    List<DoctorSchedule> schedules = (List<DoctorSchedule>) request.getAttribute("schedules");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Duty Schedule | <%= doctor.getFullName() %></title>
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
                <li><a href="<%= request.getContextPath() %>/doctor/dashboard">Dashboard</a></li>
                <li><a href="<%= request.getContextPath() %>/doctor/schedule" class="active">My Schedule</a></li>
                <li><a href="<%= request.getContextPath() %>/doctor/prescriptions">E-Prescribing</a></li>
                <li><a href="<%= request.getContextPath() %>/logout" class="btn btn-secondary btn-sm">Logout</a></li>
            </ul>
        </div>
    </header>

    <main class="container">
        <div class="card">
            <h1>Weekly Consultation Hours</h1>
            <p style="color: var(--text-muted); margin-bottom: 0;">
                Doctor: <strong><%= doctor.getFullName() %></strong> | Department: <%= doctor.getDepartmentName() %> | Room: <%= doctor.getRoomNumber() %>
            </p>
        </div>

        <div class="card">
            <div class="card-header">
                <h2>Active Duty Shifts</h2>
                <span class="badge badge-confirmed">30-Minute Standard Slots</span>
            </div>
            <div class="table-wrapper">
                <table>
                    <thead>
                        <tr>
                            <th>Day of Week</th>
                            <th>Shift Start</th>
                            <th>Shift End</th>
                            <th>Slot Duration</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% if (schedules == null || schedules.isEmpty()) { %>
                            <tr><td colspan="5" style="text-align: center; color: var(--text-muted);">No active schedule slots configured.</td></tr>
                        <% } else {
                            for (DoctorSchedule s : schedules) { %>
                            <tr>
                                <td><strong><%= s.getDayOfWeek() %></strong></td>
                                <td><%= s.getStartTime() %></td>
                                <td><%= s.getEndTime() %></td>
                                <td><%= s.getSlotDurationMinutes() %> minutes</td>
                                <td><span class="badge badge-confirmed"><%= s.isActive() ? "ACTIVE" : "INACTIVE" %></span></td>
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
            <p>&copy; 2026 Medicare Clinic System. Doctor Roster Module.</p>
        </div>
    </footer>
</body>
</html>
