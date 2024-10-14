package pe.edu.utp.isi.dwi.proyecto_dwi.dto;

import pe.edu.utp.isi.dwi.proyecto_dwi.entities.LogAuditoria;

import java.sql.Timestamp;

public class LogAuditoriaDTO {

    private int idLog;
    private int idUsuario;
    private String accion;
    private Timestamp fecha;

    // Constructor vacío
    public LogAuditoriaDTO() {}

    // Constructor con todos los campos
    public LogAuditoriaDTO(int idLog, int idUsuario, String accion, Timestamp fecha) {
        if (idUsuario <= 0) {
            throw new IllegalArgumentException("El ID del usuario debe ser un número positivo.");
        }
        if (accion == null || accion.trim().isEmpty()) {
            throw new IllegalArgumentException("La acción no puede estar vacía.");
        }
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula.");
        }
        this.idLog = idLog;
        this.idUsuario = idUsuario;
        this.accion = accion;
        this.fecha = fecha;
    }

    // Getters y Setters con validación
    public int getIdLog() {
        return idLog;
    }

    public void setIdLog(int idLog) {
        this.idLog = idLog;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        if (idUsuario <= 0) {
            throw new IllegalArgumentException("El ID del usuario debe ser un número positivo.");
        }
        this.idUsuario = idUsuario;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        if (accion == null || accion.trim().isEmpty()) {
            throw new IllegalArgumentException("La acción no puede estar vacía.");
        }
        this.accion = accion;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula.");
        }
        this.fecha = fecha;
    }

    // Método para convertir DTO a entidad
    public LogAuditoria toEntity() {
        LogAuditoria logAuditoria = new LogAuditoria();
        logAuditoria.setIdLog(this.idLog);
        logAuditoria.setIdUsuario(this.idUsuario);
        logAuditoria.setAccion(this.accion);
        logAuditoria.setFecha(this.fecha);
        return logAuditoria;
    }

    @Override
    public String toString() {
        return "LogAuditoriaDTO{" +
                "idLog=" + idLog +
                ", idUsuario=" + idUsuario +
                ", accion='" + accion + '\'' +
                ", fecha=" + fecha +
                '}';
    }
}
