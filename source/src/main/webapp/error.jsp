<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>System Notification | Medicare Clinic</title>
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
        <div class="card" style="max-width: 600px; margin: 4rem auto; text-align: center; padding: 3rem 2rem;">
            <div style="font-size: 3.5rem; margin-bottom: 1rem;">🛡️</div>
            <h2>System Alert / Exception Occurred</h2>
            <p style="color: var(--text-muted); margin-top: 0.5rem;">
                <%
                    String msg = (String) request.getAttribute("errorMessage");
                    if (msg == null) msg = request.getParameter("msg");
                    if (msg == null && exception != null) msg = exception.getMessage();
                    if (msg == null) msg = "The requested resource could not be found or you do not have permission to access it.";
                %>
                <%= msg %>
            </p>

            <div style="margin-top: 2rem; display: flex; gap: 1rem; justify-content: center;">
                <a href="<%= request.getContextPath() %>/" class="btn btn-primary">Return to Home</a>
                <a href="javascript:history.back()" class="btn btn-secondary">Go Back</a>
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
