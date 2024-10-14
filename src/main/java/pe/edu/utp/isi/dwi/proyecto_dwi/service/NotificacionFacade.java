package pe.edu.utp.isi.dwi.proyecto_dwi.service;

import pe.edu.utp.isi.dwi.proyecto_dwi.dao.NotificacionDAO;
import pe.edu.utp.isi.dwi.proyecto_dwi.dto.NotificacionDTO;
import pe.edu.utp.isi.dwi.proyecto_dwi.entities.Notificacion;
import pe.edu.utp.isi.dwi.proyecto_dwi.factory.DAOFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NotificacionFacade {

    private static final Logger logger = Logger.getLogger(NotificacionFacade.class.getName());
    private final NotificacionDAO notificacionDAO;

    public NotificacionFacade() {
        this.notificacionDAO = DAOFactory.getInstance().getNotificacionDAO();
    }

    // Listar notificaciones de un usuario por ID
    public List<NotificacionDTO> listarNotificaciones(int idUsuario) {
        try {
            List<Notificacion> notificaciones = notificacionDAO.obtenerNotificaciones(idUsuario);
            logger.log(Level.INFO, "Se listaron correctamente las notificaciones para el usuario con ID: {0}", idUsuario);
            return NotificacionDTO.fromEntities(notificaciones);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al listar las notificaciones para el usuario con ID: " + idUsuario, e);
            throw new RuntimeException("Error al listar las notificaciones", e);
        }
    }

    // Registrar una nueva notificación
    public void registrarNotificacion(NotificacionDTO notificacionDTO) {
        try {
            Notificacion notificacion = notificacionDTO.toEntity();
            notificacionDAO.registrarNotificacion(notificacion);
            logger.log(Level.INFO, "Notificación registrada correctamente: {0}", notificacionDTO);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al registrar la notificación", e);
            throw new RuntimeException("Error al registrar la notificación", e);
        }
    }

    // Marcar una notificación como leída
    public void marcarComoLeida(int id) {
        try {
            notificacionDAO.marcarNotificacionLeida(id);
            logger.log(Level.INFO, "Notificación marcada como leída con ID: {0}", id);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al marcar la notificación con ID: " + id + " como leída", e);
            throw new RuntimeException("Error al marcar la notificación como leída", e);
        }
    }

    // Contar las notificaciones no leídas de un usuario
    public int contarNotificacionesNoLeidas(int idUsuario) {
        try {
            int noLeidas = notificacionDAO.contarNotificacionesNoLeidas(idUsuario);
            logger.log(Level.INFO, "Total de notificaciones no leídas para el usuario con ID: {0}: {1}", new Object[]{idUsuario, noLeidas});
            return noLeidas;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al contar las notificaciones no leídas para el usuario con ID: " + idUsuario, e);
            throw new RuntimeException("Error al contar las notificaciones no leídas", e);
        }
    }

    // Eliminar una notificación por ID
    public void eliminarNotificacion(int id) {
        try {
            notificacionDAO.eliminarNotificacion(id);
            logger.log(Level.INFO, "Notificación eliminada con ID: {0}", id);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al eliminar la notificación con ID: " + id, e);
            throw new RuntimeException("Error al eliminar la notificación", e);
        }
    }
}
