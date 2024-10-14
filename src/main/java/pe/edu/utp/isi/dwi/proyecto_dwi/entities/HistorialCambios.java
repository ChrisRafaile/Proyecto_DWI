package pe.edu.utp.isi.dwi.proyecto_dwi.entities;

import java.sql.Timestamp;

public class HistorialCambios {

    private int idHistorial;
    private int idUsuario;
    private String descripcionCambio;
    private Timestamp fechaCambio;
    private String tablaAfectada; // Nueva columna para especificar la tabla afectada
    private String campoAfectado; // Nueva columna para el campo afectado
    private String valorAnterior; // Valor anterior del campo afectado
    private String valorNuevo;    // Valor nuevo del campo afectado

    // Constructor vacío
    public HistorialCambios() {
    }

    // Constructor con todos los campos
    public HistorialCambios(int idHistorial, int idUsuario, String descripcionCambio, Timestamp fechaCambio,
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

    @Override
    public String toString() {
        return "HistorialCambios{" +
                "idHistorial=" + idHistorial +
                ", idUsuario=" + idUsuario +
                ", descripcionCambio='" + descripcionCambio + '\'' +
                ", fechaCambio=" + fechaCambio +
                ", tablaAfectada='" + tablaAfectada + '\'' +
                ", campoAfectado='" + campoAfectado + '\'' +
                ", valorAnterior='" + valorAnterior + '\'' +
                ", valorNuevo='" + valorNuevo + '\'' +
                '}';
    }
}
