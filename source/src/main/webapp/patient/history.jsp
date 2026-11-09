<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.clinic.model.Patient, com.clinic.model.MedicalRecord, com.clinic.model.Prescription, com.clinic.model.PrescriptionItem, java.util.List" %>
<%
    Patient patient = (Patient) request.getAttribute("patient");
    List<MedicalRecord> history = (List<MedicalRecord>) request.getAttribute("history");
    List<Prescription> prescriptions = (List<Prescription>) request.getAttribute("prescriptions");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Medical History & Records | Medicare Clinic</title>
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
                <li><a href="<%= request.getContextPath() %>/patient/dashboard">Dashboard</a></li>
                <li><a href="<%= request.getContextPath() %>/patient/book-appointment">Book Visit</a></li>
                <li><a href="<%= request.getContextPath() %>/patient/history" class="active">Medical Records</a></li>
                <li><a href="<%= request.getContextPath() %>/logout" class="btn btn-secondary btn-sm">Logout</a></li>
            </ul>
        </div>
    </header>

    <main class="container">
        <div class="card">
            <h1>Electronic Health Records (EHR)</h1>
            <p style="color: var(--text-muted); margin-bottom: 0;">
                Patient: <strong><%= patient.getFullName() %></strong> (MRN: <%= patient.getPatientMrn() %>) | DOB: <%= patient.getDateOfBirth() %>
            </p>
        </div>

        <div class="card">
            <div class="card-header">
                <h2>Clinical Consultations & Diagnostics</h2>
            </div>
            <% if (history == null || history.isEmpty()) { %>
                <p style="text-align: center; color: var(--text-muted); padding: 1.5rem 0;">No diagnostic consultation records found on file.</p>
            <% } else {
                for (MedicalRecord rec : history) { %>
                <div style="border: 1px solid var(--border); border-radius: 8px; padding: 1.25rem; margin-bottom: 1.25rem; background: #ffffff;">
                    <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid var(--border); padding-bottom: 0.5rem; margin-bottom: 0.75rem;">
                        <div>
                            <strong style="font-size: 1.1rem; color: var(--secondary);"><%= rec.getDiagnosis() %></strong><br>
                            <span style="font-size: 0.85rem; color: var(--text-muted);">Attending: <%= rec.getDoctorName() %></span>
                        </div>
                        <span style="font-size: 0.85rem; color: var(--text-muted);"><%= rec.getCreatedAt() %></span>
                    </div>

                    <div class="stats-grid" style="margin-bottom: 1rem; grid-template-columns: repeat(4, 1fr);">
                        <div style="background: #f8fafc; padding: 0.5rem 0.75rem; border-radius: 6px;">
                            <span style="font-size: 0.75rem; color: var(--text-muted); display: block;">Blood Pressure</span>
                            <strong><%= rec.getBloodPressure() != null ? rec.getBloodPressure() : "N/A" %></strong>
                        </div>
                        <div style="background: #f8fafc; padding: 0.5rem 0.75rem; border-radius: 6px;">
                            <span style="font-size: 0.75rem; color: var(--text-muted); display: block;">Heart Rate</span>
                            <strong><%= rec.getPulseBpm() != null ? rec.getPulseBpm() + " bpm" : "N/A" %></strong>
                        </div>
                        <div style="background: #f8fafc; padding: 0.5rem 0.75rem; border-radius: 6px;">
                            <span style="font-size: 0.75rem; color: var(--text-muted); display: block;">Temperature</span>
                            <strong><%= rec.getTemperatureCelsius() != null ? rec.getTemperatureCelsius() + " °C" : "N/A" %></strong>
                        </div>
                        <div style="background: #f8fafc; padding: 0.5rem 0.75rem; border-radius: 6px;">
                            <span style="font-size: 0.75rem; color: var(--text-muted); display: block;">Weight</span>
                            <strong><%= rec.getWeightKg() != null ? rec.getWeightKg() + " kg" : "N/A" %></strong>
                        </div>
                    </div>

                    <p><strong>Presenting Symptoms:</strong> <%= rec.getSymptoms() %></p>
                    <p style="margin-bottom: 0;"><strong>Doctor Clinical Notes:</strong> <%= rec.getClinicalNotes() %></p>
                </div>
            <%  }
            } %>
        </div>

        <div class="card">
            <div class="card-header">
                <h2>Issued Prescriptions & Medications</h2>
            </div>
            <% if (prescriptions == null || prescriptions.isEmpty()) { %>
                <p style="text-align: center; color: var(--text-muted); padding: 1.5rem 0;">No active prescriptions issued.</p>
            <% } else {
                for (Prescription rx : prescriptions) { %>
                <div style="border: 1px solid #bae6fd; background: #f0f9ff; border-radius: 8px; padding: 1.25rem; margin-bottom: 1.25rem;">
                    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 0.75rem;">
                        <div>
                            <strong>Prescribed by <%= rx.getDoctorName() %></strong><br>
                            <small style="color: var(--text-muted);">Diagnosis: <%= rx.getDiagnosis() %></small>
                        </div>
                        <span style="font-size: 0.85rem; color: var(--text-muted);"><%= rx.getCreatedAt() %></span>
                    </div>

                    <div class="table-wrapper" style="background: white; margin-bottom: 0.75rem;">
                        <table>
                            <thead>
                                <tr>
                                    <th>Medicine Name</th>
                                    <th>Dosage</th>
                                    <th>Frequency</th>
                                    <th>Duration</th>
                                    <th>Instructions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% if (rx.getItems() != null) {
                                    for (PrescriptionItem it : rx.getItems()) { %>
                                    <tr>
                                        <td><strong><%= it.getMedicineName() %></strong></td>
                                        <td><%= it.getDosage() %></td>
                                        <td><code><%= it.getFrequency() %></code></td>
                                        <td><%= it.getDurationDays() %> days</td>
                                        <td><%= it.getInstructions() %></td>
                                    </tr>
                                <%  }
                                } %>
                            </tbody>
                        </table>
                    </div>

                    <p style="font-size: 0.9rem; margin-bottom: 0;">
                        <strong>Special Doctor Advice:</strong> <%= rx.getAdvice() %>
                        <% if (rx.getFollowUpDate() != null) { %>
                            | <strong>Follow-up Consultation:</strong> <%= rx.getFollowUpDate() %>
                        <% } %>
                    </p>
                </div>
            <%  }
            } %>
        </div>
    </main>

    <footer>
        <div class="container">
            <p>&copy; 2026 Medicare Clinic System. Patient Medical Records Module.</p>
        </div>
    </footer>
</body>
</html>
