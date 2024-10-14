package pe.edu.utp.isi.dwi.proyecto_dwi.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pe.edu.utp.isi.dwi.proyecto_dwi.dao.UsuarioDAO;
import pe.edu.utp.isi.dwi.proyecto_dwi.dto.UsuarioDTO;
import pe.edu.utp.isi.dwi.proyecto_dwi.entities.Usuario;
import pe.edu.utp.isi.dwi.proyecto_dwi.dao.ConexionBD;
import pe.edu.utp.isi.dwi.proyecto_dwi.util.Roles;
import pe.edu.utp.isi.dwi.proyecto_dwi.dao.ColaboradorDAO; // Importación de ColaboradorDAO
import pe.edu.utp.isi.dwi.proyecto_dwi.entities.Colaborador; // Importación de Colaborador
import java.time.LocalDate; // Importación de LocalDate para manejar fechas

public class UsuarioFacade {

    private static final Logger logger = Logger.getLogger(UsuarioFacade.class.getName());

    // Obtener un usuario por ID
    public UsuarioDTO obtenerUsuarioPorId(int id) {
        try (Connection connection = ConexionBD.getConnection()) {
            UsuarioDAO usuarioDAO = new UsuarioDAO(connection); // Crear el UsuarioDAO directamente con la conexión
            Usuario usuario = usuarioDAO.obtenerUsuarioPorId(id);
            if (usuario != null) {
                logger.log(Level.INFO, "Usuario encontrado con ID: {0}", id);
                return UsuarioDTO.fromEntity(usuario);
            } else {
                logger.log(Level.WARNING, "No se encontró ningún usuario con ID: {0}", id);
                return null;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener el usuario por ID: " + id, e);
            throw new RuntimeException("Error al obtener el usuario por ID", e);
        }
    }

    // Listar todos los usuarios
    public List<UsuarioDTO> listarUsuarios() {
        try (Connection connection = ConexionBD.getConnection()) {
            UsuarioDAO usuarioDAO = new UsuarioDAO(connection); // Crear el UsuarioDAO directamente con la conexión
            List<Usuario> usuarios = usuarioDAO.listarUsuarios();
            logger.log(Level.INFO, "Se encontraron {0} usuarios.", usuarios.size());
            return UsuarioDTO.fromEntities(usuarios);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al listar los usuarios", e);
            throw new RuntimeException("Error al listar los usuarios", e);
        }
    }

    // Registrar un nuevo usuario
    public int registrarUsuario(UsuarioDTO usuarioDTO) {
        if (usuarioDTO == null) {
            throw new IllegalArgumentException("El UsuarioDTO no puede ser nulo");
        }
        try (Connection connection = ConexionBD.getConnection()) {
            UsuarioDAO usuarioDAO = new UsuarioDAO(connection); // Crear el UsuarioDAO directamente con la conexión
            logger.log(Level.INFO, "Intentando registrar usuario con correo: {0}", usuarioDTO.getCorreo());
            int idUsuario = usuarioDAO.registrarUsuario(usuarioDTO);
            logger.log(Level.INFO, "Usuario registrado exitosamente con ID: {0}", idUsuario);
            return idUsuario;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al registrar el usuario con correo: " + usuarioDTO.getCorreo(), e);
            throw new RuntimeException("Error al registrar el usuario", e);
        }
    }

    public int registrarUsuarioCompleto(UsuarioDTO usuarioDTO, String cargo) {
        if (usuarioDTO == null) {
            throw new IllegalArgumentException("El UsuarioDTO no puede ser nulo");
        }
        if (cargo == null || cargo.isEmpty()) {
            throw new IllegalArgumentException("El cargo no puede ser nulo o vacío");
        }

        try (Connection connection = ConexionBD.getConnection()) {
            connection.setAutoCommit(false);  // Desactivar el auto-commit para manejar la transacción manualmente
            try {
                // Registrar el usuario y obtener el ID generado
                UsuarioDAO usuarioDAO = new UsuarioDAO(connection);
                logger.log(Level.INFO, "Registrando usuario con correo: {0}", usuarioDTO.getCorreo());
                int idUsuario = usuarioDAO.registrarUsuario(usuarioDTO);

                // Registrar al colaborador usando el ID del usuario generado
                ColaboradorDAO colaboradorDAO = new ColaboradorDAO(connection);
                Colaborador colaborador = new Colaborador();
                colaborador.setIdUsuario(idUsuario);
                colaborador.setCargo(cargo);
                colaborador.setFechaIngreso(LocalDate.now()); // Fecha actual
                colaboradorDAO.registrarColaborador(colaborador);

                // Obtener el ID del rol basado en el cargo y registrar la relación en usuarios_roles
                int idRol = obtenerIdRolPorCargo(cargo);
                registrarUsuarioRol(connection, idUsuario, idRol);

                connection.commit();  // Confirmar la transacción si todo sale bien
                logger.log(Level.INFO, "Usuario, colaborador y rol registrados exitosamente con ID de usuario: {0}, cargo: {1}, rol: {2}", new Object[]{idUsuario, cargo, idRol});
                return idUsuario;

            } catch (SQLException e) {
                connection.rollback();  // Deshacer todos los cambios si hay algún problema
                logger.log(Level.SEVERE, "Error al registrar el usuario, colaborador y rol. Realizando rollback: {0}", e.getMessage());
                throw new RuntimeException("Error al registrar el usuario, colaborador y rol", e);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al establecer la conexión: {0}", e.getMessage());
            throw new RuntimeException("Error al establecer la conexión", e);
        }
    }

    public void actualizarUsuario(UsuarioDTO usuarioDTO) {
        if (usuarioDTO == null) {
            throw new IllegalArgumentException("El UsuarioDTO no puede ser nulo");
        }
        try (Connection connection = ConexionBD.getConnection()) {
            UsuarioDAO usuarioDAO = new UsuarioDAO(connection);
            usuarioDAO.actualizarUsuario(usuarioDTO);
            logger.log(Level.INFO, "Usuario actualizado exitosamente con ID: {0}", usuarioDTO.getIdUsuario());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al actualizar el usuario con ID: " + usuarioDTO.getIdUsuario(), e);
            throw new RuntimeException("Error al actualizar el usuario", e);
        }
    }

    // Método para obtener el ID del rol basado en el cargo
    private int obtenerIdRolPorCargo(String cargo) {
        return switch (cargo) {
            case "Jefe de Área" ->
                1;
            case "Analista Senior" ->
                2;
            case "Programador Junior" ->
                3;
            case "Cargo por Defecto" ->
                4;
            case "Coordinador" ->
                5;
            default ->
                throw new IllegalArgumentException("Cargo no válido");
        };
    }

// Método para registrar el rol del usuario en la tabla usuarios_roles
    private void registrarUsuarioRol(Connection connection, int idUsuario, int idRol) throws SQLException {
        String sql = "INSERT INTO usuarios_roles (id_usuario, id_rol) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idRol);
            stmt.executeUpdate();
        }
    }

    // Eliminar un usuario por ID
    public void eliminarUsuario(int id, UsuarioDTO usuarioSolicitante) {
        if (!usuarioSolicitante.tienePermiso(Roles.CARGO_JEFE_DE_AREA)) {
            throw new RuntimeException("No tiene permiso para eliminar usuarios.");
        }

        try (Connection connection = ConexionBD.getConnection()) {
            connection.setAutoCommit(false); // Iniciar una transacción

            // Primero elimina el colaborador asociado al usuario
            ColaboradorDAO colaboradorDAO = new ColaboradorDAO(connection);
            colaboradorDAO.eliminarColaboradorPorIdUsuario(id);

            // Luego elimina el usuario
            UsuarioDAO usuarioDAO = new UsuarioDAO(connection);
            usuarioDAO.eliminarUsuario(id);

            connection.commit(); // Confirmar los cambios
            logger.log(Level.INFO, "Usuario y colaborador eliminados exitosamente con ID de usuario: {0}", id);

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al eliminar el usuario con ID: " + id, e);
            throw new RuntimeException("Error al eliminar el usuario", e);
        }
    }
}
