package pe.edu.utp.isi.dwi.proyecto_dwi.dto;

import pe.edu.utp.isi.dwi.proyecto_dwi.entities.Solicitud.EstadoSolicitud;
import pe.edu.utp.isi.dwi.proyecto_dwi.entities.Solicitud;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * DTO para la entidad Solicitud, utilizado para transferir datos entre capas.
 */
public class SolicitudDTO {

    private final int idSolicitud;
    private final int idUsuario;
    private final String tipoSolicitud;
    private final String descripcion;
    private final EstadoSolicitud estado; // Cambiado a EstadoSolicitud (Enum)
    private final Timestamp fechaCreacion;
    private final Timestamp fechaFinalizacion;

    // Constructor para inicializar todos los campos
    public SolicitudDTO(int idSolicitud, int idUsuario, String tipoSolicitud, String descripcion, EstadoSolicitud estado, Timestamp fechaCreacion, Timestamp fechaFinalizacion) {
        this.idSolicitud = idSolicitud;
        this.idUsuario = idUsuario;
        this.tipoSolicitud = tipoSolicitud;
        this.descripcion = descripcion;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion != null ? fechaCreacion : Timestamp.valueOf(LocalDateTime.now()); // Manejo para evitar valores nulos
        this.fechaFinalizacion = fechaFinalizacion; // Puede ser null
    }

    // Constructor vacío para facilitar la creación sin valores iniciales (si es necesario)
    public SolicitudDTO() {
        this(0, 0, null, null, null, null, null);
    }

    // Getters
    public int getIdSolicitud() {
        return idSolicitud;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getTipoSolicitud() {
        return tipoSolicitud;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public Timestamp getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    /**
     * Convierte este DTO a una entidad Solicitud.
     *
     * @return Solicitud la entidad correspondiente.
     */
    public Solicitud toEntity() {
        return new Solicitud(
                this.idSolicitud,
                this.idUsuario,
                this.tipoSolicitud,
                this.descripcion,
                this.estado,
                this.fechaCreacion,
                this.fechaFinalizacion
        );
    }

    /**
     * Crea un DTO a partir de una entidad Solicitud.
     *
     * @param solicitud La entidad Solicitud a convertir.
     * @return SolicitudDTO el DTO correspondiente.
     */
    public static SolicitudDTO fromEntity(Solicitud solicitud) {
        if (solicitud == null) {
            return null;
        }
        return new SolicitudDTO(
                solicitud.getIdSolicitud(),
                solicitud.getIdUsuario(),
                solicitud.getTipoSolicitud(),
                solicitud.getDescripcion(),
                solicitud.getEstado(),
                solicitud.getFechaCreacion(),
                solicitud.getFechaFinalizacion()
        );
    }
}
