package pe.edu.utp.isi.dwi.proyecto_dwi.dao;

import pe.edu.utp.isi.dwi.proyecto_dwi.entities.Solicitud;
import pe.edu.utp.isi.dwi.proyecto_dwi.entities.Solicitud.EstadoSolicitud;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SolicitudDAO implements AutoCloseable {

    private final Connection connection;
    private static final Logger logger = Logger.getLogger(SolicitudDAO.class.getName());

    // Constructor que recibe la conexión desde DAOFactory
    public SolicitudDAO(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("La conexión a la base de datos no debe ser null.");
        }
        this.connection = connection;
    }

    // Método para registrar la solicitud usando Solicitud entidad
    public void registrarSolicitud(Solicitud solicitud) throws SQLException {
        String query = "INSERT INTO solicitudes (id_usuario, tipo_solicitud, descripcion, estado, fecha_creacion) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, solicitud.getIdUsuario());
            ps.setString(2, solicitud.getTipoSolicitud());
            ps.setString(3, solicitud.getDescripcion());
            ps.setString(4, solicitud.getEstado().toString());
            ps.setTimestamp(5, solicitud.getFechaCreacion());
            ps.executeUpdate();
            logger.log(Level.INFO, "Solicitud registrada correctamente");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al registrar la solicitud", e);
            throw e;
        }
    }

    // Método para cambiar el estado de una solicitud
    public void cambiarEstadoSolicitud(int idSolicitud, EstadoSolicitud nuevoEstado) throws SQLException {
        String query = "UPDATE solicitudes SET estado = ? WHERE id_solicitud = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, nuevoEstado.toString());
            ps.setInt(2, idSolicitud);
            ps.executeUpdate();
            logger.log(Level.INFO, "Estado de solicitud cambiado correctamente para ID: {0}", idSolicitud);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al cambiar el estado de la solicitud con ID: " + idSolicitud, e);
            throw e;
        }
    }

    // Método para actualizar una solicitud
    public void actualizarSolicitud(Solicitud solicitud) throws SQLException {
        String query = "UPDATE solicitudes SET id_usuario = ?, tipo_solicitud = ?, descripcion = ?, estado = ?, fecha_finalizacion = ? WHERE id_solicitud = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, solicitud.getIdUsuario());
            ps.setString(2, solicitud.getTipoSolicitud());
            ps.setString(3, solicitud.getDescripcion());
            ps.setString(4, solicitud.getEstado().toString());
            ps.setTimestamp(5, solicitud.getFechaFinalizacion());
            ps.setInt(6, solicitud.getIdSolicitud());
            ps.executeUpdate();
            logger.log(Level.INFO, "Solicitud actualizada correctamente con ID: {0}", solicitud.getIdSolicitud());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al actualizar la solicitud con ID: " + solicitud.getIdSolicitud(), e);
            throw e;
        }
    }

    // Método para eliminar una solicitud
    public void eliminarSolicitud(int idSolicitud) throws SQLException {
        String query = "DELETE FROM solicitudes WHERE id_solicitud = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idSolicitud);
            ps.executeUpdate();
            logger.log(Level.INFO, "Solicitud eliminada correctamente con ID: {0}", idSolicitud);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al eliminar la solicitud con ID: " + idSolicitud, e);
            throw e;
        }
    }

    // Método para filtrar solicitudes por rango de fechas
    public List<Solicitud> filtrarSolicitudesPorFecha(Timestamp fechaInicio, Timestamp fechaFin) throws SQLException {
        List<Solicitud> solicitudes = new ArrayList<>();
        String query = "SELECT * FROM solicitudes WHERE fecha_creacion BETWEEN ? AND ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setTimestamp(1, fechaInicio);
            ps.setTimestamp(2, fechaFin);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Solicitud solicitud = mapResultSetToSolicitud(rs);
                    solicitudes.add(solicitud);
                }
            }
            logger.log(Level.INFO, "Solicitudes filtradas correctamente entre {0} y {1}", new Object[]{fechaInicio, fechaFin});
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al filtrar las solicitudes por fecha", e);
            throw e;
        }
        return solicitudes;
    }

    // Método para obtener una solicitud por ID
    public Solicitud obtenerSolicitudPorId(int idSolicitud) throws SQLException {
        String query = "SELECT * FROM solicitudes WHERE id_solicitud = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idSolicitud);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Solicitud solicitud = mapResultSetToSolicitud(rs);
                    logger.log(Level.INFO, "Solicitud obtenida con ID: {0}", idSolicitud);
                    return solicitud;
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener la solicitud con ID: " + idSolicitud, e);
            throw e;
        }
        logger.log(Level.WARNING, "No se encontró la solicitud con ID: {0}", idSolicitud);
        return null;
    }

    // Método para listar todas las solicitudes
    public List<Solicitud> listarSolicitudes() throws SQLException {
        List<Solicitud> solicitudes = new ArrayList<>();
        String query = "SELECT * FROM solicitudes";
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Solicitud solicitud = mapResultSetToSolicitud(rs);
                solicitudes.add(solicitud);
            }
            logger.log(Level.INFO, "Solicitudes listadas correctamente");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al listar las solicitudes", e);
            throw e;
        }
        return solicitudes;
    }

    // Método para mapear el ResultSet a la entidad Solicitud
    private Solicitud mapResultSetToSolicitud(ResultSet rs) throws SQLException {
        Solicitud solicitud = new Solicitud();
        solicitud.setIdSolicitud(rs.getInt("id_solicitud"));
        solicitud.setIdUsuario(rs.getInt("id_usuario"));
        solicitud.setTipoSolicitud(rs.getString("tipo_solicitud"));
        solicitud.setDescripcion(rs.getString("descripcion"));
        solicitud.setEstado(EstadoSolicitud.valueOf(rs.getString("estado")));
        solicitud.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
        solicitud.setFechaFinalizacion(rs.getTimestamp("fecha_finalizacion"));
        return solicitud;
    }

    // Método para cerrar la conexión
    @Override
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            logger.log(Level.INFO, "Conexión cerrada correctamente por SolicitudDAO.");
        }
    }
}
