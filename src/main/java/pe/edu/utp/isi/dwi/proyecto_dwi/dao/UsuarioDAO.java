package pe.edu.utp.isi.dwi.proyecto_dwi.dao;

import pe.edu.utp.isi.dwi.proyecto_dwi.entities.Usuario;
import pe.edu.utp.isi.dwi.proyecto_dwi.dto.UsuarioDTO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UsuarioDAO implements AutoCloseable {

    private static final Logger logger = Logger.getLogger(UsuarioDAO.class.getName());
    private final Connection connection;

    public UsuarioDAO(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("La conexión a la base de datos no debe ser null.");
        }
        this.connection = connection;
    }

    public int registrarUsuario(UsuarioDTO usuarioDTO) throws SQLException {
        String sql = "INSERT INTO usuarios (nombre_usuario, correo, contrasena, salt, direccion, telefono, apellidos, fecha_nacimiento, foto_perfil, fecha_ingreso, departamento, dni) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id_usuario";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            setUsuarioPreparedStatement(stmt, usuarioDTO);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_usuario");
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al registrar el usuario", e);
        }
        return 0; // Si no se genera un ID
    }

    public Usuario obtenerUsuarioPorId(int id) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE id_usuario = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUsuario(rs);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener el usuario por ID", e);
            throw e;
        }
        return null;
    }

    public boolean validarUsuario(String correo, String contrasena) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE correo = ? AND contrasena = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, correo);
            stmt.setString(2, contrasena);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Devuelve true si se encuentra un registro con las credenciales proporcionadas
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al validar el usuario con correo: " + correo, e);
            throw e;
        }
    }

    public int obtenerIdPorCorreo(String correo) throws SQLException {
        String sql = "SELECT id_usuario FROM usuarios WHERE correo = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, correo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_usuario");
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener el ID del usuario por correo", e);
            throw e;
        }
        return 0; // Devuelve 0 si no se encuentra el usuario
    }

    public void guardarTokenRestablecimiento(int idUsuario, String token) throws SQLException {
        String sql = "INSERT INTO tokens (id_usuario, token, fecha_creacion, fecha_expiracion) VALUES (?, ?, CURRENT_TIMESTAMP, (CURRENT_TIMESTAMP + INTERVAL '1 day'))";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            stmt.setString(2, token);
            stmt.executeUpdate();
            logger.log(Level.INFO, "Token de restablecimiento guardado para el usuario con ID: {0}", idUsuario);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al guardar el token de restablecimiento", e);
            throw e;
        }
    }

    public String obtenerCorreoPorToken(String token) throws SQLException {
        String sql = "SELECT u.correo FROM usuarios u JOIN tokens t ON u.id_usuario = t.id_usuario WHERE t.token = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, token);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("correo");
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener el correo por token", e);
            throw e;
        }
        return null; // Devuelve null si no se encuentra el token
    }

    public void actualizarContraseñaConSalt(String correo, String hashedPassword, String salt) throws SQLException {
        String sql = "UPDATE usuarios SET contrasena = ?, salt = ? WHERE correo = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, hashedPassword);
            stmt.setString(2, salt);
            stmt.setString(3, correo);
            stmt.executeUpdate();
            logger.log(Level.INFO, "Contraseña actualizada para el usuario con correo: {0}", correo);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al actualizar la contraseña del usuario", e);
            throw e;
        }
    }

    public void eliminarToken(String token) throws SQLException {
        String sql = "DELETE FROM tokens WHERE token = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, token);
            stmt.executeUpdate();
            logger.log(Level.INFO, "Token de restablecimiento eliminado: {0}", token);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al eliminar el token de restablecimiento", e);
            throw e;
        }
    }

    public void eliminarUsuario(int id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id_usuario=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            logger.log(Level.INFO, "Usuario eliminado con ID: {0}", id);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al eliminar el usuario", e);
            throw e;
        }
    }

    public Usuario obtenerUsuarioPorCorreo(String correo) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE correo = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, correo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUsuario(rs);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener el usuario por correo", e);
            throw e;
        }
        return null;
    }

    public List<Usuario> listarUsuarios() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";

        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                usuarios.add(mapResultSetToUsuario(rs));
            }
            logger.log(Level.INFO, "Usuarios listados correctamente");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener los usuarios", e);
            throw e;
        }
        return usuarios;
    }
    
    public void actualizarUsuario(UsuarioDTO usuario) throws SQLException {
    String sql = "UPDATE usuarios SET nombre_usuario = ?, correo = ?, direccion = ?, telefono = ?, apellidos = ?, fecha_nacimiento = ?, foto_perfil = ?, departamento = ?, dni = ? WHERE id_usuario = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, usuario.getNombreUsuario());
        stmt.setString(2, usuario.getCorreo());
        stmt.setString(3, usuario.getDireccion());
        stmt.setString(4, usuario.getTelefono());
        stmt.setString(5, usuario.getApellidos());
        stmt.setDate(6, usuario.getFechaNacimiento() != null ? Date.valueOf(usuario.getFechaNacimiento()) : null);
        stmt.setString(7, usuario.getFotoPerfil());
        stmt.setString(8, usuario.getDepartamento());
        stmt.setString(9, usuario.getDni());
        stmt.setInt(10, usuario.getIdUsuario());

        stmt.executeUpdate();
        logger.log(Level.INFO, "Usuario actualizado exitosamente con ID: {0}", usuario.getIdUsuario());
    } catch (SQLException e) {
        logger.log(Level.SEVERE, "Error al actualizar el usuario con ID: " + usuario.getIdUsuario(), e);
        throw e;
    }
}

    private Usuario mapResultSetToUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(rs.getInt("id_usuario"));
        usuario.setNombreUsuario(rs.getString("nombre_usuario"));
        usuario.setCorreo(rs.getString("correo"));
        usuario.setContrasena(rs.getString("contrasena"));
        usuario.setSalt(rs.getString("salt"));
        usuario.setFechaRegistro(rs.getTimestamp("fecha_registro").toLocalDateTime().toLocalDate());
        usuario.setDireccion(rs.getString("direccion"));
        usuario.setTelefono(rs.getString("telefono"));
        usuario.setApellidos(rs.getString("apellidos"));
        usuario.setFechaNacimiento(rs.getDate("fecha_nacimiento") != null ? rs.getDate("fecha_nacimiento").toLocalDate() : null);
        usuario.setFotoPerfil(rs.getString("foto_perfil"));
        usuario.setFechaIngreso(rs.getDate("fecha_ingreso") != null ? rs.getDate("fecha_ingreso").toLocalDate() : null);
        usuario.setDepartamento(rs.getString("departamento"));
        usuario.setDni(rs.getString("dni"));
        return usuario;
    }

    private void setUsuarioPreparedStatement(PreparedStatement stmt, UsuarioDTO usuario) throws SQLException {
        stmt.setString(1, usuario.getNombreUsuario());
        stmt.setString(2, usuario.getCorreo());
        stmt.setString(3, usuario.getContrasena());
        stmt.setString(4, usuario.getSalt());
        stmt.setString(5, usuario.getDireccion());
        stmt.setString(6, usuario.getTelefono());
        stmt.setString(7, usuario.getApellidos());
        stmt.setDate(8, usuario.getFechaNacimiento() != null ? Date.valueOf(usuario.getFechaNacimiento()) : null);
        stmt.setString(9, usuario.getFotoPerfil());
        // Aquí asignamos una fecha predeterminada si fechaIngreso es null
        stmt.setDate(10, usuario.getFechaIngreso() != null ? Date.valueOf(usuario.getFechaIngreso()) : Date.valueOf(LocalDate.now()));
        stmt.setString(11, usuario.getDepartamento());
        stmt.setString(12, usuario.getDni());
    }

    @Override
    public void close() {
        if (connection != null) {
            try {
                connection.close();
                logger.log(Level.INFO, "Conexión cerrada exitosamente en UsuarioDAO");
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error al cerrar la conexión en UsuarioDAO", e);
            }
        }
    }
}
