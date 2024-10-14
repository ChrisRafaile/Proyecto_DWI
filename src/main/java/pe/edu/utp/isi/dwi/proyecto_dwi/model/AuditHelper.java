package pe.edu.utp.isi.dwi.proyecto_dwi.model;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import pe.edu.utp.isi.dwi.proyecto_dwi.dao.ConexionBD;

public class AuditHelper {

    private static final Logger logger = Logger.getLogger(AuditHelper.class.getName());

    // Método para registrar cambios en la base de datos
    public static void registrarCambio(int idUsuario, String tablaAfectada, String campoAfectado,
            String valorAnterior, String valorNuevo, String descripcionCambio) {
        if (descripcionCambio == null || descripcionCambio.isEmpty()) {
            logger.log(Level.WARNING, "Descripción del cambio no puede estar vacía");
            return;
        }

        String query = "INSERT INTO historial_cambios (id_usuario, cambio, fecha_cambio, tabla_afectada, campo_afectado, valor_anterior, valor_nuevo) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = ConexionBD.getConnection(); // Obtiene una nueva conexión del pool
                 PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, idUsuario);
            ps.setString(2, descripcionCambio);
            ps.setTimestamp(3, new Timestamp(new Date().getTime()));
            ps.setString(4, tablaAfectada);
            ps.setString(5, campoAfectado);
            ps.setString(6, valorAnterior);
            ps.setString(7, valorNuevo);

            ps.executeUpdate();
            logger.log(Level.INFO, "Cambio registrado correctamente en la tabla historial_cambios: {0}", descripcionCambio);

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al registrar el cambio en la base de datos: " + descripcionCambio, e);
        }
    }
}
