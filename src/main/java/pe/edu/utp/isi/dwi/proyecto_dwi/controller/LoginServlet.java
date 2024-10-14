package pe.edu.utp.isi.dwi.proyecto_dwi.controller;

import pe.edu.utp.isi.dwi.proyecto_dwi.entities.Usuario;
import pe.edu.utp.isi.dwi.proyecto_dwi.util.SecurityUtils;
import pe.edu.utp.isi.dwi.proyecto_dwi.util.Roles;
import pe.edu.utp.isi.dwi.proyecto_dwi.service.LoginFacade;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import pe.edu.utp.isi.dwi.proyecto_dwi.dao.LogAuditoriaDAO;
import pe.edu.utp.isi.dwi.proyecto_dwi.dto.UsuarioDTO;
import pe.edu.utp.isi.dwi.proyecto_dwi.entities.LogAuditoria;
import pe.edu.utp.isi.dwi.proyecto_dwi.factory.DAOFactory;

@WebServlet(name = "LoginServlet", urlPatterns = {"/Login"})
public class LoginServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(LoginServlet.class.getName());
    private final LoginFacade loginFacade = new LoginFacade();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String correo = request.getParameter("correo") != null ? request.getParameter("correo").trim() : null;
    String contrasenaIngresada = request.getParameter("contraseña") != null ? request.getParameter("contraseña").trim() : null;
    String recaptchaResponse = request.getParameter("g-recaptcha-response");

    if (esCampoVacio(correo) || esCampoVacio(contrasenaIngresada)) {
        redirigirConMensaje(request, response, "Debe ingresar un correo y contraseña.", "login.jsp");
        return;
    }

    if (recaptchaResponse == null || !SecurityUtils.verificarReCaptcha(recaptchaResponse)) {
        redirigirConMensaje(request, response, "reCAPTCHA inválido. Por favor, intente nuevamente.", "login.jsp");
        return;
    }

    try {
        Usuario usuario = loginFacade.obtenerUsuarioPorCorreo(correo);
        if (usuario != null) {
            boolean credencialesValidas = loginFacade.validarCredenciales(correo, contrasenaIngresada);
            if (credencialesValidas) {
                // Convertir Usuario a UsuarioDTO y almacenar en sesión
                UsuarioDTO usuarioDTO = UsuarioDTO.fromEntity(usuario);
                HttpSession session = request.getSession();
                session.setAttribute("usuario", usuarioDTO);

                registrarLogAuditoria(usuario.getIdUsuario(), "Inicio de sesión exitoso");
                redirigirPorCargo(usuarioDTO, response);
            } else {
                redirigirConMensaje(request, response, "Correo o contraseña incorrectos.", "login.jsp");
            }
        } else {
            redirigirConMensaje(request, response, "Correo o contraseña incorrectos.", "login.jsp");
        }
    } catch (NoSuchAlgorithmException e) {
        logger.log(Level.SEVERE, "Error al encriptar la contraseña.", e);
        redirigirConMensaje(request, response, "Error al procesar la solicitud. Inténtelo más tarde.", "login.jsp");
    } catch (IOException | ServletException e) {
        logger.log(Level.SEVERE, "Error inesperado durante el proceso de login.", e);
        redirigirConMensaje(request, response, "Ha ocurrido un error inesperado. Inténtelo más tarde.", "login.jsp");
    }
}

    // Método para verificar si un campo está vacío
    private boolean esCampoVacio(String campo) {
        return campo == null || campo.isEmpty();
    }

    // Método para manejar redirecciones con mensajes de error
    private void redirigirConMensaje(HttpServletRequest request, HttpServletResponse response, String mensaje, String paginaDestino) throws ServletException, IOException {
        request.setAttribute("mensajeError", mensaje);
        request.getRequestDispatcher(paginaDestino).forward(request, response);
    }

    // Método para redirigir según el cargo del usuario (ahora usando UsuarioDTO)
private void redirigirPorCargo(UsuarioDTO usuarioDTO, HttpServletResponse response) throws IOException {
    if (usuarioDTO.getColaborador() != null && usuarioDTO.getColaborador().getCargo() != null) {
        String cargo = usuarioDTO.getColaborador().getCargo().trim();
        switch (cargo) {
            case Roles.CARGO_JEFE_DE_AREA -> response.sendRedirect("gestor_area.jsp");
            case Roles.CARGO_COORDINADOR -> response.sendRedirect("gestor_coordinador.jsp");
            case Roles.CARGO_ANALISTA_SENIOR -> response.sendRedirect("gestor_analista.jsp");
            case Roles.CARGO_PROGRAMADOR_JUNIOR -> response.sendRedirect("gestor_programador.jsp");
            default -> response.sendRedirect("index.jsp");
        }
    } else {
        response.sendRedirect("index.jsp");
    }
}


    // Método para registrar un log de auditoría
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
}
