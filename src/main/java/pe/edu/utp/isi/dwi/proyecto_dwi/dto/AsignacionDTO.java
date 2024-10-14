package pe.edu.utp.isi.dwi.proyecto_dwi.dto;

import java.time.LocalDateTime;
import pe.edu.utp.isi.dwi.proyecto_dwi.entities.Asignacion;

public class AsignacionDTO {

    private int idAsignacion;
    private int idSolicitud;
    private int idColaborador;
    private int idCoordinador;
    private LocalDateTime fechaAsignacion;

    // Constructor vacío
    public AsignacionDTO() {
    }

    // Constructor con todos los campos
    public AsignacionDTO(int idAsignacion, int idSolicitud, int idColaborador, int idCoordinador, LocalDateTime fechaAsignacion) {
        this.idAsignacion = idAsignacion;
        this.idSolicitud = idSolicitud;
        this.idColaborador = idColaborador;
        this.idCoordinador = idCoordinador;
        this.fechaAsignacion = fechaAsignacion;
    }

    // Getters y Setters
    public int getIdAsignacion() {
        return idAsignacion;
    }

    public void setIdAsignacion(int idAsignacion) {
        this.idAsignacion = idAsignacion;
    }

    public int getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(int idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public int getIdColaborador() {
        return idColaborador;
    }

    public void setIdColaborador(int idColaborador) {
        this.idColaborador = idColaborador;
    }

    public int getIdCoordinador() {
        return idCoordinador;
    }

    public void setIdCoordinador(int idCoordinador) {
        this.idCoordinador = idCoordinador;
    }

    public LocalDateTime getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(LocalDateTime fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    // Método para convertir DTO a entidad Asignacion
    public Asignacion toEntity() {
        Asignacion asignacion = new Asignacion();
        asignacion.setIdAsignacion(this.idAsignacion);
        asignacion.setIdSolicitud(this.idSolicitud);
        asignacion.setIdColaborador(this.idColaborador);
        asignacion.setIdCoordinador(this.idCoordinador);
        asignacion.setFechaAsignacion(this.fechaAsignacion);
        return asignacion;
    }

    // Método para convertir una entidad Asignacion en DTO
    public static AsignacionDTO fromEntity(Asignacion asignacion) {
        return new AsignacionDTO(
                asignacion.getIdAsignacion(),
                asignacion.getIdSolicitud(),
                asignacion.getIdColaborador(),
                asignacion.getIdCoordinador(),
                asignacion.getFechaAsignacion()
        );
    }
}
