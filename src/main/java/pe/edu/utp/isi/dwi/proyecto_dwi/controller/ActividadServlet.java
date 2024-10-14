package pe.edu.utp.isi.dwi.proyecto_dwi.controller;

import pe.edu.utp.isi.dwi.proyecto_dwi.service.ActividadFacade;
import pe.edu.utp.isi.dwi.proyecto_dwi.dto.ActividadDTO;
import pe.edu.utp.isi.dwi.proyecto_dwi.dto.UsuarioDTO;
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

@WebServlet(name = "ActividadServlet", urlPatterns = {"/Actividad"})
public class ActividadServlet extends HttpServlet {

    private ActividadFacade actividadFacade;
    private static final Logger logger = Logger.getLogger(ActividadServlet.class.getName());

    @Override
    public void init() throws ServletException {
        try {
            actividadFacade = new ActividadFacade();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al inicializar el facade de actividad", e);
            throw new ServletException("Error al inicializar el facade de actividad", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion").trim();

        if (!verificarPermiso(request, response, new String[]{Roles.CARGO_JEFE_DE_AREA, Roles.CARGO_COORDINADOR})) {
            return;
        }

        switch (accion) {
            case "guardar" ->
                registrarActividad(request, response);
            case "actualizar" ->
                actualizarActividad(request, response);
            case "eliminar" ->
                eliminarActividad(request, response);
            default ->
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion").trim();

        if (!verificarPermiso(request, response, new String[]{Roles.CARGO_JEFE_DE_AREA, Roles.CARGO_COORDINADOR, Roles.CARGO_ANALISTA_SENIOR, Roles.CARGO_PROGRAMADOR_JUNIOR})) {
            return;
        }

        switch (accion) {
            case "listar" ->
                listarActividades(request, response);
            case "editar" ->
                mostrarFormularioEditar(request, response);
            default ->
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida.");
        }
    }

    private void registrarActividad(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            ActividadDTO actividadDTO = new ActividadDTO();
            actividadDTO.setIdAsignacion(Integer.parseInt(request.getParameter("idAsignacion").trim()));
            actividadDTO.setDescripcionActividad(request.getParameter("descripcionActividad").trim());
            actividadDTO.setTiempoEmpleado(Integer.parseInt(request.getParameter("tiempoEmpleado").trim()));
            actividadDTO.setFechaRegistro(LocalDateTime.now());

            actividadFacade.registrarActividad(actividadDTO);
            response.sendRedirect("Actividad?accion=listar");
        } catch (NumberFormatException e) {
            logger.log(Level.SEVERE, "Error en el formato de los parámetros", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Datos inválidos.");
        }
    }

    private void actualizarActividad(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int idActividad = Integer.parseInt(request.getParameter("idActividad").trim());
            ActividadDTO actividadDTO = new ActividadDTO();
            actividadDTO.setIdActividad(idActividad);
            actividadDTO.setIdAsignacion(Integer.parseInt(request.getParameter("idAsignacion").trim()));
            actividadDTO.setDescripcionActividad(request.getParameter("descripcionActividad").trim());
            actividadDTO.setTiempoEmpleado(Integer.parseInt(request.getParameter("tiempoEmpleado").trim()));
            actividadDTO.setFechaRegistro(LocalDateTime.now());

            actividadFacade.actualizarActividad(actividadDTO);
            response.sendRedirect("Actividad?accion=listar");
        } catch (NumberFormatException e) {
            logger.log(Level.SEVERE, "Error en el formato de los parámetros", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Datos inválidos.");
        }
    }

    private void eliminarActividad(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int idActividad = Integer.parseInt(request.getParameter("idActividad").trim());
            actividadFacade.eliminarActividad(idActividad);
            response.sendRedirect("Actividad?accion=listar");
        } catch (NumberFormatException e) {
            logger.log(Level.SEVERE, "Error en el formato del ID de la actividad", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido.");
        }
    }

    private void listarActividades(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<ActividadDTO> actividades = actividadFacade.listarActividades();
            request.setAttribute("actividades", actividades);
            request.getRequestDispatcher("listar_actividades.jsp").forward(request, response);
        } catch (ServletException | IOException e) {
            logger.log(Level.SEVERE, "Error al listar las actividades", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al listar las actividades.");
        }
    }

    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int idActividad = Integer.parseInt(request.getParameter("idActividad").trim());
            ActividadDTO actividadDTO = actividadFacade.obtenerActividadPorId(idActividad);
            request.setAttribute("actividad", actividadDTO);
            request.getRequestDispatcher("editar_actividad.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            logger.log(Level.SEVERE, "Error en el formato del ID de la actividad", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido.");
        }
    }

    private boolean verificarPermiso(HttpServletRequest request, HttpServletResponse response, String[] permisosRequeridos) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("login.jsp");
            return false;
        }

        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");
        if (usuario == null || usuario.getColaborador() == null) {
            response.sendRedirect("login.jsp");
            return false;
        }

        String cargo = usuario.getColaborador().getCargo();
        for (String permiso : permisosRequeridos) {
            if (permiso.equalsIgnoreCase(cargo.trim())) {
                return true;
            }
        }

        response.sendRedirect("error.jsp?error=sin-permiso");
        return false;
    }
}
