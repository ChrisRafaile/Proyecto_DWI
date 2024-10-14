package pe.edu.utp.isi.dwi.proyecto_dwi.service;

import pe.edu.utp.isi.dwi.proyecto_dwi.dao.AsignacionDAO;
import pe.edu.utp.isi.dwi.proyecto_dwi.dto.AsignacionDTO;
import pe.edu.utp.isi.dwi.proyecto_dwi.factory.DAOFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AsignacionFacade {

    private static final Logger logger = Logger.getLogger(AsignacionFacade.class.getName());
    private final AsignacionDAO asignacionDAO;

    // Constructor que inicializa el AsignacionDAO usando DAOFactory
    public AsignacionFacade() {
        this.asignacionDAO = DAOFactory.getInstance().getAsignacionDAO();
    }

    // Registrar una nueva asignación
    public void registrarAsignacion(AsignacionDTO asignacionDTO) {
        if (asignacionDTO == null) {
            throw new IllegalArgumentException("El objeto AsignacionDTO no puede ser nulo.");
        }
        try {
            asignacionDAO.registrarAsignacion(asignacionDTO);
            logger.log(Level.INFO, "Asignación registrada exitosamente: {0}", asignacionDTO);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al registrar la asignación", e);
            throw new RuntimeException("Error al registrar la asignación", e);
        }
    }

    // Listar todas las asignaciones
    public List<AsignacionDTO> listarAsignaciones() {
        try {
            return asignacionDAO.obtenerAsignaciones();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al listar las asignaciones", e);
            throw new RuntimeException("Error al listar las asignaciones", e);
        }
    }

    // Eliminar una asignación
    public void eliminarAsignacion(int idAsignacion) {
        if (idAsignacion <= 0) {
            throw new IllegalArgumentException("El ID de la asignación debe ser un número positivo.");
        }
        try {
            asignacionDAO.eliminarAsignacion(idAsignacion);
            logger.log(Level.INFO, "Asignación con ID {0} eliminada exitosamente.", idAsignacion);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al eliminar la asignación", e);
            throw new RuntimeException("Error al eliminar la asignación", e);
        }
    }
}
