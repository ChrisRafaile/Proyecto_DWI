package pe.edu.utp.isi.dwi.proyecto_dwi.entities;

import pe.edu.utp.isi.dwi.proyecto_dwi.model.AuditHelper;

public class Permiso {
    private int idPermiso;
    private int idRol;
    private String permiso;

    // Constructor vacío
    public Permiso() {}

    // Constructor con parámetros
    public Permiso(int idPermiso, int idRol, String permiso) {
        this.idPermiso = idPermiso;
        this.idRol = idRol;
        this.permiso = validarPermiso(permiso);
    }

    // Getters y Setters con validación y auditoría
    public int getIdPermiso() {
        return idPermiso;
    }

    public void setIdPermiso(int idPermiso) {
        int valorAnterior = this.idPermiso;
        this.idPermiso = idPermiso;
        AuditHelper.registrarCambio(valorAnterior, "permiso", "idPermiso", String.valueOf(valorAnterior), String.valueOf(idPermiso), "ID del permiso actualizado.");
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        int valorAnterior = this.idRol;
        this.idRol = idRol;
        AuditHelper.registrarCambio(idPermiso, "permiso", "idRol", String.valueOf(valorAnterior), String.valueOf(idRol), "Rol del permiso actualizado.");
    }

    public String getPermiso() {
        return permiso;
    }

    public void setPermiso(String permiso) {
        String valorAnterior = this.permiso;
        this.permiso = validarPermiso(permiso);
        AuditHelper.registrarCambio(idPermiso, "permiso", "permiso", valorAnterior, permiso, "Nombre del permiso actualizado.");
    }

    // Métodos privados para validaciones
    private String validarPermiso(String permiso) {
        if (permiso == null || permiso.isEmpty()) {
            throw new IllegalArgumentException("El permiso no puede estar vacío.");
        }
        return permiso;
    }
}
