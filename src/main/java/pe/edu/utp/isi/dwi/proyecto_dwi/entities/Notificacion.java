package pe.edu.utp.isi.dwi.proyecto_dwi.entities;

import pe.edu.utp.isi.dwi.proyecto_dwi.model.AuditHelper;

import java.sql.Timestamp;

public class Notificacion {

    private int idNotificacion;  // ID de la notificación
    private int idSolicitud;     // ID de la solicitud relacionada
    private String tipo;         // Tipo de la notificación
    private Timestamp fechaEnvio; // Fecha de envío de la notificación
    private boolean leida;       // Estado de la notificación (leída o no leída)

    // Definir tipos de notificación permitidos
    public static final String TIPO_INFORMACION = "informacion";
    public static final String TIPO_ADVERTENCIA = "advertencia";
    public static final String TIPO_ALERTA = "alerta";

    // Constructor vacío
    public Notificacion() {}

    // Constructor con todos los campos
    public Notificacion(int idNotificacion, int idSolicitud, String tipo, Timestamp fechaEnvio, boolean leida) {
        this.idNotificacion = idNotificacion;
        this.idSolicitud = idSolicitud;
        this.tipo = validarTipo(tipo);
        this.fechaEnvio = validarFechaEnvio(fechaEnvio);
        this.leida = leida;
    }

    // Getters y Setters con validación y auditoría
    public int getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(int idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public int getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(int idSolicitud) {
        int valorAnterior = this.idSolicitud;
        this.idSolicitud = idSolicitud;
        registrarCambio("id_solicitud", String.valueOf(valorAnterior), String.valueOf(idSolicitud), "ID de la solicitud de notificación actualizado");
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        String valorAnterior = this.tipo;
        this.tipo = validarTipo(tipo);
        registrarCambio("tipo", valorAnterior, tipo, "Tipo de notificación actualizado");
    }

    public Timestamp getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Timestamp fechaEnvio) {
        Timestamp valorAnterior = this.fechaEnvio;
        this.fechaEnvio = validarFechaEnvio(fechaEnvio);
        registrarCambio("fecha_envio", String.valueOf(valorAnterior), String.valueOf(fechaEnvio), "Fecha de envío de la notificación actualizada");
    }

    public boolean isLeida() {
        return leida;
    }

    public void setLeida(boolean leida) {
        boolean valorAnterior = this.leida;
        this.leida = leida;
        registrarCambio("leida", valorAnterior ? "leída" : "no leída", leida ? "leída" : "no leída", "Estado de lectura de la notificación actualizado");
    }

    // Métodos privados para validaciones
    private String validarTipo(String tipo) {
        if (tipo == null || tipo.isEmpty()) {
            throw new IllegalArgumentException("El tipo de notificación no puede estar vacío.");
        }
        if (!tipo.equals(TIPO_INFORMACION) && !tipo.equals(TIPO_ADVERTENCIA) && !tipo.equals(TIPO_ALERTA)) {
            throw new IllegalArgumentException("Tipo de notificación no válido.");
        }
        return tipo;
    }

    private Timestamp validarFechaEnvio(Timestamp fechaEnvio) {
        if (fechaEnvio == null) {
            throw new IllegalArgumentException("La fecha de envío no puede ser nula.");
        }
        return fechaEnvio;
    }

    // Método para registrar cambios en los atributos
    private void registrarCambio(String campo, String valorAnterior, String nuevoValor, String descripcion) {
        AuditHelper.registrarCambio(
                idNotificacion,
                "notificacion",
                campo,
                valorAnterior,
                nuevoValor,
                descripcion
        );
    }

    @Override
    public String toString() {
        return "Notificacion{" +
                "idNotificacion=" + idNotificacion +
                ", idSolicitud=" + idSolicitud +
                ", tipo='" + tipo + '\'' +
                ", fechaEnvio=" + fechaEnvio +
                ", leida=" + leida +
                '}';
    }
}
