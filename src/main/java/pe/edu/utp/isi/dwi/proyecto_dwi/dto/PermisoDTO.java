package pe.edu.utp.isi.dwi.proyecto_dwi.dto;

import pe.edu.utp.isi.dwi.proyecto_dwi.entities.Permiso;

public class PermisoDTO {
    private int idPermiso;
    private int idRol;
    private String permiso;

    // Constructor vacío
    public PermisoDTO() {}

    // Constructor con parámetros
    public PermisoDTO(int idPermiso, int idRol, String permiso) {
        this.idPermiso = idPermiso;
        this.idRol = idRol;
        this.permiso = permiso;
    }

    // Getters y Setters
    public int getIdPermiso() {
        return idPermiso;
    }

    public void setIdPermiso(int idPermiso) {
        this.idPermiso = idPermiso;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public String getPermiso() {
        return permiso;
    }

    public void setPermiso(String permiso) {
        this.permiso = permiso;
    }

    // Método para convertir DTO a entidad
    public Permiso toEntity() {
        return new Permiso(idPermiso, idRol, permiso);
    }

    // Método para convertir una entidad a DTO
    public static PermisoDTO fromEntity(Permiso permiso) {
        return new PermisoDTO(permiso.getIdPermiso(), permiso.getIdRol(), permiso.getPermiso());
    }

    @Override
    public String toString() {
        return "PermisoDTO{" +
                "idPermiso=" + idPermiso +
                ", idRol=" + idRol +
                ", permiso='" + permiso + '\'' +
                '}';
    }
}
