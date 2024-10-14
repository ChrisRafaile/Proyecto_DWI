package pe.edu.utp.isi.dwi.proyecto_dwi.controller;

import pe.edu.utp.isi.dwi.proyecto_dwi.dto.AsignacionDTO;
import pe.edu.utp.isi.dwi.proyecto_dwi.dto.UsuarioDTO;
import pe.edu.utp.isi.dwi.proyecto_dwi.service.AsignacionFacade;
import pe.edu.utp.isi.dwi.proyecto_dwi.util.Roles;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "AsignacionServlet", urlPatterns = {"/Asignacion"})
public class AsignacionServlet extends HttpServlet {

    private AsignacionFacade asignacionFacade;
    private static final Logger logger = Logger.getLogger(AsignacionServlet.class.getName());

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            // Inicializa el Facade, utilizando DAOFactory con Singleton
            asignacionFacade = new AsignacionFacade();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error initializing AsignacionFacade", ex);
            throw new ServletException("Unable to initialize AsignacionFacade", ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");
        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String accion = request.getParameter("accion");

        switch (accion) {
            case "guardar" ->
                verificarPermiso(usuario, response, new String[]{Roles.CARGO_JEFE_DE_AREA, Roles.CARGO_COORDINADOR}, () -> registrarAsignacion(request, response));
            case "eliminar" ->
                verificarPermiso(usuario, response, new String[]{Roles.CARGO_JEFE_DE_AREA}, () -> eliminarAsignacion(request, response));
            case "asignar" ->
                verificarPermiso(usuario, response, new String[]{Roles.CARGO_COORDINADOR}, () -> asignarColaboradorDesdeDashboard(request, response));
            default ->
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String accion = request.getParameter("accion");

        if ("listar".equals(accion)) {
            listarAsignaciones(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida.");
        }
    }

    // Método para registrar una nueva asignación
    private void registrarAsignacion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int idSolicitud = Integer.parseInt(request.getParameter("idSolicitud").trim());
            int idColaborador = Integer.parseInt(request.getParameter("idColaborador").trim());
            int idCoordinador = Integer.parseInt(request.getParameter("idCoordinador").trim());

            // Validar que los IDs sean positivos
            if (idSolicitud <= 0 || idColaborador <= 0 || idCoordinador <= 0) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Los IDs deben ser positivos.");
                return;
            }

            AsignacionDTO asignacionDTO = new AsignacionDTO();
            asignacionDTO.setIdSolicitud(idSolicitud);
            asignacionDTO.setIdColaborador(idColaborador);
            asignacionDTO.setIdCoordinador(idCoordinador);
            asignacionDTO.setFechaAsignacion(LocalDateTime.now());

            asignacionFacade.registrarAsignacion(asignacionDTO);
            logger.log(Level.INFO, "Asignación registrada exitosamente para la solicitud ID {0} y colaborador ID {1}", new Object[]{idSolicitud, idColaborador});
            response.sendRedirect("Asignacion?accion=listar&mensaje=Asignación registrada exitosamente");
        } catch (NumberFormatException e) {
            logger.log(Level.SEVERE, "Error en el formato de los parámetros", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parámetros inválidos. Verifique los datos ingresados.");
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Error al procesar la solicitud", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al procesar la solicitud.");
        }
    }

    // Método para la asignación rápida desde el dashboard
    private void asignarColaboradorDesdeDashboard(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        registrarAsignacion(request, response);
    }

    // Método para listar todas las asignaciones
    private void listarAsignaciones(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<AsignacionDTO> asignaciones = asignacionFacade.listarAsignaciones();
            request.setAttribute("asignaciones", asignaciones);
            request.getRequestDispatcher("listar_asignaciones.jsp").forward(request, response);
        } catch (ServletException | IOException e) {
            manejarError(e, "Error al listar las asignaciones.", request, response);
        }
    }

    // Método para eliminar una asignación
    private void eliminarAsignacion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int idAsignacion = Integer.parseInt(request.getParameter("idAsignacion").trim());

            // Validar que el ID sea positivo
            if (idAsignacion <= 0) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "El ID de la asignación debe ser positivo.");
                return;
            }

            asignacionFacade.eliminarAsignacion(idAsignacion);
            logger.log(Level.INFO, "Asignación con ID {0} eliminada exitosamente.", idAsignacion);
            response.sendRedirect("Asignacion?accion=listar&mensaje=Asignación eliminada exitosamente");
        } catch (NumberFormatException e) {
            logger.log(Level.SEVERE, "Error en el formato del ID de la asignación", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de la asignación inválido.");
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Error al procesar la solicitud", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al procesar la solicitud.");
        }
    }

    // Método para verificar permisos del usuario
    private void verificarPermiso(UsuarioDTO usuario, HttpServletResponse response, String[] permisosRequeridos, VerifiableAction action) throws ServletException, IOException {
        for (String permiso : permisosRequeridos) {
            if (usuario.getColaborador() != null && permiso.equalsIgnoreCase(usuario.getColaborador().getCargo().trim())) {
                try {
                    action.execute();
                    return;
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Error al ejecutar la acción", e);
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al ejecutar la acción.");
                    return;
                }
            }
        }
        response.sendRedirect("error.jsp?error=sin-permiso");
    }

    @FunctionalInterface
    interface VerifiableAction {
        void execute() throws Exception;
    }

    private void manejarError(Exception e, String mensaje, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.log(Level.SEVERE, mensaje, e);
        request.setAttribute("mensajeError", mensaje);
        request.getRequestDispatcher("error.jsp").forward(request, response);
    }
}
