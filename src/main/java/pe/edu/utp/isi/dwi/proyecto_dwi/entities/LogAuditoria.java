package pe.edu.utp.isi.dwi.proyecto_dwi.entities;

import java.sql.Timestamp;

public class LogAuditoria {
    private int idLog; // Este atributo se obtendrá de la base de datos luego de la inserción
    private int idUsuario;
    private String accion;
    private Timestamp fecha;

    // Constructor vacío
    public LogAuditoria() {}

    // Constructor sin idLog (para crear un nuevo registro)
    public LogAuditoria(int idUsuario, String accion, Timestamp fecha) {
        this.idUsuario = idUsuario;
        this.accion = validarAccion(accion);
        this.fecha = validarFecha(fecha);
    }

    // Getters y setters
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
        this.idUsuario = idUsuario;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = validarAccion(accion);
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = validarFecha(fecha);
    }

    // Métodos de validación (sin cambios)
    private String validarAccion(String accion) {
        if (accion == null || accion.isEmpty()) {
            throw new IllegalArgumentException("La acción no puede estar vacía.");
        }
        return accion;
    }

    private Timestamp validarFecha(Timestamp fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula.");
        }
        return fecha;
    }
}
