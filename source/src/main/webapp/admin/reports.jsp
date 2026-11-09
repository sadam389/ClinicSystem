<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.clinic.model.Appointment, com.clinic.legacy.LegacyPatientImporter, java.util.List, java.util.Map" %>
<%
    Map<String, Object> metrics = (Map<String, Object>) request.getAttribute("metrics");
    List<Appointment> appointments = (List<Appointment>) request.getAttribute("appointments");
    LegacyPatientImporter.ImportResult importResult = (LegacyPatientImporter.ImportResult) request.getAttribute("importResult");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reports & Legacy Salvaging | Medicare Admin</title>
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
                <li><a href="<%= request.getContextPath() %>/admin/manage-doctors">Credential Doctors</a></li>
                <li><a href="<%= request.getContextPath() %>/admin/reports" class="active">Clinical Analytics & Legacy</a></li>
                <li><a href="<%= request.getContextPath() %>/logout" class="btn btn-secondary btn-sm">Logout</a></li>
            </ul>
        </div>
    </header>

    <main class="container">
        <div class="card">
            <h1>Clinical Operational Analytics & Reporting</h1>
            <p style="color: var(--text-muted); margin-bottom: 0;">Audit logs, department workload metrics, and legacy patient migration engine</p>
        </div>

        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-icon">📊</div>
                <div>
                    <div class="stat-val"><%= metrics != null ? metrics.get("completedAppointments") : 0 %></div>
                    <div class="stat-lbl">Completed Consultations</div>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon">❌</div>
                <div>
                    <div class="stat-val" style="color: #ef4444;"><%= metrics != null ? metrics.get("cancelledAppointments") : 0 %></div>
                    <div class="stat-lbl">Cancellations / No-shows</div>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon">⏳</div>
                <div>
                    <div class="stat-val" style="color: #0284c7;"><%= metrics != null ? metrics.get("upcomingAppointments") : 0 %></div>
                    <div class="stat-lbl">Pending / Confirmed Slots</div>
                </div>
            </div>
        </div>

        <!-- Legacy Data Salvaging Section -->
        <div class="card" style="border: 2px dashed #0284c7; background: #f0f9ff;">
            <div class="card-header" style="border-bottom-color: #bae6fd;">
                <div>
                    <h2 style="color: #0369a1;">🔄 Legacy Patient Data Salvaging Engine</h2>
                    <p style="font-size: 0.85rem; color: #0284c7; margin-bottom: 0;">
                        Course Concept Demonstration: Code Salvaging, Refactoring & Migration Pipeline
                    </p>
                </div>
                <span class="badge badge-confirmed">Active Importer</span>
            </div>

            <% if (importResult != null) { %>
                <div class="alert alert-success">
                    <div>
                        <strong>Batch Import Summary:</strong> Total Processed: <%= importResult.getTotalRecords() %> |
                        Success: <%= importResult.getSuccessfulImports() %> |
                        Failed / Staged: <%= importResult.getFailedRecords() %>
                        <ul style="margin-top: 0.5rem; font-size: 0.85rem; padding-left: 1.25rem;">
                            <% for (String log : importResult.getLogs()) { %>
                                <li><%= log %></li>
                            <% } %>
                        </ul>
                    </div>
                </div>
            <% } %>

            <form action="<%= request.getContextPath() %>/admin/legacy-import" method="POST">
                <div class="form-group">
                    <label class="form-label" for="legacyData">
                        Paste Raw Semicolon-Delimited Legacy Stream:
                        <small style="font-weight: normal; color: var(--text-muted);">(Format: LegacyMRN;FullName;DOB;Phone;Address;BloodGroup)</small>
                    </label>
                    <textarea id="legacyData" name="legacyData" class="form-control" rows="5" style="font-family: monospace; font-size: 0.85rem;"><%=
"# Sample Flat-File Dump from Old Desktop System\n" +
"LEG-8841;Harold Finch;24/06/1962;555-0199;34 Central Park West, NY;A+\n" +
"LEG-8842;Jocelyn Fray;1992-08-14;555-0188;88 Greenpoint Ave, Brooklyn;O-\n" +
"LEG-8843;Marcus Brody;15/03/1958;555-0177;12 Museum Way, Metro;B+"
                    %></textarea>
                </div>
                <button type="submit" class="btn btn-primary" style="padding: 0.65rem 1.5rem;">
                    Run Salvaged Legacy Importer &rarr;
                </button>
            </form>
        </div>
    </main>

    <footer>
        <div class="container">
            <p>&copy; 2026 Medicare Clinic System. Executive Analytics & Data Migration Module.</p>
        </div>
    </footer>
</body>
</html>
