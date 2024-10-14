package pe.edu.utp.isi.dwi.proyecto_dwi.dao;

import pe.edu.utp.isi.dwi.proyecto_dwi.entities.Colaborador;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ColaboradorDAO implements AutoCloseable {

    private static final Logger logger = Logger.getLogger(ColaboradorDAO.class.getName());
    private final Connection connection;

    // Constructor que obtiene la conexión desde DAOFactory
    public ColaboradorDAO(Connection connection) {
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

    // Método para eliminar un colaborador por id_usuario
    public void eliminarColaboradorPorIdUsuario(int idUsuario) throws SQLException {
        verificarConexion();

        String sql = "DELETE FROM colaboradores WHERE id_usuario = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.log(Level.INFO, "Colaborador eliminado correctamente con ID de usuario: {0}", idUsuario);
            } else {
                logger.log(Level.WARNING, "No se encontró el colaborador con ID de usuario: {0} para eliminar", idUsuario);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al eliminar colaborador con ID de usuario: " + idUsuario, e);
            throw new SQLException("Error al eliminar colaborador por ID de usuario", e);
        }
    }

    // Método para registrar un colaborador
    public void registrarColaborador(Colaborador colaborador) throws SQLException {
        verificarConexion();

        // Eliminar si ya existe el colaborador con el mismo id_usuario
        eliminarColaboradorPorIdUsuario(colaborador.getIdUsuario());

        // Registrar al colaborador
        String sql = "INSERT INTO colaboradores (id_usuario, cargo, fecha_ingreso) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, colaborador.getIdUsuario());
            stmt.setString(2, colaborador.getCargo());
            stmt.setTimestamp(3, Timestamp.valueOf(colaborador.getFechaIngreso().atStartOfDay()));
            stmt.executeUpdate();
            logger.log(Level.INFO, "Colaborador registrado correctamente con ID de usuario: {0}", colaborador.getIdUsuario());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al registrar colaborador con ID de usuario: " + colaborador.getIdUsuario(), e);
            throw new SQLException("Error al registrar colaborador", e);
        }
    }

    // Método para obtener un colaborador por ID
    public Colaborador obtenerColaboradorPorId(int idColaborador) throws SQLException {
        verificarConexion();

        String sql = "SELECT * FROM colaboradores WHERE id_colaborador = ?";
        Colaborador colaborador = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idColaborador);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    colaborador = mapResultSetToColaborador(rs);
                }
            }
            if (colaborador != null) {
                logger.log(Level.INFO, "Colaborador obtenido correctamente con ID: {0}", idColaborador);
            } else {
                logger.log(Level.WARNING, "No se encontró el colaborador con ID: {0}", idColaborador);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener colaborador por ID: " + idColaborador, e);
            throw new SQLException("Error al obtener colaborador por ID", e);
        }
        return colaborador;
    }

    // Método para actualizar un colaborador
    public void actualizarColaborador(int idColaborador, String cargo) throws SQLException {
        verificarConexion();

        String sql = "UPDATE colaboradores SET cargo = ? WHERE id_colaborador = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cargo);
            stmt.setInt(2, idColaborador);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.log(Level.INFO, "Colaborador actualizado correctamente con ID: {0}", idColaborador);
            } else {
                logger.log(Level.WARNING, "No se encontró el colaborador con ID: {0} para actualizar", idColaborador);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al actualizar colaborador con ID: " + idColaborador, e);
            throw new SQLException("Error al actualizar colaborador", e);
        }
    }

    // Método para eliminar un colaborador
    public void eliminarColaborador(int idColaborador) throws SQLException {
        verificarConexion();

        String sql = "DELETE FROM colaboradores WHERE id_colaborador = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idColaborador);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.log(Level.INFO, "Colaborador eliminado correctamente con ID: {0}", idColaborador);
            } else {
                logger.log(Level.WARNING, "No se encontró el colaborador con ID: {0} para eliminar", idColaborador);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al eliminar colaborador con ID: " + idColaborador, e);
            throw new SQLException("Error al eliminar colaborador", e);
        }
    }

    // Método para obtener todos los colaboradores
    public List<Colaborador> obtenerColaboradores() throws SQLException {
        verificarConexion();

        List<Colaborador> colaboradores = new ArrayList<>();
        String sql = "SELECT * FROM colaboradores";

        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                colaboradores.add(mapResultSetToColaborador(rs));
            }
            logger.log(Level.INFO, "Se obtuvieron todos los colaboradores correctamente.");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener los colaboradores", e);
            throw new SQLException("Error al obtener los colaboradores", e);
        }
        return colaboradores;
    }

    // Método auxiliar para mapear ResultSet a Colaborador
    private Colaborador mapResultSetToColaborador(ResultSet rs) throws SQLException {
        Colaborador colaborador = new Colaborador();
        colaborador.setIdColaborador(rs.getInt("id_colaborador"));
        colaborador.setIdUsuario(rs.getInt("id_usuario"));
        colaborador.setCargo(rs.getString("cargo"));
        colaborador.setFechaIngreso(rs.getDate("fecha_ingreso").toLocalDate());
        return colaborador;
    }

    // Implementación del método close para AutoCloseable
    @Override
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            logger.log(Level.INFO, "Conexión cerrada correctamente por ColaboradorDAO.");
        }
    }
}
