package pe.edu.utp.isi.dwi.proyecto_dwi.dao;

import pe.edu.utp.isi.dwi.proyecto_dwi.entities.HistorialCambios;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HistorialCambiosDAO implements AutoCloseable {

    private static final Logger logger = Logger.getLogger(HistorialCambiosDAO.class.getName());
    private final Connection connection;

    // Constructor para inyectar una conexión externa desde DAOFactory
    public HistorialCambiosDAO(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("La conexión proporcionada no puede ser null.");
        }
        this.connection = connection;
        logger.log(Level.INFO, "Conexión inyectada correctamente para HistorialCambiosDAO.");
    }

    // Método para registrar un cambio en el historial
    public void registrarCambio(HistorialCambios historial) throws SQLException {
        String sql = "INSERT INTO historial_cambios (id_usuario, descripcion_cambio, fecha_cambio, tabla_afectada, campo_afectado, valor_anterior, valor_nuevo) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, historial.getIdUsuario());
            ps.setString(2, historial.getDescripcionCambio());
            ps.setTimestamp(3, historial.getFechaCambio());
            ps.setString(4, historial.getTablaAfectada());
            ps.setString(5, historial.getCampoAfectado());
            ps.setString(6, historial.getValorAnterior());
            ps.setString(7, historial.getValorNuevo());

            ps.executeUpdate();
            logger.log(Level.INFO, "Cambio registrado en el historial para usuario ID: {0}", historial.getIdUsuario());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al registrar el cambio en el historial para usuario ID: " + historial.getIdUsuario(), e);
            throw new SQLException("Error al registrar el cambio en el historial", e);
        }
    }

    // Método para obtener todos los cambios del historial
    public List<HistorialCambios> obtenerHistorialCambios() throws SQLException {
        String sql = "SELECT * FROM historial_cambios ORDER BY fecha_cambio DESC";
        List<HistorialCambios> historialCambios = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                HistorialCambios historial = new HistorialCambios();
                historial.setIdHistorial(rs.getInt("id_historial"));
                historial.setIdUsuario(rs.getInt("id_usuario"));
                historial.setDescripcionCambio(rs.getString("descripcion_cambio"));
                historial.setFechaCambio(rs.getTimestamp("fecha_cambio"));
                historial.setTablaAfectada(rs.getString("tabla_afectada"));
                historial.setCampoAfectado(rs.getString("campo_afectado"));
                historial.setValorAnterior(rs.getString("valor_anterior"));
                historial.setValorNuevo(rs.getString("valor_nuevo"));

                historialCambios.add(historial);
            }
            logger.log(Level.INFO, "Historial de cambios obtenido correctamente.");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener el historial de cambios", e);
            throw new SQLException("Error al obtener el historial de cambios", e);
        }
        return historialCambios;
    }

    // Implementación del método close para AutoCloseable
    @Override
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            logger.log(Level.INFO, "Conexión cerrada correctamente por HistorialCambiosDAO.");
        }
    }
}
