package pe.edu.utp.isi.dwi.proyecto_dwi.controller;

import pe.edu.utp.isi.dwi.proyecto_dwi.service.DashboardFacade;
import pe.edu.utp.isi.dwi.proyecto_dwi.dto.DashboardDataDTO;
import pe.edu.utp.isi.dwi.proyecto_dwi.entities.Usuario;
import pe.edu.utp.isi.dwi.proyecto_dwi.util.Roles;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "DashboardServlet", urlPatterns = {"/Dashboard"})
public class DashboardServlet extends HttpServlet {

    private DashboardFacade dashboardFacade;
    private static final Logger logger = Logger.getLogger(DashboardServlet.class.getName());

    @Override
    public void init() throws ServletException {
        try {
            // Inicializa el Facade, utilizando DAOFactory con Singleton
            dashboardFacade = new DashboardFacade();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al inicializar DashboardFacade", e);
            throw new ServletException("Error al inicializar DashboardFacade", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        procesarSolicitud(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        procesarSolicitud(request, response);
    }

    private void procesarSolicitud(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("usuario") == null) {
                redirigirLogin(response);
                return;
            }

            Usuario usuario = (Usuario) session.getAttribute("usuario");

            if (usuario.getColaborador() != null && Roles.CARGO_JEFE_DE_AREA.equalsIgnoreCase(usuario.getColaborador().getCargo().trim())) {
                DashboardDataDTO dashboardData = dashboardFacade.obtenerEstadisticas();
                request.setAttribute("dashboardData", dashboardData);
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
            } else {
                redirigirError(response, "sin-permiso");
            }
        } catch (ServletException e) {
            logger.log(Level.SEVERE, "Error de servlet al generar el dashboard", e);
            redirigirConMensajeError(request, response, "Error de servidor al generar el dashboard.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error de IO al generar el dashboard", e);
            redirigirConMensajeError(request, response, "Error de entrada/salida al generar el dashboard.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error inesperado al generar el dashboard", e);
            redirigirConMensajeError(request, response, "Error inesperado al generar el dashboard.");
        }
    }

    // Método para redirigir al login si la sesión no es válida
    private void redirigirLogin(HttpServletResponse response) throws IOException {
        response.sendRedirect("login.jsp");
    }

    // Método para redirigir al error general
    private void redirigirError(HttpServletResponse response, String mensaje) throws IOException {
        response.sendRedirect("error.jsp?mensaje=" + mensaje);
    }

    // Método para redirigir con mensaje de error y mantener la solicitud
    private void redirigirConMensajeError(HttpServletRequest request, HttpServletResponse response, String mensajeError) throws ServletException, IOException {
        request.setAttribute("mensajeError", mensajeError);
        request.getRequestDispatcher("error.jsp").forward(request, response);
    }
}
