package pe.edu.utp.isi.dwi.proyecto_dwi.dao;

import pe.edu.utp.isi.dwi.proyecto_dwi.dto.AsignacionDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AsignacionDAO implements AutoCloseable {

    private static final Logger logger = Logger.getLogger(AsignacionDAO.class.getName());
    private final Connection connection;

    // Constructor que obtiene la conexión desde DAOFactory
    public AsignacionDAO(Connection connection) {
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

    // Método para registrar una nueva asignación
    public void registrarAsignacion(AsignacionDTO asignacionDTO) throws SQLException {
        verificarConexion();

        String sql = "INSERT INTO asignaciones (id_solicitud, id_colaborador, id_coordinador, fecha_asignacion) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, asignacionDTO.getIdSolicitud());
            stmt.setInt(2, asignacionDTO.getIdColaborador());
            stmt.setInt(3, asignacionDTO.getIdCoordinador());
            stmt.setTimestamp(4, Timestamp.valueOf(asignacionDTO.getFechaAsignacion()));
            stmt.executeUpdate();

            // Obtener el ID generado automáticamente
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    asignacionDTO.setIdAsignacion(generatedKeys.getInt(1));
                }
            }
            logger.log(Level.INFO, "Asignación registrada correctamente para ID solicitud: {0}", asignacionDTO.getIdSolicitud());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al registrar asignación", e);
            throw new SQLException("Error al registrar asignación", e);
        }
    }

    // Método para obtener todas las asignaciones
    public List<AsignacionDTO> obtenerAsignaciones() throws SQLException {
        verificarConexion();

        List<AsignacionDTO> asignaciones = new ArrayList<>();
        String query = "SELECT * FROM asignaciones";

        try (PreparedStatement ps = connection.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                AsignacionDTO asignacionDTO = mapResultSetToAsignacionDTO(rs);
                asignaciones.add(asignacionDTO);
            }
            logger.log(Level.INFO, "Asignaciones obtenidas correctamente");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener las asignaciones", e);
            throw new SQLException("Error al obtener las asignaciones", e);
        }
        return asignaciones;
    }

    // Método para obtener una asignación por su ID
    public AsignacionDTO obtenerAsignacionPorId(int idAsignacion) throws SQLException {
        verificarConexion();

        String query = "SELECT * FROM asignaciones WHERE id_asignacion = ?";
        AsignacionDTO asignacionDTO = null;

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idAsignacion);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    asignacionDTO = mapResultSetToAsignacionDTO(rs);
                }
            }
            if (asignacionDTO != null) {
                logger.log(Level.INFO, "Asignación obtenida correctamente con ID: {0}", idAsignacion);
            } else {
                logger.log(Level.WARNING, "No se encontró la asignación con ID: {0}", idAsignacion);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener la asignación por ID", e);
            throw new SQLException("Error al obtener la asignación por ID", e);
        }
        return asignacionDTO;
    }

    // Método para actualizar una asignación
    public void actualizarAsignacion(AsignacionDTO asignacionDTO) throws SQLException {
        verificarConexion();

        String sql = "UPDATE asignaciones SET id_solicitud = ?, id_colaborador = ?, id_coordinador = ?, fecha_asignacion = ? WHERE id_asignacion = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, asignacionDTO.getIdSolicitud());
            ps.setInt(2, asignacionDTO.getIdColaborador());
            ps.setInt(3, asignacionDTO.getIdCoordinador());
            ps.setTimestamp(4, Timestamp.valueOf(asignacionDTO.getFechaAsignacion()));
            ps.setInt(5, asignacionDTO.getIdAsignacion());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                logger.log(Level.INFO, "Asignación actualizada correctamente con ID: {0}", asignacionDTO.getIdAsignacion());
            } else {
                logger.log(Level.WARNING, "No se encontró la asignación con el ID: {0}", asignacionDTO.getIdAsignacion());
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al actualizar la asignación con ID: " + asignacionDTO.getIdAsignacion(), e);
            throw new SQLException("Error al actualizar la asignación", e);
        }
    }

    // Método para eliminar una asignación
    public void eliminarAsignacion(int idAsignacion) throws SQLException {
        verificarConexion();

        String query = "DELETE FROM asignaciones WHERE id_asignacion = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idAsignacion);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                logger.log(Level.INFO, "Asignación eliminada correctamente con ID {0}", idAsignacion);
            } else {
                logger.log(Level.WARNING, "No se encontró la asignación con ID {0} para eliminar", idAsignacion);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al eliminar la asignación con ID: " + idAsignacion, e);
            throw new SQLException("Error al eliminar la asignación", e);
        }
    }

    // Método auxiliar para mapear ResultSet a AsignacionDTO
    private AsignacionDTO mapResultSetToAsignacionDTO(ResultSet rs) throws SQLException {
        AsignacionDTO asignacionDTO = new AsignacionDTO();
        asignacionDTO.setIdAsignacion(rs.getInt("id_asignacion"));
        asignacionDTO.setIdSolicitud(rs.getInt("id_solicitud"));
        asignacionDTO.setIdColaborador(rs.getInt("id_colaborador"));
        asignacionDTO.setIdCoordinador(rs.getInt("id_coordinador"));
        asignacionDTO.setFechaAsignacion(rs.getTimestamp("fecha_asignacion").toLocalDateTime());
        return asignacionDTO;
    }

    // Implementación del método close para AutoCloseable
    @Override
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            logger.log(Level.INFO, "Conexión cerrada correctamente por AsignacionDAO.");
        }
    }
}
