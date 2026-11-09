package com.clinic.servlet;

import com.clinic.legacy.LegacyPatientImporter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LegacyImportServlet", urlPatterns = {"/admin/legacy-import"})
public class LegacyImportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final LegacyPatientImporter importer = new LegacyPatientImporter();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String legacyData = req.getParameter("legacyData");
        LegacyPatientImporter.ImportResult result = importer.importLegacyStream(legacyData);

        req.setAttribute("importResult", result);
        req.getRequestDispatcher("/admin/reports.jsp").forward(req, resp);
    }
}
