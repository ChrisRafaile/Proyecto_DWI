package pe.edu.utp.isi.dwi.proyecto_dwi.controller;

import pe.edu.utp.isi.dwi.proyecto_dwi.service.UsuarioFacade;
import pe.edu.utp.isi.dwi.proyecto_dwi.dto.UsuarioDTO;
import pe.edu.utp.isi.dwi.proyecto_dwi.util.SecurityUtils;
import pe.edu.utp.isi.dwi.proyecto_dwi.util.Roles;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pe.edu.utp.isi.dwi.proyecto_dwi.dao.LogAuditoriaDAO;
import pe.edu.utp.isi.dwi.proyecto_dwi.entities.LogAuditoria;
import pe.edu.utp.isi.dwi.proyecto_dwi.factory.DAOFactory;

@WebServlet(name = "UsuarioServlet", urlPatterns = {"/Usuario"})
public class UsuarioServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(UsuarioServlet.class.getName());
    private UsuarioFacade usuarioFacade;

    public UsuarioServlet() {
        try {
            usuarioFacade = new UsuarioFacade();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al inicializar UsuarioFacade", e);
            throw new RuntimeException("Error crítico al inicializar el UsuarioFacade.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");

        if (accion == null || "guardar".equals(accion)) {
            registrarUsuario(request, response);
        } else if ("actualizarPerfil".equals(accion)) {
            actualizarPerfilUsuario(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");

        switch (accion == null ? "listar" : accion) {
            case "listar" ->
                listarUsuarios(request, response);
            case "editar" ->
                verificarPermiso(request, response, Roles.CARGO_JEFE_DE_AREA, () -> mostrarFormularioEditar(request, response));
            case "eliminar" ->
                verificarPermiso(request, response, Roles.CARGO_JEFE_DE_AREA, () -> eliminarUsuario(request, response));
            default ->
                request.getRequestDispatcher("registro_usuario.jsp").forward(request, response);
        }
    }

    private void registrarUsuario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
        // Obtener la respuesta del reCAPTCHA
        String recaptchaResponse = request.getParameter("g-recaptcha-response");
        
        // Verificar si la respuesta del reCAPTCHA está presente
        if (recaptchaResponse == null || recaptchaResponse.trim().isEmpty()) {
            redirigirConMensaje(request, response, "No se recibió respuesta del reCAPTCHA. Por favor, intente nuevamente.", "registro_usuario.jsp");
            return;
        }

        // Validar reCAPTCHA
        if (!SecurityUtils.verificarReCaptcha(recaptchaResponse)) {
            redirigirConMensaje(request, response, "reCAPTCHA inválido. Por favor, intente nuevamente.", "registro_usuario.jsp");
            return;
        }

            // Obtener y validar los datos del formulario
            String nombre = request.getParameter("nombre_usuario");
            String correo = request.getParameter("correo");
            String contrasena = request.getParameter("contrasena");
            String confirmacion = request.getParameter("confirmacion");
            String direccion = request.getParameter("direccion");
            String telefono = request.getParameter("telefono");
            String apellidos = request.getParameter("apellidos");
            String fechaNacimiento = request.getParameter("fecha_nacimiento");
            String departamento = request.getParameter("departamento");
            String dni = request.getParameter("dni");
            String cargo = request.getParameter("cargo"); // Nuevo campo capturado

            // Validaciones de correo y contraseña
            if (nombre == null || correo == null || contrasena == null || confirmacion == null || cargo == null || cargo.isEmpty()) {
                redirigirConMensaje(request, response, "Todos los campos son requeridos.", "registro_usuario.jsp");
                return;
            }

            if (!correo.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                redirigirConMensaje(request, response, "Correo inválido.", "registro_usuario.jsp");
                return;
            }

            if (!contrasena.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{8,}$")) {
                redirigirConMensaje(request, response, "La contraseña debe tener al menos 8 caracteres, mayúsculas, minúsculas, números y caracteres especiales.", "registro_usuario.jsp");
                return;
            }

            if (!contrasena.equals(confirmacion)) {
                redirigirConMensaje(request, response, "Las contraseñas no coinciden.", "registro_usuario.jsp");
                return;
            }

            // Manejar la imagen de perfil
            Part filePart = request.getPart("foto_perfil");
            String fotoPerfil = "images/perfiles/default_profile.png";
            if (filePart != null && filePart.getSize() > 0) {
                // Obtener el nombre del archivo
                String fileName = "perfil_" + nombre + "_" + System.currentTimeMillis() + ".png";
                String uploadPath = getServletContext().getRealPath("") + "images/perfiles";

                // Crear el directorio si no existe
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs(); // Asegura la creación de la ruta completa
                }

                // Guardar la imagen en la carpeta
                filePart.write(uploadPath + File.separator + fileName);

                // Establecer la ruta relativa de la imagen para guardarla en la base de datos
                fotoPerfil = "images/perfiles/" + fileName;
            }

            // Crear el objeto UsuarioDTO y registrar el usuario
            UsuarioDTO usuarioDTO = new UsuarioDTO();
            usuarioDTO.setNombreUsuario(nombre);
            usuarioDTO.setCorreo(correo);
            String salt = SecurityUtils.generateSalt();
            String hashedPassword = SecurityUtils.hashPasswordWithSalt(contrasena, salt);
            usuarioDTO.setContrasena(hashedPassword);
            usuarioDTO.setSalt(salt);

            // Establecer los campos adicionales
            usuarioDTO.setDireccion(direccion);
            usuarioDTO.setTelefono(telefono);
            usuarioDTO.setApellidos(apellidos);
            usuarioDTO.setFechaNacimiento(fechaNacimiento);
            usuarioDTO.setFotoPerfil(fotoPerfil);
            usuarioDTO.setDepartamento(departamento);
            usuarioDTO.setDni(dni);

            // Registrar el usuario usando el método registrarUsuarioCompleto
            int idUsuario = usuarioFacade.registrarUsuarioCompleto(usuarioDTO, cargo);
            usuarioDTO.setIdUsuario(idUsuario);

            // Crear una nueva sesión para el usuario registrado
            HttpSession session = request.getSession();
            session.setAttribute("usuario", usuarioDTO);

            // Registrar log de auditoría para el registro de usuario exitoso
            registrarLogAuditoria(idUsuario, "Registro de usuario exitoso");

            // Redirigir al usuario a la página de registro exitoso
            response.sendRedirect("registro_exitoso.jsp");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error al registrar el usuario", e);
            redirigirConMensaje(request, response, "Error al registrar el usuario.", "error.jsp");
        }
    }

    private void actualizarPerfilUsuario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("usuario") == null) {
                request.setAttribute("mensajeError", "Su sesión ha expirado. Por favor, inicie sesión nuevamente.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }

            UsuarioDTO usuarioLogueado = (UsuarioDTO) session.getAttribute("usuario");

            // Obtener datos del formulario
            String nombre = request.getParameter("nombre_usuario");
            if (nombre != null && !nombre.isEmpty()) {
                usuarioLogueado.setNombreUsuario(nombre);
            }

            String apellidos = request.getParameter("apellidos");
            if (apellidos != null && !apellidos.isEmpty()) {
                usuarioLogueado.setApellidos(apellidos);
            }

            String correo = request.getParameter("correo");
            if (correo != null && !correo.isEmpty()) {
                usuarioLogueado.setCorreo(correo);
            }

            String direccion = request.getParameter("direccion");
            if (direccion != null && !direccion.isEmpty()) {
                usuarioLogueado.setDireccion(direccion);
            }

            String telefono = request.getParameter("telefono");
            if (telefono != null && !telefono.isEmpty()) {
                usuarioLogueado.setTelefono(telefono);
            }

            String fechaNacimiento = request.getParameter("fecha_nacimiento");
            if (fechaNacimiento != null && !fechaNacimiento.isEmpty()) {
                usuarioLogueado.setFechaNacimiento(fechaNacimiento);
            }

            // Manejar la imagen de perfil (opcional)
            Part filePart = request.getPart("foto_perfil");
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = "perfil_" + usuarioLogueado.getNombreUsuario() + "_" + System.currentTimeMillis() + ".png";
                String uploadPath = "C:\\Temp\\images";
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                filePart.write(uploadPath + File.separator + fileName);
                usuarioLogueado.setFotoPerfil("images/perfiles/" + fileName);
            }

            // Actualizar el usuario en la base de datos
            usuarioFacade.actualizarUsuario(usuarioLogueado);

            // Actualizar la sesión con el usuario modificado
            session.setAttribute("usuario", usuarioLogueado);

            // Redirigir con mensaje de éxito
            request.setAttribute("mensaje", "Perfil actualizado exitosamente.");
            request.getRequestDispatcher("perfil_usuario.jsp").forward(request, response);
        } catch (ServletException | IOException e) {
            logger.log(Level.SEVERE, "Error al actualizar el perfil del usuario", e);
            request.setAttribute("mensajeError", "Error al actualizar el perfil del usuario.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } catch (IllegalArgumentException e) {
            request.setAttribute("mensajeError", e.getMessage());
            response.sendRedirect("perfil_usuario.jsp");
        }
    }

    private void listarUsuarios(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<UsuarioDTO> usuarios = usuarioFacade.listarUsuarios();
            request.setAttribute("usuarios", usuarios);
            request.getRequestDispatcher("listar_usuarios.jsp").forward(request, response);
        } catch (IOException | ServletException e) {
            logger.log(Level.SEVERE, "Error al obtener la lista de usuarios", e);
            redirigirConMensaje(request, response, "Error al obtener la lista de usuarios.", "error.jsp");
        }
    }

    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idUsuario = Integer.parseInt(request.getParameter("id"));
        try {
            UsuarioDTO usuarioDTO = usuarioFacade.obtenerUsuarioPorId(idUsuario);
            request.setAttribute("usuario", usuarioDTO);
            request.getRequestDispatcher("editar_usuario.jsp").forward(request, response);

            // Registrar log de auditoría
            registrarLogAuditoria(idUsuario, "Acceso al formulario de edición de usuario");
        } catch (IOException | ServletException e) {
            logger.log(Level.SEVERE, "Error al obtener los datos del usuario", e);
            request.setAttribute("mensajeError", "Error al obtener los datos del usuario.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void eliminarUsuario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idUsuario = Integer.parseInt(request.getParameter("id"));
        HttpSession session = request.getSession(false);
        UsuarioDTO usuarioSolicitante = (UsuarioDTO) session.getAttribute("usuario");

        try {
            // Verificar si el usuario solicitante tiene el permiso necesario para eliminar usuarios
            if (usuarioSolicitante == null || !Roles.CARGO_JEFE_DE_AREA.equalsIgnoreCase(usuarioSolicitante.getColaborador().getCargo())) {
                // Si el usuario no tiene el rol adecuado, se envía un mensaje de error y se redirige al error.jsp
                request.setAttribute("mensajeError", "No tiene permiso para eliminar usuarios.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            // Si el usuario tiene permisos, proceder a eliminar el usuario
            usuarioFacade.eliminarUsuario(idUsuario, usuarioSolicitante);

            // Registrar log de auditoría para la eliminación de usuario
            registrarLogAuditoria(idUsuario, "Eliminación de usuario");

            // Redirigir al usuario a la lista de usuarios después de la eliminación
            response.sendRedirect("Usuario?accion=listar");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error al eliminar el usuario", e);
            request.setAttribute("mensajeError", "Error al eliminar el usuario.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void registrarLogAuditoria(int idUsuario, String accion) {
        try {
            LogAuditoriaDAO logAuditoriaDAO = DAOFactory.getInstance().getLogAuditoriaDAO();
            LogAuditoria logAuditoria = new LogAuditoria(idUsuario, accion, new Timestamp(System.currentTimeMillis()));
            logAuditoriaDAO.registrarLog(logAuditoria);
            logger.log(Level.INFO, "Log de auditoría registrado correctamente para el usuario con ID: {0}", idUsuario);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al registrar el log de auditoría", e);
        }
    }

    private void redirigirConMensaje(HttpServletRequest request, HttpServletResponse response, String mensaje, String paginaDestino) throws ServletException, IOException {
        request.setAttribute("mensajeError", mensaje);
        request.getRequestDispatcher(paginaDestino).forward(request, response);
    }

    private void verificarPermiso(HttpServletRequest request, HttpServletResponse response, String permisoRequerido, VerifiableAction action) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");

        if (usuario != null && usuario.tienePermiso(permisoRequerido)) {
            try {
                action.execute();
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error al ejecutar la acción", e);
                request.setAttribute("mensajeError", "Error al ejecutar la acción: " + e.getMessage());
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("mensajeError", "No tiene permiso para realizar esta acción.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    @FunctionalInterface
    interface VerifiableAction {

        void execute() throws Exception;
    }
}