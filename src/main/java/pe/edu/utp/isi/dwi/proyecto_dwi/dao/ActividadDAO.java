package pe.edu.utp.isi.dwi.proyecto_dwi.dao;

import pe.edu.utp.isi.dwi.proyecto_dwi.dto.ActividadDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ActividadDAO implements AutoCloseable {

    private static final Logger logger = Logger.getLogger(ActividadDAO.class.getName());
    private final Connection connection;

    // Constructor que recibe la conexión desde DAOFactory
    public ActividadDAO(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("La conexión proporcionada no puede ser null.");
        }
        this.connection = connection;
    }

    // Método para verificar la conexión
    private void verificarConexion() throws SQLException {
        if (connection == null || connection.isClosed()) {
            throw new SQLException("Conexión a la base de datos no disponible.");
        }
    }

    // Método para registrar una nueva actividad con recuperación de ID generado
    public void registrarActividad(ActividadDTO actividadDTO) throws SQLException {
        verificarConexion();
        String query = "INSERT INTO actividades (id_asignacion, descripcion_actividad, tiempo_empleado, fecha_registro) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, actividadDTO.getIdAsignacion());
            ps.setString(2, actividadDTO.getDescripcionActividad());
            ps.setInt(3, actividadDTO.getTiempoEmpleado());
            ps.setTimestamp(4, Timestamp.valueOf(actividadDTO.getFechaRegistro()));
            ps.executeUpdate();

            // Obtener el ID generado automáticamente
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    actividadDTO.setIdActividad(generatedKeys.getInt(1));  // Asignar el ID generado al DTO
                }
            }
            logger.log(Level.INFO, "Actividad registrada correctamente con ID: {0}", actividadDTO.getIdActividad());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al registrar la actividad", e);
            throw e;
        }
    }

    // Método para obtener todas las actividades
    public List<ActividadDTO> obtenerActividades() throws SQLException {
        verificarConexion();
        List<ActividadDTO> actividades = new ArrayList<>();
        String query = "SELECT * FROM actividades ORDER BY fecha_registro DESC";

        try (PreparedStatement ps = connection.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                actividades.add(convertirResultSetADTO(rs));
            }
            logger.log(Level.INFO, "Actividades obtenidas correctamente.");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener las actividades", e);
            throw e;
        }
        return actividades;
    }

    // Método para obtener una actividad por ID
    public ActividadDTO obtenerActividadPorId(int id) throws SQLException {
        verificarConexion();
        ActividadDTO actividadDTO = null;
        String query = "SELECT * FROM actividades WHERE id_actividad = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    actividadDTO = convertirResultSetADTO(rs);
                }
            }
            if (actividadDTO != null) {
                logger.log(Level.INFO, "Actividad obtenida correctamente con ID: {0}", id);
            } else {
                logger.log(Level.WARNING, "No se encontró la actividad con el ID: {0}", id);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener la actividad por ID", e);
            throw e;
        }
        return actividadDTO;
    }

    // Método para actualizar una actividad
    public void actualizarActividad(ActividadDTO actividadDTO) throws SQLException {
        verificarConexion();
        String query = "UPDATE actividades SET id_asignacion = ?, descripcion_actividad = ?, tiempo_empleado = ?, fecha_registro = ? WHERE id_actividad = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, actividadDTO.getIdAsignacion());
            ps.setString(2, actividadDTO.getDescripcionActividad());
            ps.setInt(3, actividadDTO.getTiempoEmpleado());
            ps.setTimestamp(4, Timestamp.valueOf(actividadDTO.getFechaRegistro()));
            ps.setInt(5, actividadDTO.getIdActividad());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                logger.log(Level.INFO, "Actividad actualizada correctamente con ID: {0}", actividadDTO.getIdActividad());
            } else {
                logger.log(Level.WARNING, "No se encontró la actividad con el ID: {0}", actividadDTO.getIdActividad());
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al actualizar la actividad con ID: " + actividadDTO.getIdActividad(), e);
            throw e;
        }
    }

    // Método para eliminar una actividad
    public void eliminarActividad(int id) throws SQLException {
        verificarConexion();
        String query = "DELETE FROM actividades WHERE id_actividad = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                logger.log(Level.INFO, "Actividad eliminada correctamente con ID: {0}", id);
            } else {
                logger.log(Level.WARNING, "No se encontró la actividad con el ID: {0}", id);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al eliminar la actividad con ID: " + id, e);
            throw e;
        }
    }

    // Método para convertir ResultSet a ActividadDTO
    private ActividadDTO convertirResultSetADTO(ResultSet rs) throws SQLException {
        ActividadDTO actividadDTO = new ActividadDTO();
        actividadDTO.setIdActividad(rs.getInt("id_actividad"));
        actividadDTO.setIdAsignacion(rs.getInt("id_asignacion"));
        actividadDTO.setDescripcionActividad(rs.getString("descripcion_actividad"));
        actividadDTO.setTiempoEmpleado(rs.getInt("tiempo_empleado"));
        actividadDTO.setFechaRegistro(rs.getTimestamp("fecha_registro").toLocalDateTime());
        return actividadDTO;
    }

    // Implementación del método close para AutoCloseable
    @Override
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            logger.log(Level.INFO, "Conexión cerrada correctamente por ActividadDAO.");
        }
    }
}
