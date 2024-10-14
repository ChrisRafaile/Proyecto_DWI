package pe.edu.utp.isi.dwi.proyecto_dwi.dto;

import java.sql.Timestamp;
import pe.edu.utp.isi.dwi.proyecto_dwi.entities.HistorialCambios;

public class HistorialCambiosDTO {

    private int idHistorial;  // ID del historial de cambios
    private int idUsuario;    // ID del usuario que hizo el cambio
    private String descripcionCambio; // Descripción del cambio realizado
    private Timestamp fechaCambio;    // Fecha del cambio realizado
    private String tablaAfectada;     // Tabla afectada por el cambio
    private String campoAfectado;     // Campo afectado en la tabla
    private String valorAnterior;     // Valor anterior del campo
    private String valorNuevo;        // Nuevo valor del campo

    // Constructor vacío
    public HistorialCambiosDTO() {
    }

    // Constructor con todos los campos
    public HistorialCambiosDTO(int idHistorial, int idUsuario, String descripcionCambio, Timestamp fechaCambio,
            String tablaAfectada, String campoAfectado, String valorAnterior, String valorNuevo) {
        this.idHistorial = idHistorial;
        this.idUsuario = idUsuario;
        this.descripcionCambio = descripcionCambio;
        this.fechaCambio = fechaCambio;
        this.tablaAfectada = tablaAfectada;
        this.campoAfectado = campoAfectado;
        this.valorAnterior = valorAnterior;
        this.valorNuevo = valorNuevo;
    }

    // Getters y Setters
    public int getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(int idHistorial) {
        this.idHistorial = idHistorial;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getDescripcionCambio() {
        return descripcionCambio;
    }

    public void setDescripcionCambio(String descripcionCambio) {
        if (descripcionCambio == null || descripcionCambio.isEmpty()) {
            throw new IllegalArgumentException("La descripción del cambio no puede estar vacía.");
        }
        this.descripcionCambio = descripcionCambio;
    }

    public Timestamp getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(Timestamp fechaCambio) {
        if (fechaCambio == null) {
            throw new IllegalArgumentException("La fecha de cambio no puede ser nula.");
        }
        this.fechaCambio = fechaCambio;
    }

    public String getTablaAfectada() {
        return tablaAfectada;
    }

    public void setTablaAfectada(String tablaAfectada) {
        this.tablaAfectada = tablaAfectada;
    }

    public String getCampoAfectado() {
        return campoAfectado;
    }

    public void setCampoAfectado(String campoAfectado) {
        this.campoAfectado = campoAfectado;
    }

    public String getValorAnterior() {
        return valorAnterior;
    }

    public void setValorAnterior(String valorAnterior) {
        this.valorAnterior = valorAnterior;
    }

    public String getValorNuevo() {
        return valorNuevo;
    }

    public void setValorNuevo(String valorNuevo) {
        this.valorNuevo = valorNuevo;
    }

    // Método para convertir DTO a entidad HistorialCambios
    public HistorialCambios toEntity() {
        HistorialCambios historial = new HistorialCambios();
        historial.setIdHistorial(this.idHistorial);
        historial.setIdUsuario(this.idUsuario);
        historial.setDescripcionCambio(this.descripcionCambio);
        historial.setFechaCambio(this.fechaCambio);
        historial.setTablaAfectada(this.tablaAfectada);
        historial.setCampoAfectado(this.campoAfectado);
        historial.setValorAnterior(this.valorAnterior);
        historial.setValorNuevo(this.valorNuevo);
        return historial;
    }

    // Método para convertir una entidad HistorialCambios a DTO
    public static HistorialCambiosDTO fromEntity(HistorialCambios historial) {
        return new HistorialCambiosDTO(
                historial.getIdHistorial(),
                historial.getIdUsuario(),
                historial.getDescripcionCambio(),
                historial.getFechaCambio(),
                historial.getTablaAfectada(),
                historial.getCampoAfectado(),
                historial.getValorAnterior(),
                historial.getValorNuevo()
        );
    }
}
