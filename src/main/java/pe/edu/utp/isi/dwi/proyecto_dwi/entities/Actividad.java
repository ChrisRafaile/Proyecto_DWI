package pe.edu.utp.isi.dwi.proyecto_dwi.entities;

import java.time.LocalDateTime;
import pe.edu.utp.isi.dwi.proyecto_dwi.model.AuditHelper;
import pe.edu.utp.isi.dwi.proyecto_dwi.model.BaseEntity;

public class Actividad extends BaseEntity {

    private int idActividad;  // ID de la actividad (clave primaria)
    private int idAsignacion;  // Relación con la asignación correspondiente
    private String descripcionActividad;  // Descripción de la actividad
    private int tiempoEmpleado;  // Tiempo empleado en la actividad
    private LocalDateTime fechaRegistro;  // Fecha de registro de la actividad

    // Constructor vacío
    public Actividad() {
        super();  // Inicializar la fecha de creación y actualización
    }

    // Constructor con todos los campos
    public Actividad(int idActividad, int idAsignacion, String descripcionActividad, int tiempoEmpleado, LocalDateTime fechaRegistro) {
        super();  // Inicializar la fecha de creación y actualización
        this.idActividad = idActividad;
        this.idAsignacion = idAsignacion;
        this.descripcionActividad = validarDescripcion(descripcionActividad);
        this.tiempoEmpleado = tiempoEmpleado;
        this.fechaRegistro = fechaRegistro;
    }

    // Getters y Setters con validación y auditoría
    public int getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(int idActividad) {
        int valorAnterior = this.idActividad;
        this.idActividad = idActividad;
        AuditHelper.registrarCambio(
                getId(),
                "actividades",
                "id_actividad",
                String.valueOf(valorAnterior),
                String.valueOf(idActividad),
                "ID de la actividad actualizado"
        );
    }

    public int getIdAsignacion() {
        return idAsignacion;
    }

    public void setIdAsignacion(int idAsignacion) {
        int valorAnterior = this.idAsignacion;
        this.idAsignacion = idAsignacion;
        AuditHelper.registrarCambio(
                getId(),
                "actividades",
                "id_asignacion",
                String.valueOf(valorAnterior),
                String.valueOf(idAsignacion),
                "ID de asignación actualizado"
        );
    }

    public String getDescripcionActividad() {
        return descripcionActividad;
    }

    public void setDescripcionActividad(String descripcionActividad) {
        String valorAnterior = this.descripcionActividad;
        this.descripcionActividad = validarDescripcion(descripcionActividad);
        AuditHelper.registrarCambio(
                getId(),
                "actividades",
                "descripcion_actividad",
                valorAnterior,
                descripcionActividad,
                "Descripción de la actividad actualizada"
        );
    }

    public int getTiempoEmpleado() {
        return tiempoEmpleado;
    }

    public void setTiempoEmpleado(int tiempoEmpleado) {
        int valorAnterior = this.tiempoEmpleado;
        this.tiempoEmpleado = tiempoEmpleado;
        AuditHelper.registrarCambio(
                getId(),
                "actividades",
                "tiempo_empleado",
                String.valueOf(valorAnterior),
                String.valueOf(tiempoEmpleado),
                "Tiempo empleado actualizado"
        );
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        LocalDateTime valorAnterior = this.fechaRegistro;
        this.fechaRegistro = fechaRegistro;
        AuditHelper.registrarCambio(
                getId(),
                "actividades",
                "fecha_registro",
                valorAnterior != null ? valorAnterior.toString() : null,
                fechaRegistro != null ? fechaRegistro.toString() : null,
                "Fecha de registro actualizada"
        );
    }

    // Métodos privados para validaciones
    private String validarDescripcion(String descripcion) {
        if (descripcion == null || descripcion.isEmpty()) {
            throw new IllegalArgumentException("La descripción no puede estar vacía.");
        }
        return descripcion;
    }

    @Override
    public String toString() {
        return "Actividad{"
                + "idActividad=" + idActividad
                + ", idAsignacion=" + idAsignacion
                + ", descripcionActividad='" + descripcionActividad + '\''
                + ", tiempoEmpleado=" + tiempoEmpleado
                + ", fechaRegistro=" + fechaRegistro
                + ", fechaCreacion=" + getCreatedAt()
                + ", fechaActualizacion=" + getUpdatedAt()
                + '}';
    }
}
