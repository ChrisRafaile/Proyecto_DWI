package pe.edu.utp.isi.dwi.proyecto_dwi.model;

import java.time.LocalDateTime;

public abstract class BaseEntity implements Auditable {

    private int id;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    // Constructor
    public BaseEntity() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return fechaCreacion;
    }

    @Override
    public void setCreatedAt(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Override
    public LocalDateTime getUpdatedAt() {
        return fechaActualizacion;
    }

    @Override
    public void setUpdatedAt(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    // Alias para mantener compatibilidad con código que use getFechaCreacion y getFechaActualizacion
    public LocalDateTime getFechaCreacion() {
        return getCreatedAt();
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        setCreatedAt(fechaCreacion);
    }

    public LocalDateTime getFechaActualizacion() {
        return getUpdatedAt();
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        setUpdatedAt(fechaActualizacion);
    }

    // Método común para actualizar la fecha de actualización
    public void actualizarFecha() {
        this.fechaActualizacion = LocalDateTime.now();
    }
}
