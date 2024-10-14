package pe.edu.utp.isi.dwi.proyecto_dwi.service;

import pe.edu.utp.isi.dwi.proyecto_dwi.dao.SolicitudDAO;
import pe.edu.utp.isi.dwi.proyecto_dwi.dto.SolicitudDTO;
import pe.edu.utp.isi.dwi.proyecto_dwi.entities.Solicitud;
import pe.edu.utp.isi.dwi.proyecto_dwi.factory.DAOFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class SolicitudFacade {

    private static final Logger logger = Logger.getLogger(SolicitudFacade.class.getName());
    private final DAOFactory daoFactory;

    // Constructor que obtiene la DAOFactory
    public SolicitudFacade() {
        this.daoFactory = DAOFactory.getInstance();
    }

    // Registrar una nueva solicitud
    public void registrarSolicitud(SolicitudDTO solicitudDTO) {
        validarSolicitudDTO(solicitudDTO);
        try {
            Connection connection = daoFactory.getConnection();
            connection.setAutoCommit(false); // Iniciar transacción

            try (SolicitudDAO solicitudDAO = daoFactory.getSolicitudDAO()) {
                Solicitud solicitud = solicitudDTO.toEntity();
                solicitudDAO.registrarSolicitud(solicitud);

                connection.commit(); // Confirmar transacción
                logger.log(Level.INFO, "Solicitud registrada exitosamente: {0}", solicitudDTO);
            } catch (SQLException e) {
                connection.rollback(); // Revertir transacción en caso de error
                logger.log(Level.SEVERE, "Error al registrar la solicitud, se revertirán los cambios", e);
                throw new RuntimeException("Error al registrar la solicitud", e);
            } finally {
                connection.setAutoCommit(true); // Restablecer el modo de autocommit
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al manejar la conexión durante el registro de la solicitud", e);
            throw new RuntimeException("Error al manejar la conexión durante el registro de la solicitud", e);
        }
    }

    // Cambiar el estado de una solicitud
    public void cambiarEstadoSolicitud(int id, Solicitud.EstadoSolicitud nuevoEstado) {
        validarId(id, "El ID de la solicitud debe ser un número positivo.");
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El nuevo estado no puede ser nulo.");
        }

        try (SolicitudDAO solicitudDAO = daoFactory.getSolicitudDAO()) {
            solicitudDAO.cambiarEstadoSolicitud(id, nuevoEstado);
            logger.log(Level.INFO, "Estado de solicitud con ID {0} cambiado a {1}", new Object[]{id, nuevoEstado});
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al cambiar el estado de la solicitud", e);
            throw new RuntimeException("Error al cambiar el estado de la solicitud", e);
        }
    }

    // Actualizar solicitud
    public void actualizarSolicitud(SolicitudDTO solicitudDTO) {
        validarSolicitudDTO(solicitudDTO);
        try (SolicitudDAO solicitudDAO = daoFactory.getSolicitudDAO()) {
            Solicitud solicitud = solicitudDTO.toEntity();
            solicitudDAO.actualizarSolicitud(solicitud);
            logger.log(Level.INFO, "Solicitud actualizada exitosamente con ID {0}", solicitudDTO.getIdSolicitud());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al actualizar la solicitud con ID " + solicitudDTO.getIdSolicitud(), e);
            throw new RuntimeException("Error al actualizar la solicitud", e);
        }
    }

    // Eliminar una solicitud por ID
    public void eliminarSolicitud(int idSolicitud) {
        validarId(idSolicitud, "El ID de la solicitud debe ser un número positivo.");
        try (SolicitudDAO solicitudDAO = daoFactory.getSolicitudDAO()) {
            solicitudDAO.eliminarSolicitud(idSolicitud);
            logger.log(Level.INFO, "Solicitud eliminada con ID {0}", idSolicitud);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al eliminar la solicitud con ID " + idSolicitud, e);
            throw new RuntimeException("Error al eliminar la solicitud", e);
        }
    }

    // Filtrar solicitudes por fechas
    public List<SolicitudDTO> filtrarSolicitudesPorFecha(Timestamp fechaInicio, Timestamp fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin no pueden ser nulas.");
        }

        try (SolicitudDAO solicitudDAO = daoFactory.getSolicitudDAO()) {
            List<Solicitud> solicitudes = solicitudDAO.filtrarSolicitudesPorFecha(fechaInicio, fechaFin);
            logger.log(Level.INFO, "Solicitudes filtradas correctamente entre {0} y {1}", new Object[]{fechaInicio, fechaFin});
            return solicitudes.stream().map(SolicitudDTO::fromEntity).collect(Collectors.toList());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al filtrar las solicitudes por fecha", e);
            throw new RuntimeException("Error al filtrar las solicitudes por fecha", e);
        }
    }

    // Obtener solicitud por ID
    public SolicitudDTO obtenerSolicitudPorId(int idSolicitud) {
        validarId(idSolicitud, "El ID de la solicitud debe ser un número positivo.");
        try (SolicitudDAO solicitudDAO = daoFactory.getSolicitudDAO()) {
            Solicitud solicitud = solicitudDAO.obtenerSolicitudPorId(idSolicitud);
            if (solicitud != null) {
                logger.log(Level.INFO, "Solicitud obtenida con ID {0}", idSolicitud);
                return SolicitudDTO.fromEntity(solicitud);
            } else {
                logger.log(Level.WARNING, "Solicitud no encontrada con ID {0}", idSolicitud);
                return null;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener la solicitud con ID " + idSolicitud, e);
            throw new RuntimeException("Error al obtener la solicitud", e);
        }
    }

    // Listar todas las solicitudes
    public List<SolicitudDTO> listarSolicitudes() {
        try (SolicitudDAO solicitudDAO = daoFactory.getSolicitudDAO()) {
            List<Solicitud> solicitudes = solicitudDAO.listarSolicitudes();
            logger.log(Level.INFO, "Solicitudes listadas correctamente");
            return solicitudes.stream().map(SolicitudDTO::fromEntity).collect(Collectors.toList());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al listar las solicitudes", e);
            throw new RuntimeException("Error al listar las solicitudes", e);
        }
    }

    // Métodos auxiliares para validar entradas
    private void validarId(int id, String mensaje) {
        if (id <= 0) {
            throw new IllegalArgumentException(mensaje);
        }
    }

    private void validarSolicitudDTO(SolicitudDTO solicitudDTO) {
        if (solicitudDTO == null) {
            throw new IllegalArgumentException("El objeto SolicitudDTO no puede ser nulo.");
        }
    }
}
