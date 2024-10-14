package pe.edu.utp.isi.dwi.proyecto_dwi.dto;

import java.time.LocalDateTime;
import pe.edu.utp.isi.dwi.proyecto_dwi.entities.Actividad;

public class ActividadDTO {

    private int idActividad;
    private int idAsignacion;
    private String descripcionActividad;
    private int tiempoEmpleado;
    private LocalDateTime fechaRegistro;  

    // Constructor sin parámetros
    public ActividadDTO() {
    }

    // Constructor con todos los campos
    public ActividadDTO(int idActividad, int idAsignacion, String descripcionActividad, int tiempoEmpleado, LocalDateTime fechaRegistro) {
        this.idActividad = idActividad;
        this.idAsignacion = idAsignacion;
        this.descripcionActividad = descripcionActividad;
        this.tiempoEmpleado = tiempoEmpleado;
        this.fechaRegistro = fechaRegistro;
    }

    // Getters y Setters
    public int getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(int idActividad) {
        this.idActividad = idActividad;
    }

    public int getIdAsignacion() {
        return idAsignacion;
    }

    public void setIdAsignacion(int idAsignacion) {
        this.idAsignacion = idAsignacion;
    }

    public String getDescripcionActividad() {
        return descripcionActividad;
    }

    public void setDescripcionActividad(String descripcionActividad) {
        this.descripcionActividad = descripcionActividad;
    }

    public int getTiempoEmpleado() {
        return tiempoEmpleado;
    }

    public void setTiempoEmpleado(int tiempoEmpleado) {
        this.tiempoEmpleado = tiempoEmpleado;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    // Método para convertir DTO a entidad Actividad
    public Actividad toEntity() {
        Actividad actividad = new Actividad();
        actividad.setIdActividad(this.idActividad);
        actividad.setIdAsignacion(this.idAsignacion);
        actividad.setDescripcionActividad(this.descripcionActividad);
        actividad.setTiempoEmpleado(this.tiempoEmpleado);
        actividad.setFechaRegistro(this.fechaRegistro);
        return actividad;
    }

    // Método para convertir una entidad Actividad en DTO
    public static ActividadDTO fromEntity(Actividad actividad) {
        return new ActividadDTO(
                actividad.getIdActividad(),
                actividad.getIdAsignacion(),
                actividad.getDescripcionActividad(),
                actividad.getTiempoEmpleado(),
                actividad.getFechaRegistro()
        );
    }
}
