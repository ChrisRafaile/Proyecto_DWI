package pe.edu.utp.isi.dwi.proyecto_dwi.service;

import pe.edu.utp.isi.dwi.proyecto_dwi.dao.PermisosDAO;
import pe.edu.utp.isi.dwi.proyecto_dwi.dto.PermisoDTO;
import pe.edu.utp.isi.dwi.proyecto_dwi.factory.DAOFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PermisosFacade {
    private static final Logger logger = Logger.getLogger(PermisosFacade.class.getName());
    private final PermisosDAO permisosDAO;

    // Constructor para inicializar el DAO usando DAOFactory
    public PermisosFacade() {
        this.permisosDAO = DAOFactory.getInstance().getPermisosDAO();
    }

    // Asignar un permiso
    public void asignarPermiso(PermisoDTO permisoDTO) {
        if (permisoDTO == null) {
            throw new IllegalArgumentException("El PermisoDTO no puede ser nulo");
        }
        try {
            permisosDAO.insertarPermiso(permisoDTO);
            logger.log(Level.INFO, "Permiso asignado exitosamente.");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al asignar el permiso", e);
            throw new RuntimeException("Error al asignar el permiso", e);
        }
    }

    // Quitar un permiso por ID
    public void quitarPermiso(int idPermiso) {
        if (idPermiso <= 0) {
            throw new IllegalArgumentException("El ID del permiso debe ser un número positivo.");
        }
        try {
            permisosDAO.eliminarPermiso(idPermiso);
            logger.log(Level.INFO, "Permiso quitado exitosamente con ID: {0}", idPermiso);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al quitar el permiso", e);
            throw new RuntimeException("Error al quitar el permiso", e);
        }
    }

    // Listar todos los permisos
    public List<PermisoDTO> listarPermisos() {
        try {
            return permisosDAO.obtenerPermisos();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al listar los permisos", e);
            throw new RuntimeException("Error al listar los permisos", e);
        }
    }

    // Obtener un permiso por ID
    public PermisoDTO obtenerPermisoPorId(int idPermiso) {
        if (idPermiso <= 0) {
            throw new IllegalArgumentException("El ID del permiso debe ser un número positivo.");
        }
        try {
            return permisosDAO.obtenerPermisoPorId(idPermiso);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener el permiso por ID", e);
            throw new RuntimeException("Error al obtener el permiso", e);
        }
    }

    // Actualizar un permiso
    public void actualizarPermiso(PermisoDTO permisoDTO) {
        if (permisoDTO == null) {
            throw new IllegalArgumentException("El PermisoDTO no puede ser nulo");
        }
        try {
            permisosDAO.actualizarPermiso(permisoDTO);
            logger.log(Level.INFO, "Permiso actualizado exitosamente.");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al actualizar el permiso", e);
            throw new RuntimeException("Error al actualizar el permiso", e);
        }
    }
}
