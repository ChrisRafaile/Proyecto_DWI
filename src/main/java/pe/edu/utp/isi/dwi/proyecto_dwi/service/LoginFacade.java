package pe.edu.utp.isi.dwi.proyecto_dwi.service;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import pe.edu.utp.isi.dwi.proyecto_dwi.dao.UsuarioDAO;
import pe.edu.utp.isi.dwi.proyecto_dwi.dao.LogAuditoriaDAO;
import pe.edu.utp.isi.dwi.proyecto_dwi.factory.DAOFactory;
import pe.edu.utp.isi.dwi.proyecto_dwi.entities.LogAuditoria;
import pe.edu.utp.isi.dwi.proyecto_dwi.entities.Usuario;
import pe.edu.utp.isi.dwi.proyecto_dwi.util.SecurityUtils;

public class LoginFacade {

    private static final Logger logger = Logger.getLogger(LoginFacade.class.getName());
    private final UsuarioDAO usuarioDAO;
    private final LogAuditoriaDAO logAuditoriaDAO;

    // Constructor que inicializa el UsuarioDAO y LogAuditoriaDAO usando DAOFactory
    public LoginFacade() {
        DAOFactory daoFactory = DAOFactory.getInstance();
        this.usuarioDAO = daoFactory.getUsuarioDAO();
        this.logAuditoriaDAO = daoFactory.getLogAuditoriaDAO();
    }

    // Obtener usuario por correo
    public Usuario obtenerUsuarioPorCorreo(String correo) {
        try {
            return usuarioDAO.obtenerUsuarioPorCorreo(correo);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener el usuario por correo", e);
            throw new RuntimeException("Error al obtener el usuario por correo", e);
        }
    }

    // Validar credenciales
    public boolean validarCredenciales(String correo, String contrasenaIngresada) throws NoSuchAlgorithmException {
        try {
            Usuario usuario = usuarioDAO.obtenerUsuarioPorCorreo(correo);
            if (usuario != null) {
                // Obtener el salt y la contraseña almacenada
                String salt = usuario.getSalt();
                String storedHashedPassword = usuario.getContrasena();

                // Hashear la contraseña ingresada usando el salt
                String hashedPassword = SecurityUtils.hashPasswordWithSalt(contrasenaIngresada, salt);

                // Comparar el hash generado con el almacenado
                return hashedPassword.equals(storedHashedPassword);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al validar las credenciales del usuario", e);
            throw new RuntimeException("Error al validar las credenciales del usuario", e);
        }
        return false; // Si no se encuentra el usuario o la contraseña no coincide
    }

    // Registrar log de auditoría
    public void registrarLog(int idUsuario, String accion) {
        if (idUsuario <= 0) {
            throw new IllegalArgumentException("El ID del usuario debe ser un número positivo.");
        }
        try {
            LogAuditoria logAuditoria = new LogAuditoria();
            logAuditoria.setIdUsuario(idUsuario);
            logAuditoria.setAccion(accion);
            logAuditoria.setFecha(new Timestamp(System.currentTimeMillis()));
            logAuditoriaDAO.registrarLog(logAuditoria);
            logger.log(Level.INFO, "Log de auditoría registrado correctamente para el usuario con ID: {0}", idUsuario);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al registrar el log de auditoría", e);
            throw new RuntimeException("Error al registrar el log de auditoría", e);
        }
    }
}
