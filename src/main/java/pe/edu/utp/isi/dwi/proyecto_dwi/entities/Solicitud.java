package pe.edu.utp.isi.dwi.proyecto_dwi.entities;

import java.sql.Timestamp;

/**
 * Entidad para representar una Solicitud dentro del sistema.
 */
public class Solicitud {

    private int idSolicitud;
    private int idUsuario;
    private String tipoSolicitud;
    private String descripcion;
    private EstadoSolicitud estado;
    private Timestamp fechaCreacion;
    private Timestamp fechaFinalizacion;

    /**
     * Estados permitidos para una solicitud.
     */
    public enum EstadoSolicitud {
        PENDIENTE,
        EN_PROCESO,
        FINALIZADA
    }

    // Constructor vacío.
    public Solicitud() {}

    // Constructor con todos los campos.
    public Solicitud(int idSolicitud, int idUsuario, String tipoSolicitud, String descripcion, EstadoSolicitud estado, Timestamp fechaCreacion, Timestamp fechaFinalizacion) {
        this.idSolicitud = idSolicitud;
        this.idUsuario = idUsuario;
        this.tipoSolicitud = validarTipoSolicitud(tipoSolicitud);
        this.descripcion = validarDescripcion(descripcion);
        this.estado = estado;
        this.fechaCreacion = validarFecha(fechaCreacion, "Fecha de creación");
        this.fechaFinalizacion = validarFecha(fechaFinalizacion, "Fecha de finalización");
    }

    // Getters y Setters con validación.
    public int getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(int idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTipoSolicitud() {
        return tipoSolicitud;
    }

    public void setTipoSolicitud(String tipoSolicitud) {
        this.tipoSolicitud = validarTipoSolicitud(tipoSolicitud);
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = validarDescripcion(descripcion);
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public void setEstado(EstadoSolicitud estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo.");
        }
        this.estado = estado;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = validarFecha(fechaCreacion, "Fecha de creación");
    }

    public Timestamp getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(Timestamp fechaFinalizacion) {
        this.fechaFinalizacion = validarFecha(fechaFinalizacion, "Fecha de finalización");
    }

    // Métodos de validación.
    private String validarTipoSolicitud(String tipoSolicitud) {
        if (tipoSolicitud == null || tipoSolicitud.isEmpty()) {
            throw new IllegalArgumentException("El tipo de solicitud no puede estar vacío.");
        }
        if (tipoSolicitud.length() > 50) {
            throw new IllegalArgumentException("El tipo de solicitud no puede tener más de 50 caracteres.");
        }
        return tipoSolicitud;
    }

    private String validarDescripcion(String descripcion) {
        if (descripcion == null || descripcion.isEmpty()) {
            throw new IllegalArgumentException("La descripción no puede estar vacía.");
        }
        return descripcion;
    }

    private Timestamp validarFecha(Timestamp fecha, String campo) {
        if (fecha == null) {
            throw new IllegalArgumentException(campo + " no puede ser nula.");
        }
        return fecha;
    }
}
