package pe.edu.utp.isi.dwi.proyecto_dwi.dao;

import pe.edu.utp.isi.dwi.proyecto_dwi.entities.LogAuditoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogAuditoriaDAO implements AutoCloseable {
    private static final Logger logger = Logger.getLogger(LogAuditoriaDAO.class.getName());
    private final Connection connection;

    // Constructor que recibe la conexión desde DAOFactory
    public LogAuditoriaDAO(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("La conexión proporcionada no puede ser null.");
        }
        this.connection = connection;
        logger.log(Level.INFO, "Conexión inyectada correctamente para LogAuditoriaDAO.");
    }

    // Método para registrar un log de auditoría
    public void registrarLog(LogAuditoria logAuditoria) throws SQLException {
        String sql = "INSERT INTO logs_auditoria (id_usuario, accion, fecha) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, logAuditoria.getIdUsuario());
            ps.setString(2, logAuditoria.getAccion());
            ps.setTimestamp(3, logAuditoria.getFecha());
            ps.executeUpdate();
            logger.log(Level.INFO, "Log de auditoría registrado para usuario ID: {0}", logAuditoria.getIdUsuario());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al registrar el log de auditoría para usuario ID: " + logAuditoria.getIdUsuario(), e);
            throw new SQLException("Error al registrar el log de auditoría", e);
        }
    }

    // Método para obtener todos los logs de auditoría
    public List<LogAuditoria> obtenerLogsAuditoria() throws SQLException {
        String sql = "SELECT * FROM logs_auditoria ORDER BY fecha DESC";
        List<LogAuditoria> logsAuditoria = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                LogAuditoria logAuditoria = new LogAuditoria();
                logAuditoria.setIdLog(rs.getInt("id_log"));
                logAuditoria.setIdUsuario(rs.getInt("id_usuario"));
                logAuditoria.setAccion(rs.getString("accion"));
                logAuditoria.setFecha(rs.getTimestamp("fecha"));
                logsAuditoria.add(logAuditoria);
            }
            logger.log(Level.INFO, "Logs de auditoría obtenidos correctamente.");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener los logs de auditoría", e);
            throw new SQLException("Error al obtener los logs de auditoría", e);
        }

        return logsAuditoria;
    }

    // Implementación del método close para AutoCloseable
    @Override
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            logger.log(Level.INFO, "Conexión cerrada correctamente por LogAuditoriaDAO.");
        }
    }
}
