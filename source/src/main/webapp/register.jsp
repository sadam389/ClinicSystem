<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Patient Registration | Medicare Clinical System</title>
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
                <li><a href="<%= request.getContextPath() %>/">Home</a></li>
                <li><a href="<%= request.getContextPath() %>/login.jsp">Login</a></li>
            </ul>
        </div>
    </header>

    <main class="container">
        <div class="auth-box" style="max-width: 640px;">
            <div class="auth-header">
                <h2>New Patient Registration</h2>
                <p style="color: var(--text-muted); font-size: 0.9rem;">Register to schedule visits and access your digital medical records</p>
            </div>

            <% String err = (String) request.getAttribute("errorMessage");
               if (err != null) { %>
                <div class="alert alert-danger">
                    <span>⚠️</span>
                    <span><%= err %></span>
                </div>
            <% } %>

            <form action="<%= request.getContextPath() %>/register" method="POST">
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
                        <label class="form-label" for="email">Email Address *</label>
                        <input type="email" id="email" name="email" class="form-control" placeholder="name@domain.com" required />
                    </div>
                    <div class="form-group">
                        <label class="form-label" for="password">Password (min 8 chars) *</label>
                        <input type="password" id="password" name="password" class="form-control" required minlength="8" />
                    </div>
                </div>

                <div class="form-grid-2">
                    <div class="form-group">
                        <label class="form-label" for="dob">Date of Birth *</label>
                        <input type="date" id="dob" name="dob" class="form-control" required />
                    </div>
                    <div class="form-group">
                        <label class="form-label" for="gender">Gender *</label>
                        <select id="gender" name="gender" class="form-control" required>
                            <option value="MALE">Male</option>
                            <option value="FEMALE">Female</option>
                            <option value="OTHER">Other</option>
                        </select>
                    </div>
                </div>

                <div class="form-grid-2">
                    <div class="form-group">
                        <label class="form-label" for="phone">Phone Number *</label>
                        <input type="tel" id="phone" name="phone" class="form-control" placeholder="+1-555-0100" required />
                    </div>
                    <div class="form-group">
                        <label class="form-label" for="bloodGroup">Blood Group</label>
                        <select id="bloodGroup" name="bloodGroup" class="form-control">
                            <option value="O+">O+</option>
                            <option value="O-">O-</option>
                            <option value="A+">A+</option>
                            <option value="A-">A-</option>
                            <option value="B+">B+</option>
                            <option value="B-">B-</option>
                            <option value="AB+">AB+</option>
                            <option value="AB-">AB-</option>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label" for="address">Residential Address *</label>
                    <input type="text" id="address" name="address" class="form-control" required />
                </div>

                <div class="form-grid-2">
                    <div class="form-group">
                        <label class="form-label" for="emergencyContact">Emergency Contact Person *</label>
                        <input type="text" id="emergencyContact" name="emergencyContact" class="form-control" placeholder="Spouse / Parent Name" required />
                    </div>
                    <div class="form-group">
                        <label class="form-label" for="emergencyPhone">Emergency Phone *</label>
                        <input type="tel" id="emergencyPhone" name="emergencyPhone" class="form-control" required />
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label" for="allergies">Known Allergies / Medical Alerts</label>
                    <textarea id="allergies" name="allergies" class="form-control" rows="2" placeholder="e.g. Penicillin, Aspirin, Latex (or None)"></textarea>
                </div>

                <button type="submit" class="btn btn-primary" style="width: 100%; padding: 0.75rem;">Complete Patient Registration</button>
            </form>

            <div style="margin-top: 1.5rem; text-align: center; font-size: 0.875rem;">
                Already registered? <a href="<%= request.getContextPath() %>/login.jsp">Sign in here</a>
            </div>
        </div>
    </main>

    <footer>
        <div class="container">
            <p>&copy; 2026 Medicare Clinic System. SCD Semester Capstone Project.</p>
        </div>
    </footer>
</body>
</html>
