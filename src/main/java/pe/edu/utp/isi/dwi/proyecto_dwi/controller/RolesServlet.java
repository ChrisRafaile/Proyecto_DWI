package pe.edu.utp.isi.dwi.proyecto_dwi.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import pe.edu.utp.isi.dwi.proyecto_dwi.dto.RolDTO;
import pe.edu.utp.isi.dwi.proyecto_dwi.service.RolesFacade;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "RolesServlet", urlPatterns = {"/roles"})
public class RolesServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(RolesServlet.class.getName());
    private final RolesFacade rolesFacade = new RolesFacade();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Verificar sesión válida
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("idUsuario") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String accion = request.getParameter("accion");
        try {
            switch (accion == null ? "listar" : accion) {
                case "listar" -> listarRoles(request, response);
                case "asignar" -> mostrarFormularioAsignar(request, response);
                case "editar" -> mostrarFormularioEditar(request, response);
                default -> listarRoles(request, response);
            }
        } catch (ServletException | IOException e) {
            manejarError(request, response, "Error al manejar la operación de roles.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");
        try {
            switch (accion == null ? "" : accion) {
                case "asignar" -> asignarRolAUsuario(request, response);
                case "editar" -> editarRol(request, response);
                case "eliminar" -> eliminarRol(request, response);
                default -> listarRoles(request, response);
            }
        } catch (ServletException | IOException e) {
            manejarError(request, response, "Error al procesar la solicitud de roles.", e);
        }
    }

    // Método para listar roles
    private void listarRoles(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<RolDTO> roles = rolesFacade.listarRoles();
            request.setAttribute("roles", roles);
            request.getRequestDispatcher("listar_roles.jsp").forward(request, response);
        } catch (ServletException | IOException | SQLException e) {
            manejarError(request, response, "Error al listar los roles.", e);
        }
    }

    // Método para mostrar el formulario de asignación de rol
    private void mostrarFormularioAsignar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("asignar_rol.jsp").forward(request, response);
    }

    // Método para asignar un rol a un usuario
    private void asignarRolAUsuario(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
            int idRol = Integer.parseInt(request.getParameter("idRol"));
            rolesFacade.asignarRolAUsuario(idUsuario, idRol);
            response.sendRedirect("roles?accion=listar");
        } catch (NumberFormatException e) {
            manejarError(request, response, "ID de usuario o rol inválido.", e);
        } catch (IOException | SQLException e) {
            manejarError(request, response, "Error al asignar rol al usuario.", e);
        }
    }

    // Método para mostrar el formulario de edición de rol
    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int idRol = Integer.parseInt(request.getParameter("idRol"));
            RolDTO rol = rolesFacade.obtenerRolPorId(idRol);
            if (rol != null) {
                request.setAttribute("rol", rol);
                request.getRequestDispatcher("editar_rol.jsp").forward(request, response);
            } else {
                manejarError(request, response, "Rol no encontrado.", null);
            }
        } catch (NumberFormatException e) {
            manejarError(request, response, "ID de rol inválido.", e);
        } catch (ServletException | IOException | SQLException e) {
            manejarError(request, response, "Error al obtener el rol.", e);
        }
    }

    // Método para editar un rol
    private void editarRol(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int idRol = Integer.parseInt(request.getParameter("idRol"));
            String nombreRol = request.getParameter("nombreRol");
            String descripcion = request.getParameter("descripcion");

            RolDTO rolDTO = new RolDTO();
            rolDTO.setIdRol(idRol);
            rolDTO.setNombreRol(nombreRol);
            rolDTO.setDescripcion(descripcion);

            rolesFacade.actualizarRol(rolDTO);
            response.sendRedirect("roles?accion=listar");
        } catch (NumberFormatException e) {
            manejarError(request, response, "ID de rol inválido.", e);
        } catch (IOException | SQLException e) {
            manejarError(request, response, "Error al actualizar el rol.", e);
        }
    }

    // Método para eliminar un rol
    private void eliminarRol(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            int idRol = Integer.parseInt(request.getParameter("idRol"));
            rolesFacade.eliminarRol(idRol);
            response.sendRedirect("roles?accion=listar");
        } catch (NumberFormatException e) {
            manejarError(request, response, "ID de rol inválido.", e);
        } catch (IOException | SQLException e) {
            manejarError(request, response, "Error al eliminar el rol.", e);
        }
    }

    // Método auxiliar para manejar errores y redirigir a la página de error
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
