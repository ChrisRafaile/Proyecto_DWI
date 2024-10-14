package pe.edu.utp.isi.dwi.proyecto_dwi.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import pe.edu.utp.isi.dwi.proyecto_dwi.dto.HistorialCambiosDTO;
import pe.edu.utp.isi.dwi.proyecto_dwi.dao.ConexionBD;
import pe.edu.utp.isi.dwi.proyecto_dwi.service.HistorialCambiosFacade;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "HistorialCambiosServlet", urlPatterns = {"/historialCambios"})
public class HistorialCambiosServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(HistorialCambiosServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection connection = ConexionBD.getConnection()) {
            HistorialCambiosFacade historialCambiosFacade = new HistorialCambiosFacade(connection);
            List<HistorialCambiosDTO> historialCambiosList = historialCambiosFacade.obtenerHistorialCambios();
            request.setAttribute("historialCambiosList", historialCambiosList);
            request.getRequestDispatcher("historial_cambios.jsp").forward(request, response);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al obtener el historial de cambios.", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "No se pudo obtener el historial de cambios.");
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

        try {
            String descripcionCambio = request.getParameter("descripcionCambio");
            String tablaAfectada = request.getParameter("tablaAfectada");
            String campoAfectado = request.getParameter("campoAfectado");
            String valorAnterior = request.getParameter("valorAnterior");
            String valorNuevo = request.getParameter("valorNuevo");

            HistorialCambiosDTO historialCambiosDTO = new HistorialCambiosDTO();
            historialCambiosDTO.setIdUsuario(idUsuario);
            historialCambiosDTO.setDescripcionCambio(descripcionCambio);
            historialCambiosDTO.setTablaAfectada(tablaAfectada);
            historialCambiosDTO.setCampoAfectado(campoAfectado);
            historialCambiosDTO.setValorAnterior(valorAnterior);
            historialCambiosDTO.setValorNuevo(valorNuevo);

            try (Connection connection = ConexionBD.getConnection()) {
                HistorialCambiosFacade historialCambiosFacade = new HistorialCambiosFacade(connection);
                historialCambiosFacade.registrarCambio(historialCambiosDTO);
                response.sendRedirect("historialCambios");
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error al registrar el cambio en el historial.", e);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "No se pudo registrar el cambio en el historial.");
            }
        } catch (NumberFormatException e) {
            logger.log(Level.SEVERE, "Error en el formato de los parámetros.", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Formato de parámetros inválido.");
        }
    }
}
