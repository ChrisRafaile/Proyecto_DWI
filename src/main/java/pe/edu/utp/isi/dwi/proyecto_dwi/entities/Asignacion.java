package pe.edu.utp.isi.dwi.proyecto_dwi.entities;

import pe.edu.utp.isi.dwi.proyecto_dwi.model.AuditHelper;
import pe.edu.utp.isi.dwi.proyecto_dwi.model.BaseEntity;

import java.time.LocalDateTime;

public class Asignacion extends BaseEntity {

    private int idAsignacion; // ID de la asignación (clave primaria)
    private int idSolicitud;  // Relación con la solicitud correspondiente
    private int idColaborador; // Relación con el colaborador que realiza la asignación
    private int idCoordinador; // Coordinador responsable de la asignación
    private LocalDateTime fechaAsignacion; // Fecha de la asignación

    // Constructor vacío
    public Asignacion() {
        super();  // Inicializar la fecha de creación y actualización
    }

    // Constructor con parámetros
    public Asignacion(int idAsignacion, int idSolicitud, int idColaborador, int idCoordinador, LocalDateTime fechaAsignacion) {
        super();  // Inicializar la fecha de creación y actualización
        this.idAsignacion = idAsignacion;
        this.idSolicitud = idSolicitud;
        this.idColaborador = idColaborador;
        this.idCoordinador = idCoordinador;
        this.fechaAsignacion = fechaAsignacion;
    }

    // Getters y Setters con validación y auditoría
    public int getIdAsignacion() {
        return idAsignacion;
    }

    public void setIdAsignacion(int idAsignacion) {
        int valorAnterior = this.idAsignacion;
        this.idAsignacion = idAsignacion;
        AuditHelper.registrarCambio(
                getId(),
                "asignaciones",
                "id_asignacion",
                String.valueOf(valorAnterior),
                String.valueOf(idAsignacion),
                "ID de la asignación actualizado"
        );
    }

    public int getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(int idSolicitud) {
        int valorAnterior = this.idSolicitud;
        this.idSolicitud = idSolicitud;
        AuditHelper.registrarCambio(
                getId(),
                "asignaciones",
                "id_solicitud",
                String.valueOf(valorAnterior),
                String.valueOf(idSolicitud),
                "ID de la solicitud actualizado"
        );
    }

    public int getIdColaborador() {
        return idColaborador;
    }

    public void setIdColaborador(int idColaborador) {
        int valorAnterior = this.idColaborador;
        this.idColaborador = idColaborador;
        AuditHelper.registrarCambio(
                getId(),
                "asignaciones",
                "id_colaborador",
                String.valueOf(valorAnterior),
                String.valueOf(idColaborador),
                "ID del colaborador actualizado"
        );
    }

    public int getIdCoordinador() {
        return idCoordinador;
    }

    public void setIdCoordinador(int idCoordinador) {
        int valorAnterior = this.idCoordinador;
        this.idCoordinador = idCoordinador;
        AuditHelper.registrarCambio(
                getId(),
                "asignaciones",
                "id_coordinador",
                String.valueOf(valorAnterior),
                String.valueOf(idCoordinador),
                "ID del coordinador actualizado"
        );
    }

    public LocalDateTime getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(LocalDateTime fechaAsignacion) {
        LocalDateTime valorAnterior = this.fechaAsignacion;
        this.fechaAsignacion = fechaAsignacion;
        AuditHelper.registrarCambio(
                getId(),
                "asignaciones",
                "fecha_asignacion",
                valorAnterior != null ? valorAnterior.toString() : null,
                fechaAsignacion != null ? fechaAsignacion.toString() : null,
                "Fecha de la asignación actualizada"
        );
    }

    @Override
    public String toString() {
        return "Asignacion{" +
                "idAsignacion=" + idAsignacion +
                ", idSolicitud=" + idSolicitud +
                ", idColaborador=" + idColaborador +
                ", idCoordinador=" + idCoordinador +
                ", fechaAsignacion=" + fechaAsignacion +
                ", fechaCreacion=" + getCreatedAt() +
                ", fechaActualizacion=" + getUpdatedAt() +
                '}';
    }
}
