package pe.edu.utp.isi.dwi.proyecto_dwi.dto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import pe.edu.utp.isi.dwi.proyecto_dwi.entities.Notificacion;

public class NotificacionDTO {

    private int idNotificacion;
    private int idSolicitud;
    private Timestamp fechaEnvio;
    private String tipo;
    private boolean leida;

    // Constructor vacío
    public NotificacionDTO() {
    }

    // Constructor con todos los campos
    public NotificacionDTO(int idNotificacion, int idSolicitud, Timestamp fechaEnvio, String tipo, boolean leida) {
        this.idNotificacion = idNotificacion;
        this.idSolicitud = idSolicitud;
        this.fechaEnvio = fechaEnvio;
        this.tipo = tipo;
        this.leida = leida;
    }

    // Getters y Setters
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
        this.idSolicitud = idSolicitud;
    }

    public Timestamp getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Timestamp fechaEnvio) {
        if (fechaEnvio == null) {
            throw new IllegalArgumentException("La fecha de envío no puede ser nula.");
        }
        this.fechaEnvio = fechaEnvio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        if (tipo == null || tipo.isEmpty()) {
            throw new IllegalArgumentException("El tipo de notificación no puede estar vacío.");
        }
        if (!tipo.equals(Notificacion.TIPO_INFORMACION) && !tipo.equals(Notificacion.TIPO_ADVERTENCIA) && !tipo.equals(Notificacion.TIPO_ALERTA)) {
            throw new IllegalArgumentException("Tipo de notificación no válido.");
        }
        this.tipo = tipo;
    }

    public boolean isLeida() {
        return leida;
    }

    public void setLeida(boolean leida) {
        this.leida = leida;
    }

    // Métodos para conversión de DTO a Entidad y viceversa
    public Notificacion toEntity() {
        Notificacion notificacion = new Notificacion();
        notificacion.setIdNotificacion(this.idNotificacion);
        notificacion.setIdSolicitud(this.idSolicitud);
        notificacion.setFechaEnvio(this.fechaEnvio);
        notificacion.setTipo(this.tipo);
        notificacion.setLeida(this.leida);
        return notificacion;
    }

    public static NotificacionDTO fromEntity(Notificacion notificacion) {
        if (notificacion == null) {
            throw new IllegalArgumentException("La entidad Notificacion no puede ser nula.");
        }
        return new NotificacionDTO(
                notificacion.getIdNotificacion(),
                notificacion.getIdSolicitud(),
                notificacion.getFechaEnvio(),
                notificacion.getTipo(),
                notificacion.isLeida()
        );
    }

    public static List<NotificacionDTO> fromEntities(List<Notificacion> notificaciones) {
        if (notificaciones == null) {
            throw new IllegalArgumentException("La lista de entidades Notificacion no puede ser nula.");
        }
        List<NotificacionDTO> notificacionDTOs = new ArrayList<>();
        for (Notificacion notificacion : notificaciones) {
            notificacionDTOs.add(fromEntity(notificacion));
        }
        return notificacionDTOs;
    }
}
