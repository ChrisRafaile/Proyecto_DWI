package pe.edu.utp.isi.dwi.proyecto_dwi.dao;

import pe.edu.utp.isi.dwi.proyecto_dwi.dto.PermisoDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PermisosDAO implements AutoCloseable {

    private static final Logger logger = Logger.getLogger(PermisosDAO.class.getName());
    private final Connection connection;

    // Constructor para recibir la conexión desde DAOFactory
    public PermisosDAO(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("La conexión a la base de datos no debe ser null.");
        }
        this.connection = connection;
    }

    // Método para obtener todos los permisos
    public List<PermisoDTO> obtenerPermisos() throws SQLException {
        List<PermisoDTO> permisos = new ArrayList<>();
        String query = "SELECT id_permiso, id_rol, permiso FROM permisos";

        try (PreparedStatement ps = connection.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                PermisoDTO permisoDTO = new PermisoDTO();
                permisoDTO.setIdPermiso(rs.getInt("id_permiso"));
                permisoDTO.setIdRol(rs.getInt("id_rol"));
                permisoDTO.setPermiso(rs.getString("permiso"));
                permisos.add(permisoDTO);
            }
            logger.log(Level.INFO, "Permisos obtenidos correctamente.");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener los permisos", e);
            throw e;
        }
        return permisos;
    }

    // Método para obtener un permiso por ID
    public PermisoDTO obtenerPermisoPorId(int idPermiso) throws SQLException {
        String query = "SELECT id_permiso, id_rol, permiso FROM permisos WHERE id_permiso = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idPermiso);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PermisoDTO permisoDTO = new PermisoDTO();
                    permisoDTO.setIdPermiso(rs.getInt("id_permiso"));
                    permisoDTO.setIdRol(rs.getInt("id_rol"));
                    permisoDTO.setPermiso(rs.getString("permiso"));
                    logger.log(Level.INFO, "Permiso obtenido correctamente con ID: {0}", idPermiso);
                    return permisoDTO;
                } else {
                    logger.log(Level.WARNING, "Permiso no encontrado con ID: {0}", idPermiso);
                    return null;
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, String.format("Error al obtener el permiso con ID: %d", idPermiso), e);
            throw e;
        }
    }

    // Método para insertar un nuevo permiso
    public void insertarPermiso(PermisoDTO permisoDTO) throws SQLException {
        String query = "INSERT INTO permisos (id_rol, permiso) VALUES (?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, permisoDTO.getIdRol());
            ps.setString(2, permisoDTO.getPermiso());
            ps.executeUpdate();
            logger.log(Level.INFO, "Permiso insertado correctamente: {0}", permisoDTO.getPermiso());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, String.format("Error al insertar el permiso: %s", permisoDTO.getPermiso()), e);
            throw e;
        }
    }

    // Método para actualizar un permiso
    public void actualizarPermiso(PermisoDTO permisoDTO) throws SQLException {
        String query = "UPDATE permisos SET id_rol = ?, permiso = ? WHERE id_permiso = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, permisoDTO.getIdRol());
            ps.setString(2, permisoDTO.getPermiso());
            ps.setInt(3, permisoDTO.getIdPermiso());
            ps.executeUpdate();
            logger.log(Level.INFO, "Permiso actualizado correctamente con ID: {0}", permisoDTO.getIdPermiso());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, String.format("Error al actualizar el permiso con ID: %d", permisoDTO.getIdPermiso()), e);
            throw e;
        }
    }

    // Método para eliminar un permiso por ID
    public void eliminarPermiso(int idPermiso) throws SQLException {
        String query = "DELETE FROM permisos WHERE id_permiso = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idPermiso);
            ps.executeUpdate();
            logger.log(Level.INFO, "Permiso eliminado correctamente con ID: {0}", idPermiso);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, String.format("Error al eliminar el permiso con ID: %d", idPermiso), e);
            throw e;
        }
    }

    // Implementación del método close para AutoCloseable
    @Override
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            logger.log(Level.INFO, "Conexión cerrada correctamente por PermisosDAO.");
        }
    }
}
