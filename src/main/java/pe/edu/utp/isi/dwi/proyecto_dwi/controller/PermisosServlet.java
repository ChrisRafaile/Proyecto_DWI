package pe.edu.utp.isi.dwi.proyecto_dwi.controller;

import pe.edu.utp.isi.dwi.proyecto_dwi.dto.PermisoDTO;
import pe.edu.utp.isi.dwi.proyecto_dwi.service.PermisosFacade;
import pe.edu.utp.isi.dwi.proyecto_dwi.dto.UsuarioDTO;
import pe.edu.utp.isi.dwi.proyecto_dwi.util.Roles;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "PermisosServlet", urlPatterns = {"/Permisos"})
public class PermisosServlet extends HttpServlet {

    private PermisosFacade permisosFacade;
    private static final Logger logger = Logger.getLogger(PermisosServlet.class.getName());

    @Override
    public void init() throws ServletException {
        try {
            permisosFacade = new PermisosFacade();
        } catch (RuntimeException ex) {
            logger.log(Level.SEVERE, "Error al inicializar PermisosFacade", ex);
            throw new ServletException("Error al inicializar PermisosFacade", ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");

        if (!validarAcceso(request, accion)) {
            redirigirConMensaje(request, response, "No tienes permisos para realizar esta acción.", "error.jsp");
            return;
        }

        switch (accion) {
            case "guardar" -> asignarPermiso(request, response);
            case "eliminar" -> eliminarPermiso(request, response);
            case "editar" -> editarPermiso(request, response);
            default -> redirigirConMensaje(request, response, "Acción no soportada.", "error.jsp");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");

        if (accion == null) {
            redirigirConMensaje(request, response, "Acción no soportada.", "error.jsp");
        } else {
            switch (accion) {
                case "listar" -> listarPermisos(request, response);
                case "editar" -> mostrarFormularioEditar(request, response);
                default -> redirigirConMensaje(request, response, "Acción no soportada.", "error.jsp");
            }
        }
    }

    // Método para asignar un nuevo permiso
    private void asignarPermiso(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String nombrePermiso = request.getParameter("nombrePermiso");
            int idRol = Integer.parseInt(request.getParameter("idRol"));

            PermisoDTO permisoDTO = new PermisoDTO();
            permisoDTO.setPermiso(nombrePermiso);
            permisoDTO.setIdRol(idRol);

            permisosFacade.asignarPermiso(permisoDTO);
            response.sendRedirect("Permisos?accion=listar");
        } catch (NumberFormatException e) {
            manejarError(request, response, "ID de rol inválido.", e);
        } catch (IOException e) {
            manejarError(request, response, "Error al asignar el permiso.", e);
        }
    }

    // Método para mostrar el formulario de edición de permiso
    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int idPermiso = Integer.parseInt(request.getParameter("idPermiso"));
            PermisoDTO permiso = permisosFacade.obtenerPermisoPorId(idPermiso);
            if (permiso != null) {
                request.setAttribute("permiso", permiso);
                request.getRequestDispatcher("editar_permiso.jsp").forward(request, response);
            } else {
                manejarError(request, response, "Permiso no encontrado.", null);
            }
        } catch (NumberFormatException e) {
            manejarError(request, response, "ID de permiso inválido.", e);
        } catch (ServletException | IOException e) {
            manejarError(request, response, "Error al obtener el permiso.", e);
        }
    }

    // Método para editar un permiso
    private void editarPermiso(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int idPermiso = Integer.parseInt(request.getParameter("idPermiso"));
            String nombrePermiso = request.getParameter("nombrePermiso");
            int idRol = Integer.parseInt(request.getParameter("idRol"));

            PermisoDTO permisoDTO = new PermisoDTO();
            permisoDTO.setIdPermiso(idPermiso);
            permisoDTO.setPermiso(nombrePermiso);
            permisoDTO.setIdRol(idRol);

            permisosFacade.actualizarPermiso(permisoDTO);
            response.sendRedirect("Permisos?accion=listar");
        } catch (NumberFormatException e) {
            manejarError(request, response, "ID de permiso o rol inválido.", e);
        } catch (IOException e) {
            manejarError(request, response, "Error al actualizar el permiso.", e);
        }
    }

    // Método para eliminar un permiso
    private void eliminarPermiso(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int idPermiso = Integer.parseInt(request.getParameter("idPermiso"));
            permisosFacade.quitarPermiso(idPermiso);
            response.sendRedirect("Permisos?accion=listar");
        } catch (NumberFormatException e) {
            manejarError(request, response, "ID de permiso inválido.", e);
        } catch (IOException e) {
            manejarError(request, response, "Error al eliminar el permiso.", e);
        }
    }

    // Método para listar todos los permisos
    private void listarPermisos(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<PermisoDTO> permisos = permisosFacade.listarPermisos();
            request.setAttribute("permisos", permisos);
            request.getRequestDispatcher("listar_permisos.jsp").forward(request, response);
        } catch (RuntimeException e) {
            manejarError(request, response, "Error al listar los permisos: " + e.getMessage(), e);
        }
    }

    // Método auxiliar para redirigir con un mensaje específico
    private void redirigirConMensaje(HttpServletRequest request, HttpServletResponse response, String mensaje, String paginaDestino) throws ServletException, IOException {
        request.setAttribute("mensaje", mensaje);
        request.getRequestDispatcher(paginaDestino).forward(request, response);
    }

    // Método para validar si el usuario tiene acceso a realizar una acción específica
    private boolean validarAcceso(HttpServletRequest request, String accion) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }

        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");
        if (usuario == null || usuario.getColaborador() == null) {
            return false;
        }

        String cargo = usuario.getColaborador().getCargo();
        return switch (cargo) {
            case Roles.CARGO_JEFE_DE_AREA -> true; // El jefe de área tiene acceso a todas las acciones
            case Roles.CARGO_COORDINADOR, Roles.CARGO_ANALISTA_SENIOR -> "listar".equals(accion); // Coordinadores y analistas solo pueden listar
            case Roles.CARGO_PROGRAMADOR_JUNIOR -> false; // El programador junior no tiene acceso
            default -> false; // Por defecto, sin acceso
        };
    }

    // Método para manejar errores y redirigir a la página de error
    private void manejarError(HttpServletRequest request, HttpServletResponse response, String mensaje, Exception e) throws ServletException, IOException {
        if (e != null) {
            logger.log(Level.SEVERE, mensaje, e);
        } else {
            logger.log(Level.WARNING, mensaje);
        }
        request.setAttribute("mensajeError", mensaje);
        request.getRequestDispatcher("error.jsp").forward(request, response);
    }
}
