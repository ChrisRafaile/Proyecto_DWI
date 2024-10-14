package pe.edu.utp.isi.dwi.proyecto_dwi.dto;

import pe.edu.utp.isi.dwi.proyecto_dwi.entities.Rol;

public class RolDTO {
    private int idRol;
    private String nombreRol;
    private String descripcion; // Nueva propiedad para la descripción del rol

    // Getters y Setters
    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // Método para convertir un DTO a una entidad Rol
    public Rol toEntity() {
        Rol rol = new Rol();
        rol.setIdRol(this.idRol);
        rol.setNombreRol(this.nombreRol);
        rol.setDescripcion(this.descripcion);
        return rol;
    }

    // Método para convertir una entidad Rol a un DTO
    public static RolDTO fromEntity(Rol rol) {
        RolDTO rolDTO = new RolDTO();
        rolDTO.setIdRol(rol.getIdRol());
        rolDTO.setNombreRol(rol.getNombreRol());
        rolDTO.setDescripcion(rol.getDescripcion());
        return rolDTO;
    }
}
