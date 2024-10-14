package pe.edu.utp.isi.dwi.proyecto_dwi.entities;

import pe.edu.utp.isi.dwi.proyecto_dwi.model.BaseEntity;

import java.time.LocalDate;

public class Usuario extends BaseEntity {

    private int idUsuario;
    private String nombreUsuario;
    private String correo;
    private String contrasena; // Cambiado a "contrasena" para mantener consistencia con la base de datos
    private String salt;
    private LocalDate fechaRegistro;
    private String direccion;
    private String telefono;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String fotoPerfil;
    private LocalDate fechaIngreso;
    private String departamento;
    private String dni;
    private Colaborador colaborador;

    // Constructor vacío
    public Usuario() {
    }

    // Constructor con todos los campos
    public Usuario(int idUsuario, String nombreUsuario, String correo, String contrasena, String salt, LocalDate fechaRegistro,
                   String direccion, String telefono, String apellidos, LocalDate fechaNacimiento, String fotoPerfil,
                   LocalDate fechaIngreso, String departamento, String dni, Colaborador colaborador) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.correo = correo;
        this.contrasena = contrasena; // Asignación correcta
        this.salt = salt;
        this.fechaRegistro = fechaRegistro;
        this.direccion = direccion;
        this.telefono = telefono;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.fotoPerfil = fotoPerfil;
        this.fechaIngreso = fechaIngreso;
        this.departamento = departamento;
        this.dni = dni;
        this.colaborador = colaborador;
    }

    // Getters y Setters con validación y auditoría
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public Colaborador getColaborador() {
        return colaborador;
    }

    public void setColaborador(Colaborador colaborador) {
        this.colaborador = colaborador;
    }

    // Método toString para depuración
    @Override
    public String toString() {
        return "Usuario{"
                + "idUsuario=" + idUsuario
                + ", nombreUsuario='" + nombreUsuario + '\''
                + ", correo='" + correo + '\''
                + ", fechaRegistro=" + fechaRegistro
                + ", direccion='" + direccion + '\''
                + ", telefono='" + telefono + '\''
                + ", apellidos='" + apellidos + '\''
                + ", fechaNacimiento=" + fechaNacimiento
                + ", fotoPerfil='" + fotoPerfil + '\''
                + ", fechaIngreso=" + fechaIngreso
                + ", departamento='" + departamento + '\''
                + ", dni='" + dni + '\''
                + ", colaborador=" + (colaborador != null ? colaborador.getIdColaborador() : "Sin colaborador")
                + '}';
    }
}
