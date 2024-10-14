package pe.edu.utp.isi.dwi.proyecto_dwi.entities;

public class Rol {
    private int idRol;           // ID del rol
    private String nombreRol;    // Nombre del rol
    private String descripcion;  // Nueva propiedad para la descripción del rol

    // Constructor vacío
    public Rol() {}

    // Constructor con parámetros
    public Rol(int idRol, String nombreRol, String descripcion) {
        this.idRol = idRol;

        if (nombreRol == null || nombreRol.isEmpty()) {
            throw new IllegalArgumentException("El nombre del rol no puede estar vacío.");
        }
        this.nombreRol = nombreRol;

        if (descripcion == null || descripcion.isEmpty()) {
            throw new IllegalArgumentException("La descripción no puede estar vacía.");
        }
        this.descripcion = descripcion; // Asignación directa
    }

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
        if (nombreRol == null || nombreRol.isEmpty()) {
            throw new IllegalArgumentException("El nombre del rol no puede estar vacío.");
        }
        this.nombreRol = nombreRol;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        if (descripcion == null || descripcion.isEmpty()) {
            throw new IllegalArgumentException("La descripción no puede estar vacía.");
        }
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Rol{" +
                "idRol=" + idRol +
                ", nombreRol='" + nombreRol + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
