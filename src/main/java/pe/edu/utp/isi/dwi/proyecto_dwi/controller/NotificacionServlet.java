package pe.edu.utp.isi.dwi.proyecto_dwi.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import pe.edu.utp.isi.dwi.proyecto_dwi.dto.NotificacionDTO;
import pe.edu.utp.isi.dwi.proyecto_dwi.service.NotificacionFacade;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "NotificacionServlet", urlPatterns = {"/notificacion"})
public class NotificacionServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(NotificacionServlet.class.getName());
    private final NotificacionFacade notificacionFacade = new NotificacionFacade();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if (null == accion) {
            listarNotificaciones(request, response);
        } else switch (accion) {
            case "listar" -> listarNotificaciones(request, response);
            case "marcarLeida" -> marcarNotificacionLeida(request, response);
            default -> redirigirConMensaje(request, response, "Acción no soportada.", "error.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if (null == accion) {
            redirigirConMensaje(request, response, "Acción no soportada.", "error.jsp");
        } else switch (accion) {
            case "eliminar" -> eliminarNotificacion(request, response);
            default -> redirigirConMensaje(request, response, "Acción no soportada.", "error.jsp");
        }
    }

    // Método para obtener el usuario de la sesión
    private Integer obtenerUsuarioDeSesion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("idUsuario") == null) {
            response.sendRedirect("login.jsp");
            return null;
        }
        return (Integer) session.getAttribute("idUsuario");
    }

    // Método para listar las notificaciones del usuario
    private void listarNotificaciones(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer idUsuario = obtenerUsuarioDeSesion(request, response);
        if (idUsuario == null) {
            return;
        }

        try {
            List<NotificacionDTO> notificaciones = notificacionFacade.listarNotificaciones(idUsuario);
            request.setAttribute("notificaciones", notificaciones);
            request.getRequestDispatcher("listar_notificaciones.jsp").forward(request, response);
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Error inesperado al listar las notificaciones", e);
            request.setAttribute("mensajeError", "Ocurrió un error inesperado al listar las notificaciones.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    // Método para marcar una notificación como leída
    private void marcarNotificacionLeida(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer idUsuario = obtenerUsuarioDeSesion(request, response);
        if (idUsuario == null) {
            return;
        }

        try {
            int idNotificacion = Integer.parseInt(request.getParameter("id"));
            notificacionFacade.marcarComoLeida(idNotificacion);
            response.sendRedirect("notificacion?accion=listar");
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "ID de notificación no válido: " + request.getParameter("id"), e);
            response.sendRedirect("notificacion?accion=listar&mensajeError=ID de notificación no válido.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error de redirección al marcar la notificación como leída", e);
            throw e;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error inesperado al marcar la notificación como leída", e);
            response.sendRedirect("notificacion?accion=listar&mensajeError=Ocurrió un error inesperado al marcar la notificación como leída.");
        }
    }

    // Método para eliminar una notificación
    private void eliminarNotificacion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer idUsuario = obtenerUsuarioDeSesion(request, response);
        if (idUsuario == null) {
            return;
        }

        try {
            int idNotificacion = Integer.parseInt(request.getParameter("id"));
            notificacionFacade.eliminarNotificacion(idNotificacion);
            response.sendRedirect("notificacion?accion=listar");
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "ID de notificación no válido: " + request.getParameter("id"), e);
            response.sendRedirect("notificacion?accion=listar&mensajeError=ID de notificación no válido.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error de redirección al eliminar la notificación", e);
            throw e;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error inesperado al eliminar la notificación", e);
            response.sendRedirect("notificacion?accion=listar&mensajeError=Ocurrió un error inesperado al eliminar la notificación.");
        }
    }

    // Método auxiliar para redirigir con un mensaje específico
    private void redirigirConMensaje(HttpServletRequest request, HttpServletResponse response, String mensaje, String paginaDestino) throws ServletException, IOException {
        request.setAttribute("mensaje", mensaje);
        request.getRequestDispatcher(paginaDestino).forward(request, response);
    }
}
