package pe.edu.utp.isi.dwi.proyecto_dwi.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import pe.edu.utp.isi.dwi.proyecto_dwi.dao.ConexionBD;
import pe.edu.utp.isi.dwi.proyecto_dwi.dto.LogAuditoriaDTO;
import pe.edu.utp.isi.dwi.proyecto_dwi.service.LogAuditoriaFacade;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "LogAuditoriaServlet", urlPatterns = {"/logsAuditoria"})
public class LogAuditoriaServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(LogAuditoriaServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection connection = ConexionBD.getConnection()) {
            LogAuditoriaFacade logAuditoriaFacade = new LogAuditoriaFacade(connection);
            List<LogAuditoriaDTO> logsAuditoriaList = logAuditoriaFacade.obtenerLogsAuditoria();
            request.setAttribute("logsAuditoriaList", logsAuditoriaList);
            request.getRequestDispatcher("logs_auditoria.jsp").forward(request, response);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al obtener los logs de auditoría.", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "No se pudo obtener los logs de auditoría.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener la sesión del usuario
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("idUsuario") == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Debe iniciar sesión para realizar esta acción.");
            return;
        }

        int idUsuario = (int) session.getAttribute("idUsuario");
        String accion = request.getParameter("accion");

        LogAuditoriaDTO logAuditoriaDTO = new LogAuditoriaDTO();
        logAuditoriaDTO.setIdUsuario(idUsuario);
        logAuditoriaDTO.setAccion(accion);
        logAuditoriaDTO.setFecha(new java.sql.Timestamp(System.currentTimeMillis())); // Fecha actual

        try (Connection connection = ConexionBD.getConnection()) {
            LogAuditoriaFacade logAuditoriaFacade = new LogAuditoriaFacade(connection);
            logAuditoriaFacade.registrarLog(logAuditoriaDTO);
            response.sendRedirect("logsAuditoria");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al registrar el log de auditoría.", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "No se pudo registrar el log de auditoría.");
        }
    }
}
