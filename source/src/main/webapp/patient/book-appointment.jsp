<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.clinic.model.Doctor, com.clinic.model.Department, java.util.List" %>
<%
    List<Doctor> doctors = (List<Doctor>) request.getAttribute("doctors");
    List<Department> departments = (List<Department>) request.getAttribute("departments");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Book Appointment | Medicare Clinic</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <script src="<%= request.getContextPath() %>/js/main.js" defer></script>
</head>
<body>
    <header class="navbar">
        <div class="nav-container">
            <a href="<%= request.getContextPath() %>/patient/dashboard" class="brand">
                <span class="brand-icon">+</span>
                <span>Medicare Portal</span>
            </a>
            <ul class="nav-links">
                <li><a href="<%= request.getContextPath() %>/patient/dashboard">Dashboard</a></li>
                <li><a href="<%= request.getContextPath() %>/patient/book-appointment" class="active">Book Visit</a></li>
                <li><a href="<%= request.getContextPath() %>/patient/history">Medical Records</a></li>
                <li><a href="<%= request.getContextPath() %>/logout" class="btn btn-secondary btn-sm">Logout</a></li>
            </ul>
        </div>
    </header>

    <main class="container">
        <div class="card" style="max-width: 720px; margin: 0 auto;">
            <div class="card-header">
                <h2>Book Clinical Consultation</h2>
                <a href="<%= request.getContextPath() %>/patient/dashboard" class="btn btn-secondary btn-sm">&larr; Back to Dashboard</a>
            </div>

            <% String err = (String) request.getAttribute("errorMessage");
               if (err != null) { %>
                <div class="alert alert-danger"><span>⚠️</span> <span><%= err %></span></div>
            <% } %>

            <form action="<%= request.getContextPath() %>/patient/book-appointment" method="POST">
                <div class="form-group">
                    <label class="form-label" for="doctorId">Select Doctor & Specialty *</label>
                    <select id="doctorId" name="doctorId" class="form-control" required>
                        <option value="">-- Choose a doctor --</option>
                        <% if (doctors != null) {
                            for (Doctor doc : doctors) { %>
                                <option value="<%= doc.getDoctorId() %>">
                                    <%= doc.getFullName() %> (<%= doc.getDepartmentName() %>) - <%= doc.getRoomNumber() %>
                                </option>
                        <%  }
                        } %>
                    </select>
                </div>

                <div class="form-grid-2">
                    <div class="form-group">
                        <label class="form-label" for="appointmentDate">Appointment Date *</label>
                        <input type="date" id="appointmentDate" name="appointmentDate" class="form-control" required />
                    </div>
                    <div class="form-group">
                        <label class="form-label" for="appointmentTime">Preferred Start Time *</label>
                        <select id="appointmentTime" name="appointmentTime" class="form-control" required>
                            <option value="09:00">09:00 AM</option>
                            <option value="09:30">09:30 AM</option>
                            <option value="10:00">10:00 AM</option>
                            <option value="10:30">10:30 AM</option>
                            <option value="11:00">11:00 AM</option>
                            <option value="11:30">11:30 AM</option>
                            <option value="12:00">12:00 PM</option>
                            <option value="14:00">02:00 PM</option>
                            <option value="14:30">02:30 PM</option>
                            <option value="15:00">03:00 PM</option>
                            <option value="15:30">03:30 PM</option>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label" for="reason">Reason for Visit / Primary Symptoms *</label>
                    <textarea id="reason" name="reason" class="form-control" rows="3"
                              placeholder="Please describe your symptoms, discomfort, or reason for follow-up" required></textarea>
                </div>

                <div style="background: #f8fafc; border: 1px solid var(--border); padding: 1rem; border-radius: 6px; margin-bottom: 1.25rem;">
                    <div style="font-weight: 600; margin-bottom: 0.25rem;">Clinic Policy & Confirmation Notice:</div>
                    <p style="font-size: 0.85rem; color: var(--text-muted); margin-bottom: 0;">
                        Appointments are held for 15 minutes past start time. In case of emergency or severe chest pain, visit the 24/7 ER facility immediately.
                    </p>
                </div>

                <button type="submit" class="btn btn-primary" style="width: 100%; padding: 0.75rem;">Confirm & Reserve Appointment Slot</button>
            </form>
        </div>
    </main>

    <footer>
        <div class="container">
            <p>&copy; 2026 Medicare Clinic System. Outpatient Booking Module.</p>
        </div>
    </footer>
</body>
</html>
