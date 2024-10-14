package pe.edu.utp.isi.dwi.proyecto_dwi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import pe.edu.utp.isi.dwi.proyecto_dwi.dto.DashboardDataDTO;

public class DashboardDAO implements AutoCloseable {

    private static final Logger logger = Logger.getLogger(DashboardDAO.class.getName());
    private final Connection connection;

    // Constructor que obtiene la conexión desde DAOFactory (usando Singleton)
    public DashboardDAO(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("La conexión proporcionada no puede ser null.");
        }
        this.connection = connection;
    }

    // Método para obtener los datos del dashboard
    public DashboardDataDTO obtenerDatosDashboard() throws SQLException {
        verificarConexion();

        String sql = "SELECT "
                + "(SELECT COUNT(*) FROM usuarios) AS totalUsuarios, "
                + "(SELECT COUNT(*) FROM actividades) AS totalActividades, "
                + "(SELECT COUNT(*) FROM solicitudes) AS totalSolicitudes, "
                + "(SELECT COUNT(*) FROM solicitudes WHERE estado = 'pendiente') AS solicitudesPendientes, "
                + "(SELECT COUNT(*) FROM solicitudes WHERE estado = 'completada') AS solicitudesCompletadas, "
                + "(SELECT COUNT(*) FROM asignaciones) AS totalAsignaciones";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                DashboardDataDTO dataDTO = new DashboardDataDTO();
                dataDTO.setTotalUsuarios(rs.getInt("totalUsuarios"));
                dataDTO.setTotalActividades(rs.getInt("totalActividades"));
                dataDTO.setTotalSolicitudes(rs.getInt("totalSolicitudes"));
                dataDTO.setSolicitudesPendientes(rs.getInt("solicitudesPendientes"));
                dataDTO.setSolicitudesCompletadas(rs.getInt("solicitudesCompletadas"));
                dataDTO.setTotalAsignaciones(rs.getInt("totalAsignaciones"));

                // Llamada a métodos adicionales para completar el DTO
                dataDTO.setTiempoPromedioRespuesta(obtenerTiempoPromedioDeRespuesta());
                dataDTO.setActividadesPorColaborador(obtenerActividadesPorColaborador());

                logger.log(Level.INFO, "Datos del dashboard obtenidos correctamente.");
                return dataDTO;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener los datos del dashboard", e);
            throw new SQLException("Error al obtener datos del dashboard", e);
        }
        return null;
    }

    // Método para obtener el tiempo promedio de respuesta
    public double obtenerTiempoPromedioDeRespuesta() throws SQLException {
        verificarConexion();

        String sql = "SELECT AVG(EXTRACT(EPOCH FROM (fecha_completada - fecha_creacion)) / 3600) AS tiempo_promedio "
                   + "FROM solicitudes WHERE fecha_completada IS NOT NULL";
        double tiempoPromedio = 0.0;

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                tiempoPromedio = rs.getDouble("tiempo_promedio");
            }
            logger.log(Level.INFO, "Tiempo promedio de respuesta obtenido correctamente: {0} horas", tiempoPromedio);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al calcular el tiempo promedio de respuesta", e);
            throw new SQLException("Error al calcular el tiempo promedio de respuesta", e);
        }
        return tiempoPromedio;
    }

    // Método para obtener el total de actividades por colaborador
    public Map<String, Integer> obtenerActividadesPorColaborador() throws SQLException {
        verificarConexion();

        String sql = "SELECT c.nombre, COUNT(a.id_actividad) AS total_actividades "
                   + "FROM colaboradores c LEFT JOIN actividades a ON c.id_colaborador = a.id_colaborador "
                   + "GROUP BY c.nombre";
        Map<String, Integer> actividadesPorColaborador = new HashMap<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                int totalActividades = rs.getInt("total_actividades");
                actividadesPorColaborador.put(nombre, totalActividades);
            }
            logger.log(Level.INFO, "Actividades por colaborador obtenidas correctamente.");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener actividades por colaborador", e);
            throw new SQLException("Error al obtener actividades por colaborador", e);
        }
        return actividadesPorColaborador;
    }

    // Método privado para verificar la conexión antes de cualquier operación
    private void verificarConexion() throws SQLException {
        if (connection == null || connection.isClosed()) {
            throw new SQLException("Conexión a la base de datos no disponible.");
        }
    }

    // Implementación del método close para AutoCloseable
    @Override
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            logger.log(Level.INFO, "Conexión cerrada correctamente por DashboardDAO.");
        }
    }
}
