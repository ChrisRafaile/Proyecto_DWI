package pe.edu.utp.isi.dwi.proyecto_dwi.service;

import pe.edu.utp.isi.dwi.proyecto_dwi.dao.ActividadDAO;
import pe.edu.utp.isi.dwi.proyecto_dwi.dto.ActividadDTO;
import pe.edu.utp.isi.dwi.proyecto_dwi.factory.DAOFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ActividadFacade {

    private static final Logger logger = Logger.getLogger(ActividadFacade.class.getName());
    private final ActividadDAO actividadDAO;

    // Constructor que inicializa el ActividadDAO usando DAOFactory
    public ActividadFacade() {
        this.actividadDAO = DAOFactory.getInstance().getActividadDAO();
    }

    // Registrar una nueva actividad
    public void registrarActividad(ActividadDTO actividadDTO) {
        if (actividadDTO == null) {
            throw new IllegalArgumentException("El objeto ActividadDTO no puede ser nulo.");
        }
        try {
            actividadDAO.registrarActividad(actividadDTO);
            logger.log(Level.INFO, "Actividad registrada correctamente.");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al registrar la actividad", e);
            throw new RuntimeException("Error al registrar la actividad", e);
        }
    }

    // Obtener una actividad por ID
    public ActividadDTO obtenerActividadPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID de la actividad debe ser un número positivo.");
        }
        try {
            return actividadDAO.obtenerActividadPorId(id);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener la actividad con ID: " + id, e);
            throw new RuntimeException("Error al obtener la actividad", e);
        }
    }

    // Listar todas las actividades
    public List<ActividadDTO> listarActividades() {
        try {
            return actividadDAO.obtenerActividades();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al listar las actividades", e);
            throw new RuntimeException("Error al listar las actividades", e);
        }
    }

    // Actualizar una actividad existente
    public void actualizarActividad(ActividadDTO actividadDTO) {
        if (actividadDTO == null || actividadDTO.getIdActividad() <= 0) {
            throw new IllegalArgumentException("El objeto ActividadDTO no puede ser nulo y debe tener un ID válido.");
        }
        try {
            actividadDAO.actualizarActividad(actividadDTO);
            logger.log(Level.INFO, "Actividad actualizada correctamente.");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al actualizar la actividad", e);
            throw new RuntimeException("Error al actualizar la actividad", e);
        }
    }

    // Eliminar una actividad por su ID
    public void eliminarActividad(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID de la actividad debe ser un número positivo.");
        }
        try {
            actividadDAO.eliminarActividad(id);
            logger.log(Level.INFO, "Actividad eliminada correctamente con ID: {0}", id);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al eliminar la actividad con ID: " + id, e);
            throw new RuntimeException("Error al eliminar la actividad", e);
        }
    }
}
