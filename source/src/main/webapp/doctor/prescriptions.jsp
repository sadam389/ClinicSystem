<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.clinic.model.Doctor, com.clinic.model.Prescription, com.clinic.model.PrescriptionItem, com.clinic.model.Appointment, java.util.List" %>
<%
    Doctor doctor = (Doctor) request.getAttribute("doctor");
    List<Prescription> prescriptions = (List<Prescription>) request.getAttribute("prescriptions");
    List<Appointment> pendingAppointments = (List<Appointment>) request.getAttribute("pendingAppointments");
    String aptIdParam = request.getParameter("aptId");
    String patIdParam = request.getParameter("patId");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Clinical Consultation & E-Prescribing | Medicare</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <script src="<%= request.getContextPath() %>/js/main.js" defer></script>
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
                <li><a href="<%= request.getContextPath() %>/doctor/schedule">My Schedule</a></li>
                <li><a href="<%= request.getContextPath() %>/doctor/prescriptions" class="active">E-Prescribing</a></li>
                <li><a href="<%= request.getContextPath() %>/logout" class="btn btn-secondary btn-sm">Logout</a></li>
            </ul>
        </div>
    </header>

    <main class="container">
        <% String msg = request.getParameter("msg");
           if (msg != null) { %>
            <div class="alert alert-success"><span>✓</span> <span><%= msg %></span></div>
        <% } %>

        <div class="card">
            <div class="card-header">
                <h2>Record Consultation & Issue Digital Prescription</h2>
            </div>

            <form action="<%= request.getContextPath() %>/doctor/prescriptions" method="POST">
                <div class="form-grid-2">
                    <div class="form-group">
                        <label class="form-label" for="appointmentId">Select Scheduled Patient Visit *</label>
                        <select id="appointmentId" name="appointmentId" class="form-control" required>
                            <option value="0">-- Direct Walk-in / Follow-up --</option>
                            <% if (pendingAppointments != null) {
                                for (Appointment a : pendingAppointments) {
                                    boolean sel = aptIdParam != null && aptIdParam.equals(String.valueOf(a.getAppointmentId()));
                            %>
                                <option value="<%= a.getAppointmentId() %>" <%= sel ? "selected" : "" %> data-pat="<%= a.getPatientId() %>">
                                    <%= a.getPatientName() %> (<%= a.getPatientMrn() %>) - <%= a.getAppointmentDate() %> [<%= a.getReasonForVisit() %>]
                                </option>
                            <%  }
                            } %>
                        </select>
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="patientId">Patient ID *</label>
                        <input type="number" id="patientId" name="patientId" class="form-control"
                               value="<%= patIdParam != null ? patIdParam : "1" %>" required />
                    </div>
                </div>

                <div class="form-grid-2">
                    <div class="form-group">
                        <label class="form-label" for="diagnosis">Clinical Diagnosis *</label>
                        <input type="text" id="diagnosis" name="diagnosis" class="form-control"
                               placeholder="e.g. Acute Bacterial Pharyngitis" required />
                    </div>
                    <div class="form-group">
                        <label class="form-label" for="symptoms">Reported Symptoms *</label>
                        <input type="text" id="symptoms" name="symptoms" class="form-control"
                               placeholder="e.g. Sore throat for 3 days, low grade pyrexia" required />
                    </div>
                </div>

                <div class="form-grid-2" style="grid-template-columns: repeat(4, 1fr);">
                    <div class="form-group">
                        <label class="form-label" for="bloodPressure">BP (mmHg)</label>
                        <input type="text" id="bloodPressure" name="bloodPressure" class="form-control" placeholder="120/80" />
                    </div>
                    <div class="form-group">
                        <label class="form-label" for="pulseBpm">Heart Rate (bpm)</label>
                        <input type="number" id="pulseBpm" name="pulseBpm" class="form-control" placeholder="72" />
                    </div>
                    <div class="form-group">
                        <label class="form-label" for="temperature">Temp (°C)</label>
                        <input type="number" step="0.1" id="temperature" name="temperature" class="form-control" placeholder="37.0" />
                    </div>
                    <div class="form-group">
                        <label class="form-label" for="weight">Weight (kg)</label>
                        <input type="number" step="0.1" id="weight" name="weight" class="form-control" placeholder="70.5" />
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label" for="clinicalNotes">Clinical Observations & Physical Exam</label>
                    <textarea id="clinicalNotes" name="clinicalNotes" class="form-control" rows="2"
                              placeholder="e.g. Oropharynx inflamed with tonsillar exudate. No lymphadenopathy."></textarea>
                </div>

                <!-- Prescribed Medicines -->
                <div style="background: #f8fafc; border: 1px solid var(--border); border-radius: 8px; padding: 1.25rem; margin-bottom: 1.25rem;">
                    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1rem;">
                        <h3 style="font-size: 1.1rem; margin-bottom: 0;">Prescribed Medications (Rx)</h3>
                        <button type="button" id="add-medicine-btn" class="btn btn-secondary btn-sm">+ Add Medicine Line</button>
                    </div>

                    <div id="medicines-container">
                        <div class="form-grid-2 medicine-row" style="margin-bottom: 0.75rem;">
                            <div>
                                <input type="text" name="medicineName" class="form-control" placeholder="Medicine Name (e.g. Amoxicillin 500mg)" required />
                            </div>
                            <div style="display: flex; gap: 0.5rem;">
                                <input type="text" name="dosage" class="form-control" placeholder="Dosage (1 cap)" style="width: 30%;" value="1 cap" />
                                <input type="text" name="frequency" class="form-control" placeholder="Freq (1-0-1)" style="width: 30%;" value="1-0-1" />
                                <input type="number" name="duration" class="form-control" placeholder="Days" style="width: 40%;" value="7" />
                            </div>
                            <div style="grid-column: span 2; margin-top: -0.25rem;">
                                <input type="text" name="instructions" class="form-control" placeholder="Instructions (e.g. Take after meals, finish complete course)" />
                            </div>
                        </div>
                    </div>
                </div>

                <div class="form-grid-2">
                    <div class="form-group">
                        <label class="form-label" for="advice">Patient Dietary / Recovery Advice</label>
                        <input type="text" id="advice" name="advice" class="form-control" placeholder="Warm fluid intake, adequate rest, salt-water gargles" />
                    </div>
                    <div class="form-group">
                        <label class="form-label" for="followUpDate">Recommended Follow-up Date</label>
                        <input type="date" id="followUpDate" name="followUpDate" class="form-control" />
                    </div>
                </div>

                <button type="submit" class="btn btn-primary" style="padding: 0.85rem 2rem; font-size: 1rem;">
                    Finalize Consultation & Issue Prescription
                </button>
            </form>
        </div>

        <div class="card">
            <div class="card-header">
                <h2>Recent Issued Prescriptions by You</h2>
            </div>
            <div class="table-wrapper">
                <table>
                    <thead>
                        <tr>
                            <th>Rx ID</th>
                            <th>Patient Name</th>
                            <th>Diagnosis</th>
                            <th>Prescribed Date</th>
                            <th>Medications Summary</th>
                            <th>Advice</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% if (prescriptions == null || prescriptions.isEmpty()) { %>
                            <tr><td colspan="6" style="text-align: center; color: var(--text-muted);">No prescriptions issued yet.</td></tr>
                        <% } else {
                            for (Prescription rx : prescriptions) { %>
                            <tr>
                                <td><code>RX-<%= rx.getPrescriptionId() %></code></td>
                                <td><strong><%= rx.getPatientName() %></strong></td>
                                <td><%= rx.getDiagnosis() %></td>
                                <td><%= rx.getCreatedAt() %></td>
                                <td>
                                    <% if (rx.getItems() != null) {
                                        for (PrescriptionItem it : rx.getItems()) { %>
                                            <div>• <%= it.getMedicineName() %> (<%= it.getDosage() %>, <%= it.getFrequency() %>)</div>
                                    <%  }
                                    } %>
                                </td>
                                <td><%= rx.getAdvice() != null ? rx.getAdvice() : "N/A" %></td>
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
            <p>&copy; 2026 Medicare Clinic System. E-Prescribing & Clinical Documentation Module.</p>
        </div>
    </footer>
</body>
</html>
