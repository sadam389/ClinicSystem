<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login | Medicare Clinical System</title>
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
                <li><a href="<%= request.getContextPath() %>/register.jsp">Register</a></li>
            </ul>
        </div>
    </header>

    <main class="container">
        <div class="auth-box">
            <div class="auth-header">
                <div class="brand-icon" style="margin: 0 auto 0.75rem auto; width: 44px; height: 44px; font-size: 1.5rem;">+</div>
                <h2>Sign in to Account</h2>
                <p style="color: var(--text-muted); font-size: 0.9rem;">Access appointments, schedules, and clinical records</p>
            </div>

            <% String err = (String) request.getAttribute("errorMessage");
               if (err == null) err = request.getParameter("error");
               if (err != null) { %>
                <div class="alert alert-danger">
                    <span>⚠️</span>
                    <span><%= err %></span>
                </div>
            <% } %>

            <% String msg = request.getParameter("msg");
               if (msg != null) { %>
                <div class="alert alert-success">
                    <span>✓</span>
                    <span><%= msg %></span>
                </div>
            <% } %>

            <form action="<%= request.getContextPath() %>/login" method="POST">
                <div class="form-group">
                    <label class="form-label" for="email">Email Address</label>
                    <input type="email" id="email" name="email" class="form-control"
                           placeholder="e.g. dr.smith@medicare.org"
                           value="<%= request.getAttribute("inputEmail") != null ? request.getAttribute("inputEmail") : "" %>"
                           required autofocus />
                </div>

                <div class="form-group">
                    <label class="form-label" for="password">Password</label>
                    <input type="password" id="password" name="password" class="form-control"
                           placeholder="••••••••" required />
                </div>

                <button type="submit" class="btn btn-primary" style="width: 100%; margin-top: 0.5rem; padding: 0.75rem;">
                    Sign In
                </button>
            </form>

            <div style="margin-top: 1.5rem; text-align: center; font-size: 0.875rem; color: var(--text-muted); border-top: 1px solid var(--border); padding-top: 1rem;">
                Are you a new patient? <a href="<%= request.getContextPath() %>/register.jsp">Create an account here</a>
            </div>

            <div style="margin-top: 1.5rem; background: #f8fafc; border: 1px dashed var(--border); border-radius: 6px; padding: 0.75rem; font-size: 0.8rem;">
                <div style="font-weight: bold; margin-bottom: 0.25rem;">Quick Demo Accounts:</div>
                • Admin: <code>admin@medicare.org</code> / <code>AdminPass123!</code><br>
                • Doctor: <code>dr.smith@medicare.org</code> / <code>DoctorPass123!</code><br>
                • Patient: <code>john.doe@gmail.com</code> / <code>PatientPass123!</code>
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
