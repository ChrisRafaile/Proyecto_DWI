package pe.edu.utp.isi.dwi.proyecto_dwi.service;

import pe.edu.utp.isi.dwi.proyecto_dwi.dao.UsuarioDAO;
import pe.edu.utp.isi.dwi.proyecto_dwi.factory.DAOFactory;
import pe.edu.utp.isi.dwi.proyecto_dwi.util.SecurityUtils;

import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RestablecerContrasenaFacade {

    private final UsuarioDAO usuarioDAO;
    private static final Logger logger = Logger.getLogger(RestablecerContrasenaFacade.class.getName());

    // Constructor que inicializa el UsuarioDAO utilizando DAOFactory
    public RestablecerContrasenaFacade() {
        DAOFactory daoFactory = DAOFactory.getInstance();
        this.usuarioDAO = daoFactory.getUsuarioDAO();
    }

    // Método para generar un token de restablecimiento para un usuario basado en su correo
    public String generarTokenRestablecimiento(String correo) {
        try {
            // Primero, obtener el ID del usuario usando su correo
            int idUsuario = usuarioDAO.obtenerIdPorCorreo(correo);

            if (idUsuario > 0) {
                // Generar un token único para el restablecimiento
                String token = UUID.randomUUID().toString();
                // Guardar el token en la base de datos
                usuarioDAO.guardarTokenRestablecimiento(idUsuario, token);
                logger.log(Level.INFO, "Token de restablecimiento generado y guardado para el usuario con correo: {0}", correo);
                return token;
            } else {
                logger.log(Level.WARNING, "El correo proporcionado no pertenece a ningún usuario registrado: {0}", correo);
                return null;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al generar token de restablecimiento para el correo: " + correo, e);
            throw new RuntimeException("Error al generar token de restablecimiento", e);
        }
    }

    // Método para restablecer la contraseña usando un token
    public void restablecerContraseña(String token, String nuevaContraseña) {
        try {
            // Obtener el correo asociado al token
            String correo = usuarioDAO.obtenerCorreoPorToken(token);

            if (correo != null) {
                // Generar un nuevo salt y hashear la nueva contraseña
                String salt = SecurityUtils.generateSalt();
                String hashedPassword = SecurityUtils.hashPasswordWithSalt(nuevaContraseña, salt);

                // Actualizar la contraseña del usuario y eliminar el token de restablecimiento
                usuarioDAO.actualizarContraseñaConSalt(correo, hashedPassword, salt);
                usuarioDAO.eliminarToken(token);

                logger.log(Level.INFO, "Contraseña restablecida exitosamente para el usuario con correo: {0}", correo);
            } else {
                logger.log(Level.WARNING, "El token de restablecimiento es inválido o ha expirado: {0}", token);
                throw new RuntimeException("El token de restablecimiento es inválido o ha expirado.");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al restablecer la contraseña usando el token: " + token, e);
            throw new RuntimeException("Error al restablecer la contraseña", e);
        }
    }
}
