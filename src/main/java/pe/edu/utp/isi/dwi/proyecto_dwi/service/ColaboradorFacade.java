package pe.edu.utp.isi.dwi.proyecto_dwi.service;

import pe.edu.utp.isi.dwi.proyecto_dwi.dto.ColaboradorDTO;
import pe.edu.utp.isi.dwi.proyecto_dwi.entities.Colaborador;
import pe.edu.utp.isi.dwi.proyecto_dwi.factory.DAOFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import pe.edu.utp.isi.dwi.proyecto_dwi.dao.ColaboradorDAO;

public class ColaboradorFacade {

    private static final Logger logger = Logger.getLogger(ColaboradorFacade.class.getName());
    private final ColaboradorDAO colaboradorDAO;

    // Constructor que inicializa el ColaboradorDAO usando DAOFactory
    public ColaboradorFacade() {
        this.colaboradorDAO = DAOFactory.getInstance().getColaboradorDAO();
    }

    // Obtener colaborador por ID
    public ColaboradorDTO obtenerColaboradorPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID del colaborador debe ser un número positivo.");
        }
        try {
            Colaborador colaborador = colaboradorDAO.obtenerColaboradorPorId(id);
            logger.log(Level.INFO, "Colaborador obtenido correctamente con ID: {0}", id);
            return colaborador != null ? ColaboradorDTO.fromEntity(colaborador) : null;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener el colaborador con ID: " + id, e);
            throw new RuntimeException("Error al obtener el colaborador con ID: " + id, e);
        }
    }

    // Listar todos los colaboradores
    public List<ColaboradorDTO> listarColaboradores() {
        try {
            List<Colaborador> colaboradores = colaboradorDAO.obtenerColaboradores();
            logger.log(Level.INFO, "Se listaron correctamente los colaboradores.");
            return colaboradores.stream().map(ColaboradorDTO::fromEntity).collect(Collectors.toList());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al listar los colaboradores", e);
            throw new RuntimeException("Error al listar los colaboradores", e);
        }
    }

    // Registrar un nuevo colaborador
    public void registrarColaborador(ColaboradorDTO colaboradorDTO) {
        if (colaboradorDTO == null) {
            throw new IllegalArgumentException("El objeto ColaboradorDTO no puede ser nulo.");
        }
        try {
            Colaborador colaborador = colaboradorDTO.toEntity();
            colaboradorDAO.registrarColaborador(colaborador);
            logger.log(Level.INFO, "Colaborador registrado exitosamente: {0}", colaboradorDTO);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al registrar el colaborador", e);
            throw new RuntimeException("Error al registrar el colaborador", e);
        }
    }

    // Actualizar un colaborador existente
    public void actualizarColaborador(ColaboradorDTO colaboradorDTO) {
        if (colaboradorDTO == null || colaboradorDTO.getIdColaborador() <= 0) {
            throw new IllegalArgumentException("El objeto ColaboradorDTO no puede ser nulo y debe tener un ID válido.");
        }
        try {
            colaboradorDAO.actualizarColaborador(colaboradorDTO.getIdColaborador(), colaboradorDTO.getCargo());
            logger.log(Level.INFO, "Colaborador actualizado exitosamente: {0}", colaboradorDTO);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al actualizar el colaborador", e);
            throw new RuntimeException("Error al actualizar el colaborador", e);
        }
    }

    // Eliminar un colaborador por su ID
    public void eliminarColaborador(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID del colaborador debe ser un número positivo.");
        }
        try {
            colaboradorDAO.eliminarColaborador(id);
            logger.log(Level.INFO, "Colaborador eliminado exitosamente con ID: {0}", id);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al eliminar el colaborador con ID: " + id, e);
            throw new RuntimeException("Error al eliminar el colaborador con ID: " + id, e);
        }
    }
}
