package pe.edu.utp.isi.dwi.proyecto_dwi.controller;

import pe.edu.utp.isi.dwi.proyecto_dwi.service.SolicitudFacade;
import pe.edu.utp.isi.dwi.proyecto_dwi.dto.SolicitudDTO;
import pe.edu.utp.isi.dwi.proyecto_dwi.entities.Solicitud.EstadoSolicitud;
import pe.edu.utp.isi.dwi.proyecto_dwi.util.Roles;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "SolicitudServlet", urlPatterns = {"/Solicitud"})
public class SolicitudServlet extends HttpServlet {

    private SolicitudFacade solicitudFacade;
    private static final Logger logger = Logger.getLogger(SolicitudServlet.class.getName());

    @Override
    public void init() throws ServletException {
        try {
            super.init();
            solicitudFacade = new SolicitudFacade(); // Inicializar SolicitudFacade
        } catch (ServletException e) {
            logger.log(Level.SEVERE, "Error al inicializar el SolicitudFacade", e);
            throw new ServletException("Error al inicializar el SolicitudFacade", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");

        switch (accion == null ? "" : accion) {
            case "guardar" -> registrarSolicitud(request, response);
            case "eliminar" -> verificarPermiso(request, response, Roles.CARGO_JEFE_DE_AREA, () -> eliminarSolicitud(request, response));
            case "actualizar" -> verificarPermiso(request, response, new String[]{Roles.CARGO_JEFE_DE_AREA, Roles.CARGO_COORDINADOR}, () -> actualizarSolicitud(request, response));
            default -> response.sendRedirect("error.jsp?error=accion-invalida");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");

        switch (accion == null ? "" : accion) {
            case "listar" -> listarSolicitudes(request, response);
            case "editar" -> verificarPermiso(request, response, Roles.CARGO_JEFE_DE_AREA, () -> mostrarFormularioEditar(request, response));
            default -> response.sendRedirect("error.jsp?error=accion-invalida");
        }
    }

    // Métodos para manejar las acciones del servlet
    private void registrarSolicitud(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int idUsuario = obtenerIntDesdeParametro(request, "idUsuario");
            String tipoSolicitud = validarCampoTexto(request.getParameter("tipoSolicitud"), "El tipo de solicitud no puede estar vacío.");
            String descripcion = validarCampoTexto(request.getParameter("descripcion"), "La descripción no puede estar vacía.");

            SolicitudDTO solicitudDTO = new SolicitudDTO(
                    0, // ID autogenerado
                    idUsuario,
                    tipoSolicitud,
                    descripcion,
                    EstadoSolicitud.PENDIENTE, // Estado inicial
                    Timestamp.valueOf(LocalDateTime.now()),
                    null
            );

            solicitudFacade.registrarSolicitud(solicitudDTO);
            response.sendRedirect("solicitud_exitosa.jsp");
        } catch (IllegalArgumentException e) {
            manejarError(request, response, e.getMessage(), e);
        } catch (IOException e) {
            manejarError(request, response, "Error al registrar la solicitud.", e);
        }
    }

    private void actualizarSolicitud(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int idSolicitud = obtenerIntDesdeParametro(request, "idSolicitud");
            int idUsuario = obtenerIntDesdeParametro(request, "idUsuario");
            String tipoSolicitud = validarCampoTexto(request.getParameter("tipoSolicitud"), "El tipo de solicitud no puede estar vacío.");
            String descripcion = validarCampoTexto(request.getParameter("descripcion"), "La descripción no puede estar vacía.");
            EstadoSolicitud estado = EstadoSolicitud.valueOf(validarCampoTexto(request.getParameter("estado"), "El estado no puede estar vacío.").toUpperCase());

            SolicitudDTO solicitudDTO = new SolicitudDTO(
                    idSolicitud,
                    idUsuario,
                    tipoSolicitud,
                    descripcion,
                    estado,
                    null,
                    Timestamp.valueOf(LocalDateTime.now()) // Fecha de finalización
            );

            solicitudFacade.actualizarSolicitud(solicitudDTO);
            response.sendRedirect("Solicitud?accion=listar");
        } catch (IllegalArgumentException e) {
            manejarError(request, response, "Datos inválidos al actualizar la solicitud", e);
        } catch (IOException e) {
            manejarError(request, response, "Error al actualizar la solicitud.", e);
        }
    }

    private void listarSolicitudes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<SolicitudDTO> solicitudes = solicitudFacade.listarSolicitudes();
            request.setAttribute("solicitudes", solicitudes);
            request.getRequestDispatcher("listar_solicitudes.jsp").forward(request, response);
        } catch (ServletException | IOException e) {
            manejarError(request, response, "Error al listar las solicitudes.", e);
        }
    }

    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int idSolicitud = obtenerIntDesdeParametro(request, "idSolicitud");
            SolicitudDTO solicitudDTO = solicitudFacade.obtenerSolicitudPorId(idSolicitud);

            if (solicitudDTO != null) {
                request.setAttribute("solicitud", solicitudDTO);
                request.getRequestDispatcher("editar_solicitud.jsp").forward(request, response);
            } else {
                manejarError(request, response, "Solicitud no encontrada.", null);
            }
        } catch (NumberFormatException e) {
            manejarError(request, response, "ID de solicitud inválido", e);
        } catch (ServletException | IOException | IllegalArgumentException e) {
            manejarError(request, response, "Error al obtener la solicitud.", e);
        }
    }

    private void eliminarSolicitud(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int idSolicitud = obtenerIntDesdeParametro(request, "idSolicitud");
            solicitudFacade.eliminarSolicitud(idSolicitud);
            response.sendRedirect("Solicitud?accion=listar");
        } catch (NumberFormatException e) {
            manejarError(request, response, "ID de solicitud inválido", e);
        } catch (IOException | IllegalArgumentException e) {
            manejarError(request, response, "Error al eliminar la solicitud.", e);
        }
    }

    // Métodos auxiliares
    private int obtenerIntDesdeParametro(HttpServletRequest request, String parametro) throws IllegalArgumentException {
        try {
            return Integer.parseInt(request.getParameter(parametro));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El parámetro " + parametro + " debe ser un número válido.", e);
        }
    }

    private String validarCampoTexto(String valor, String mensajeError) throws IllegalArgumentException {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(mensajeError);
        }
        return valor;
    }

    private void manejarError(HttpServletRequest request, HttpServletResponse response, String mensaje, Exception e) throws ServletException, IOException {
        if (e != null) {
            logger.log(Level.SEVERE, mensaje, e);
        } else {
            logger.log(Level.WARNING, mensaje);
        }
        request.setAttribute("mensajeError", mensaje);
        request.getRequestDispatcher("error.jsp").forward(request, response);
    }

    private void verificarPermiso(HttpServletRequest request, HttpServletResponse response, String permisoRequerido, VerifiableAction action) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("error.jsp?error=sin-sesion");
            return;
        }

        // Obtener los permisos del usuario desde la sesión
        String usuarioCargo = (String) session.getAttribute("cargo");

        if (usuarioCargo == null || !usuarioCargo.equals(permisoRequerido)) {
            // Si el usuario no tiene el permiso requerido, redirige al error
            response.sendRedirect("error.jsp?error=acceso-denegado");
            return;
        }

        try {
            action.execute();
        } catch (Exception e) {
            manejarError(request, response, "Error al ejecutar la acción requerida.", e);
        }
    }

    private void verificarPermiso(HttpServletRequest request, HttpServletResponse response, String[] permisosRequeridos, VerifiableAction action) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("error.jsp?error=sin-sesion");
            return;
        }

        // Obtener los permisos del usuario desde la sesión
        String usuarioCargo = (String) session.getAttribute("cargo");

        boolean tienePermiso = false;
        for (String permiso : permisosRequeridos) {
            if (permiso.equals(usuarioCargo)) {
                tienePermiso = true;
                break;
            }
        }

        if (!tienePermiso) {
            // Si el usuario no tiene el permiso requerido, redirige al error
            response.sendRedirect("error.jsp?error=acceso-denegado");
            return;
        }

        try {
            action.execute();
        } catch (Exception e) {
            manejarError(request, response, "Error al ejecutar la acción requerida.", e);
        }
    }

    @FunctionalInterface
    interface VerifiableAction {
        void execute() throws Exception;
    }
}
