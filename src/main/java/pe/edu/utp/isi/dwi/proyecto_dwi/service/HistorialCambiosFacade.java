package pe.edu.utp.isi.dwi.proyecto_dwi.service;

import pe.edu.utp.isi.dwi.proyecto_dwi.dao.HistorialCambiosDAO;
import pe.edu.utp.isi.dwi.proyecto_dwi.dto.HistorialCambiosDTO;
import pe.edu.utp.isi.dwi.proyecto_dwi.entities.HistorialCambios;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HistorialCambiosFacade {

    private static final Logger logger = Logger.getLogger(HistorialCambiosFacade.class.getName());
    private final Connection connection;

    public HistorialCambiosFacade(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("La conexión proporcionada no puede ser null.");
        }
        this.connection = connection;
    }

    // Registrar un cambio en el historial
    public void registrarCambio(HistorialCambiosDTO historialCambiosDTO) {
        try (HistorialCambiosDAO historialCambiosDAO = new HistorialCambiosDAO(connection)) {
            HistorialCambios historial = historialCambiosDTO.toEntity();
            historialCambiosDAO.registrarCambio(historial);
            logger.log(Level.INFO, "Cambio registrado correctamente en el historial.");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al registrar el cambio en el historial.", e);
        }
    }

    // Obtener el historial completo de cambios
    public List<HistorialCambiosDTO> obtenerHistorialCambios() {
        List<HistorialCambiosDTO> historialCambiosDTOList = new ArrayList<>();
        try (HistorialCambiosDAO historialCambiosDAO = new HistorialCambiosDAO(connection)) {
            List<HistorialCambios> historialCambiosList = historialCambiosDAO.obtenerHistorialCambios();
            for (HistorialCambios historial : historialCambiosList) {
                historialCambiosDTOList.add(HistorialCambiosDTO.fromEntity(historial));
            }
            logger.log(Level.INFO, "Historial de cambios obtenido correctamente.");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener el historial de cambios.", e);
        }
        return historialCambiosDTOList;
    }
}
