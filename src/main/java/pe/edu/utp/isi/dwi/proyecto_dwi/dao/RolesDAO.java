package pe.edu.utp.isi.dwi.proyecto_dwi.dao;

import pe.edu.utp.isi.dwi.proyecto_dwi.dto.RolDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RolesDAO implements AutoCloseable {

    private static final Logger logger = Logger.getLogger(RolesDAO.class.getName());
    private final Connection connection;

    // Constructor para recibir la conexión desde DAOFactory
    public RolesDAO(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("La conexión a la base de datos no debe ser null.");
        }
        this.connection = connection;
    }

    // Método para obtener todos los roles
    public List<RolDTO> obtenerRoles() throws SQLException {
        List<RolDTO> roles = new ArrayList<>();
        String sql = "SELECT id_rol, nombre_rol, descripcion FROM roles";

        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                RolDTO rolDTO = new RolDTO();
                rolDTO.setIdRol(rs.getInt("id_rol"));
                rolDTO.setNombreRol(rs.getString("nombre_rol"));
                rolDTO.setDescripcion(rs.getString("descripcion"));
                roles.add(rolDTO);
            }
            logger.log(Level.INFO, "Roles obtenidos correctamente");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener los roles", e);
            throw new SQLException("Error al obtener los roles", e);
        }
        return roles;
    }

    // Método para agregar un nuevo rol con transacción
    public void agregarRol(String nombreRol, String descripcion) throws SQLException {
        if (existeRol(nombreRol)) {
            throw new SQLException("El rol con el nombre '" + nombreRol + "' ya existe.");
        }

        String sql = "INSERT INTO roles (nombre_rol, descripcion) VALUES (?, ?)";

        try {
            connection.setAutoCommit(false);  // Inicia la transacción
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, nombreRol);
                ps.setString(2, descripcion);
                ps.executeUpdate();
                connection.commit();  // Confirma la transacción
                logger.log(Level.INFO, "Rol agregado correctamente: {0}", nombreRol);
            } catch (SQLException e) {
                connection.rollback();  // Revertir en caso de error
                logger.log(Level.SEVERE, "Error al agregar el rol: " + nombreRol, e);
                throw new SQLException("Error al agregar el rol", e);
            }
        } finally {
            connection.setAutoCommit(true);  // Restablecer el auto-commit
        }
    }

    // Método para actualizar un rol existente con transacción
    public void actualizarRol(RolDTO rolDTO) throws SQLException {
        if (existeRol(rolDTO.getNombreRol())) {
            throw new SQLException("El rol con el nombre '" + rolDTO.getNombreRol() + "' ya existe.");
        }

        String sql = "UPDATE roles SET nombre_rol = ?, descripcion = ? WHERE id_rol = ?";

        try {
            connection.setAutoCommit(false);  // Inicia la transacción
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, rolDTO.getNombreRol());
                ps.setString(2, rolDTO.getDescripcion());
                ps.setInt(3, rolDTO.getIdRol());
                ps.executeUpdate();
                connection.commit();  // Confirma la transacción
                logger.log(Level.INFO, "Rol actualizado correctamente con ID: {0}", rolDTO.getIdRol());
            } catch (SQLException e) {
                connection.rollback();  // Revertir en caso de error
                logger.log(Level.SEVERE, "Error al actualizar el rol con ID: " + rolDTO.getIdRol(), e);
                throw new SQLException("Error al actualizar el rol", e);
            }
        } finally {
            connection.setAutoCommit(true);  // Restablecer el auto-commit
        }
    }

    // Método para eliminar un rol por su ID con transacción
    public void eliminarRol(int idRol) throws SQLException {
        String sql = "DELETE FROM roles WHERE id_rol = ?";

        try {
            connection.setAutoCommit(false);  // Inicia la transacción
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, idRol);
                ps.executeUpdate();
                connection.commit();  // Confirma la transacción
                logger.log(Level.INFO, "Rol eliminado correctamente con ID: {0}", idRol);
            } catch (SQLException e) {
                connection.rollback();  // Revertir en caso de error
                logger.log(Level.SEVERE, "Error al eliminar el rol con ID: " + idRol, e);
                throw new SQLException("Error al eliminar el rol", e);
            }
        } finally {
            connection.setAutoCommit(true);  // Restablecer el auto-commit
        }
    }

    // Método para obtener un rol por su ID
    public RolDTO obtenerRolPorId(int idRol) throws SQLException {
        String sql = "SELECT id_rol, nombre_rol, descripcion FROM roles WHERE id_rol = ?";
        RolDTO rolDTO = null;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idRol);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    rolDTO = new RolDTO();
                    rolDTO.setIdRol(rs.getInt("id_rol"));
                    rolDTO.setNombreRol(rs.getString("nombre_rol"));
                    rolDTO.setDescripcion(rs.getString("descripcion"));
                }
            }
            logger.log(Level.INFO, "Rol obtenido correctamente con ID: {0}", idRol);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, String.format("Error al obtener el rol con ID: %d", idRol), e);
            throw new SQLException("Error al obtener el rol", e);
        }
        return rolDTO;
    }

    // Método para verificar si ya existe un rol con un nombre dado
    public boolean existeRol(String nombreRol) throws SQLException {
        String sql = "SELECT COUNT(*) FROM roles WHERE nombre_rol = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nombreRol);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Retorna true si ya existe el rol
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al verificar si existe el rol", e);
            throw new SQLException("Error al verificar si existe el rol", e);
        }
        return false;
    }

    // Implementación del método close para AutoCloseable
    @Override
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            logger.log(Level.INFO, "Conexión cerrada correctamente por RolesDAO.");
        }
    }
}
