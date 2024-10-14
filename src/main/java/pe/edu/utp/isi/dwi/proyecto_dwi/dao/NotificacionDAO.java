package pe.edu.utp.isi.dwi.proyecto_dwi.dao;

import pe.edu.utp.isi.dwi.proyecto_dwi.entities.Notificacion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NotificacionDAO implements AutoCloseable {

    private static final Logger logger = Logger.getLogger(NotificacionDAO.class.getName());
    private final Connection connection;

    // Constructor que recibe la conexión desde DAOFactory
    public NotificacionDAO(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("La conexión a la base de datos no debe ser null.");
        }
        this.connection = connection;
    }

    // Método para registrar una nueva notificación
    public void registrarNotificacion(Notificacion notificacion) throws SQLException {
        String query = "INSERT INTO notificaciones (id_solicitud, fecha_envio, tipo, leida) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, notificacion.getIdSolicitud());
            ps.setTimestamp(2, notificacion.getFechaEnvio());
            ps.setString(3, notificacion.getTipo());
            ps.setBoolean(4, notificacion.isLeida());
            ps.executeUpdate();
            logger.log(Level.INFO, "Notificación registrada correctamente.");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al registrar la notificación", e);
            throw e;
        }
    }

    // Método para obtener todas las notificaciones de un usuario
    public List<Notificacion> obtenerNotificaciones(int idUsuario) throws SQLException {
        List<Notificacion> notificaciones = new ArrayList<>();
        String query = "SELECT * FROM notificaciones WHERE id_solicitud IN (SELECT id_solicitud FROM solicitudes WHERE id_usuario = ?) ORDER BY fecha_envio DESC";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Notificacion notificacion = new Notificacion();
                    notificacion.setIdNotificacion(rs.getInt("id_notificacion"));
                    notificacion.setIdSolicitud(rs.getInt("id_solicitud"));
                    notificacion.setFechaEnvio(rs.getTimestamp("fecha_envio"));
                    notificacion.setTipo(rs.getString("tipo"));
                    notificacion.setLeida(rs.getBoolean("leida"));
                    notificaciones.add(notificacion);
                }
            }
            logger.log(Level.INFO, "Notificaciones obtenidas correctamente.");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener las notificaciones", e);
            throw e;
        }
        return notificaciones;
    }

    // Método para contar el número de notificaciones no leídas de un usuario
    public int contarNotificacionesNoLeidas(int idUsuario) throws SQLException {
        String query = "SELECT COUNT(*) FROM notificaciones WHERE id_solicitud IN (SELECT id_solicitud FROM solicitudes WHERE id_usuario = ?) AND leida = false";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al contar las notificaciones no leídas", e);
            throw e;
        }
        return 0;
    }

    // Método para marcar una notificación como leída
    public void marcarNotificacionLeida(int idNotificacion) throws SQLException {
        String query = "UPDATE notificaciones SET leida = true WHERE id_notificacion = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idNotificacion);
            ps.executeUpdate();
            logger.log(Level.INFO, "Notificación marcada como leída.");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al marcar la notificación como leída", e);
            throw e;
        }
    }

    // Método para eliminar una notificación por ID
    public void eliminarNotificacion(int idNotificacion) throws SQLException {
        String query = "DELETE FROM notificaciones WHERE id_notificacion = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idNotificacion);
            ps.executeUpdate();
            logger.log(Level.INFO, "Notificación eliminada correctamente con ID: {0}", idNotificacion);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al eliminar la notificación con ID: " + idNotificacion, e);
            throw e;
        }
    }

    // Implementación del método close para AutoCloseable
    @Override
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            logger.log(Level.INFO, "Conexión cerrada correctamente por NotificacionDAO.");
        }
    }
}
