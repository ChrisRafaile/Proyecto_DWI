package pe.edu.utp.isi.dwi.proyecto_dwi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import pe.edu.utp.isi.dwi.proyecto_dwi.dto.RolDTO;

public class UsuariosRolesDAO implements AutoCloseable {

    private static final Logger logger = Logger.getLogger(UsuariosRolesDAO.class.getName());
    private final Connection connection;

    // Constructor para recibir la conexión desde DAOFactory
    public UsuariosRolesDAO(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("La conexión a la base de datos no debe ser null.");
        }
        this.connection = connection;
    }

    // Método para asignar un rol a un usuario
    public void asignarRolAUsuario(int idUsuario, int idRol) throws SQLException {
        String sql = "INSERT INTO usuarios_roles (id_usuario, id_rol) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setInt(2, idRol);
            ps.executeUpdate();
            logger.log(Level.INFO, "Rol asignado correctamente al usuario con ID: {0}", idUsuario);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al asignar rol al usuario con ID: " + idUsuario, e);
            throw new SQLException("Error al asignar rol al usuario", e);
        }
    }

    // Método para eliminar un rol de un usuario
    public void eliminarRolDeUsuario(int idUsuario, int idRol) throws SQLException {
        String sql = "DELETE FROM usuarios_roles WHERE id_usuario = ? AND id_rol = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setInt(2, idRol);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                logger.log(Level.INFO, "Rol eliminado correctamente del usuario con ID: {0}", idUsuario);
            } else {
                logger.log(Level.WARNING, "No se encontró el rol con ID {0} para el usuario con ID: {1}", new Object[]{idRol, idUsuario});
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al eliminar el rol del usuario con ID: " + idUsuario, e);
            throw new SQLException("Error al eliminar el rol del usuario", e);
        }
    }

    // Método para obtener los roles de un usuario
    public List<RolDTO> obtenerRolesDeUsuario(int idUsuario) throws SQLException {
        List<RolDTO> roles = new ArrayList<>();
        String sql = "SELECT r.id_rol, r.nombre_rol FROM roles r INNER JOIN usuarios_roles ur ON r.id_rol = ur.id_rol WHERE ur.id_usuario = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RolDTO rolDTO = new RolDTO();
                    rolDTO.setIdRol(rs.getInt("id_rol"));
                    rolDTO.setNombreRol(rs.getString("nombre_rol"));
                    roles.add(rolDTO);
                }
                logger.log(Level.INFO, "Roles obtenidos correctamente para el usuario con ID: {0}", idUsuario);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener los roles del usuario con ID: " + idUsuario, e);
            throw new SQLException("Error al obtener los roles del usuario", e);
        }
        return roles;
    }

    // Método para verificar si un usuario tiene un rol específico
    public boolean usuarioTieneRol(int idUsuario, int idRol) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuarios_roles WHERE id_usuario = ? AND id_rol = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setInt(2, idRol);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al verificar el rol del usuario con ID: " + idUsuario, e);
            throw new SQLException("Error al verificar el rol del usuario", e);
        }
        return false;
    }

    // Implementación del método close para AutoCloseable
    @Override
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            logger.log(Level.INFO, "Conexión cerrada correctamente por UsuariosRolesDAO.");
        }
    }
}
