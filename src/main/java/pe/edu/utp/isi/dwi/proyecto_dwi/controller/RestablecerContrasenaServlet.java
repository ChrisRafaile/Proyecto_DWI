package pe.edu.utp.isi.dwi.proyecto_dwi.controller;

import pe.edu.utp.isi.dwi.proyecto_dwi.dao.UsuarioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import pe.edu.utp.isi.dwi.proyecto_dwi.util.SecurityUtils;
import pe.edu.utp.isi.dwi.proyecto_dwi.factory.DAOFactory;

@WebServlet(name = "RestablecerContraseñaServlet", urlPatterns = {"/RestablecerContrasenaServlet"})
public class RestablecerContrasenaServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(RestablecerContrasenaServlet.class.getName());
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{8,}$";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener los parámetros enviados desde el formulario
        String token = request.getParameter("token");
        String nuevaContraseña = request.getParameter("nuevaContraseña");
        String confirmarContraseña = request.getParameter("confirmarContraseña");

        try {
            // Validar las contraseñas
            if (!validarContraseñas(nuevaContraseña, confirmarContraseña, request, response)) {
                return; // Si la validación falla, se redirige desde el método de validación
            }

            // Obtener el DAO para la gestión de usuarios utilizando el Singleton de DAOFactory
            UsuarioDAO usuarioDAO = DAOFactory.getInstance().getUsuarioDAO();

            // Obtener el correo asociado con el token
            String correo = usuarioDAO.obtenerCorreoPorToken(token);

            // Si el token es válido, proceder a actualizar la contraseña
            if (correo != null) {
                actualizarContraseña(usuarioDAO, correo, nuevaContraseña, token, request, response);
            } else {
                mostrarMensaje(request, response, "El enlace de restablecimiento es inválido o ha expirado.", "restablecer_contrasena.jsp");
                logger.log(Level.WARNING, "Intento de restablecimiento con token inválido o expirado: {0}", token);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al restablecer la contraseña para el token: " + token, e);
            mostrarMensaje(request, response, "Error al procesar la solicitud. Por favor, inténtalo nuevamente.", "restablecer_contrasena.jsp");
        }
    }

    // Método para validar las contraseñas
    private boolean validarContraseñas(String nuevaContraseña, String confirmarContraseña, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Verificar si las contraseñas coinciden
        if (!nuevaContraseña.equals(confirmarContraseña)) {
            mostrarMensaje(request, response, "Las contraseñas no coinciden.", "restablecer_contrasena.jsp");
            return false;
        }

        // Verificar si la contraseña cumple con la complejidad requerida
        if (!nuevaContraseña.matches(PASSWORD_REGEX)) {
            mostrarMensaje(request, response, "La contraseña debe tener al menos 8 caracteres e incluir mayúsculas, minúsculas, números y caracteres especiales.", "restablecer_contrasena.jsp");
            return false;
        }

        return true;
    }

    // Método para actualizar la contraseña en la base de datos
    private void actualizarContraseña(UsuarioDAO usuarioDAO, String correo, String nuevaContraseña, String token, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        // Generar un nuevo salt y hashear la contraseña
        String salt = SecurityUtils.generateSalt();
        String hashedPassword = SecurityUtils.hashPasswordWithSalt(nuevaContraseña, salt);

        // Actualizar la contraseña y eliminar el token de restablecimiento
        usuarioDAO.actualizarContraseñaConSalt(correo, hashedPassword, salt);
        usuarioDAO.eliminarToken(token);

        // Mostrar mensaje de éxito y redirigir al login
        mostrarMensaje(request, response, "Contraseña actualizada correctamente. Por favor, inicia sesión.", "login.jsp");
        logger.log(Level.INFO, "Contraseña actualizada correctamente para el usuario con correo: {0}", correo);
    }

    // Método para mostrar un mensaje y redirigir a una página específica
    private void mostrarMensaje(HttpServletRequest request, HttpServletResponse response, String mensaje, String paginaDestino) throws ServletException, IOException {
        request.setAttribute("mensaje", mensaje);
        request.getRequestDispatcher(paginaDestino).forward(request, response);
    }
}
