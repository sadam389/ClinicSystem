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
    <title>Doctor Credentialing | Medicare Admin</title>
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
                <li><a href="<%= request.getContextPath() %>/admin/dashboard">Overview</a></li>
                <li><a href="<%= request.getContextPath() %>/admin/manage-doctors" class="active">Credential Doctors</a></li>
                <li><a href="<%= request.getContextPath() %>/admin/reports">Clinical Analytics & Legacy</a></li>
                <li><a href="<%= request.getContextPath() %>/logout" class="btn btn-secondary btn-sm">Logout</a></li>
            </ul>
        </div>
    </header>

    <main class="container">
        <% String msg = request.getParameter("msg");
           if (msg != null) { %>
            <div class="alert alert-success"><span>✓</span> <span><%= msg %></span></div>
        <% } %>

        <% String err = (String) request.getAttribute("errorMessage");
           if (err != null) { %>
            <div class="alert alert-danger"><span>⚠️</span> <span><%= err %></span></div>
        <% } %>

        <div class="card">
            <div class="card-header">
                <h2>Credential New Physician / Doctor Account</h2>
            </div>
            <form action="<%= request.getContextPath() %>/admin/manage-doctors" method="POST">
                <div class="form-grid-2">
                    <div class="form-group">
                        <label class="form-label" for="firstName">First Name *</label>
                        <input type="text" id="firstName" name="firstName" class="form-control" required />
                    </div>
                    <div class="form-group">
                        <label class="form-label" for="lastName">Last Name *</label>
                        <input type="text" id="lastName" name="lastName" class="form-control" required />
                    </div>
                </div>

                <div class="form-grid-2">
                    <div class="form-group">
                        <label class="form-label" for="email">Doctor Email *</label>
                        <input type="email" id="email" name="email" class="form-control" placeholder="dr.name@medicare.org" required />
                    </div>
                    <div class="form-group">
                        <label class="form-label" for="password">Initial Password (min 8) *</label>
                        <input type="password" id="password" name="password" class="form-control" required minlength="8" />
                    </div>
                </div>

                <div class="form-grid-2">
                    <div class="form-group">
                        <label class="form-label" for="departmentId">Clinical Department *</label>
                        <select id="departmentId" name="departmentId" class="form-control" required>
                            <% if (departments != null) {
                                for (Department dep : departments) { %>
                                    <option value="<%= dep.getDepartmentId() %>"><%= dep.getName() %> (<%= dep.getCode() %>)</option>
                            <%  }
                            } %>
                        </select>
                    </div>
                    <div class="form-group">
                        <label class="form-label" for="licenseNumber">Medical License Number *</label>
                        <input type="text" id="licenseNumber" name="licenseNumber" class="form-control" placeholder="MED-LIC-XXXXX" required />
                    </div>
                </div>

                <div class="form-grid-2">
                    <div class="form-group">
                        <label class="form-label" for="phone">Phone Contact *</label>
                        <input type="tel" id="phone" name="phone" class="form-control" required />
                    </div>
                    <div class="form-group">
                        <label class="form-label" for="qualification">Qualifications & Fellowship *</label>
                        <input type="text" id="qualification" name="qualification" class="form-control" placeholder="e.g. MD, FACC, MBBS" required />
                    </div>
                </div>

                <div class="form-grid-2">
                    <div class="form-group">
                        <label class="form-label" for="experienceYears">Years of Experience</label>
                        <input type="number" id="experienceYears" name="experienceYears" class="form-control" value="5" min="0" />
                    </div>
                    <div class="form-group">
                        <label class="form-label" for="roomNumber">Consultation Room *</label>
                        <input type="text" id="roomNumber" name="roomNumber" class="form-control" placeholder="Room 201" required />
                    </div>
                </div>

                <button type="submit" class="btn btn-primary" style="padding: 0.75rem 2rem;">Save & Issue Doctor Credentials</button>
            </form>
        </div>

        <div class="card">
            <div class="card-header">
                <h2>Roster of Clinical Physicians</h2>
            </div>
            <div class="table-wrapper">
                <table>
                    <thead>
                        <tr>
                            <th>License</th>
                            <th>Doctor Name</th>
                            <th>Department</th>
                            <th>Room</th>
                            <th>Phone</th>
                            <th>Experience</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% if (doctors != null) {
                            for (Doctor d : doctors) { %>
                            <tr>
                                <td><code><%= d.getLicenseNumber() %></code></td>
                                <td><strong><%= d.getFullName() %></strong><br><small style="color:var(--text-muted);"><%= d.getQualification() %></small></td>
                                <td><%= d.getDepartmentName() %></td>
                                <td><%= d.getRoomNumber() %></td>
                                <td><%= d.getPhone() %></td>
                                <td><%= d.getExperienceYears() %> years</td>
                                <td><span class="badge badge-<%= d.isAvailable() ? "confirmed" : "cancelled" %>"><%= d.isAvailable() ? "ACTIVE" : "ON LEAVE" %></span></td>
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
            <p>&copy; 2026 Medicare Clinic System. Physician Credentialing Module.</p>
        </div>
    </footer>
</body>
</html>
