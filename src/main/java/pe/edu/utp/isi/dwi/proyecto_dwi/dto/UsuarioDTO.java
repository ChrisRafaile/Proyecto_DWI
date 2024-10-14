package pe.edu.utp.isi.dwi.proyecto_dwi.dto;

import pe.edu.utp.isi.dwi.proyecto_dwi.entities.Usuario;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDTO {

    private int idUsuario;
    private String nombreUsuario;
    private String correo;
    private String fechaRegistro; // Mantener como String para la presentación
    private String contrasena;
    private String salt;
    private ColaboradorDTO colaborador; // Cambiar idColaborador a ColaboradorDTO

    // Nuevos campos agregados en la base de datos
    private String direccion;
    private String telefono;
    private String apellidos;
    private String fechaNacimiento; // String para facilitar la presentación
    private String fotoPerfil;
    private String fechaIngreso; // String para facilitar la presentación
    private String departamento;
    private String dni;

    // Método para verificar si el usuario tiene un cargo específico
    public boolean tienePermiso(String cargoRequerido) {
        return colaborador != null && colaborador.getCargo() != null && colaborador.getCargo().equalsIgnoreCase(cargoRequerido);
    }

    // Getters y Setters
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

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
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

    public ColaboradorDTO getColaborador() {
        return colaborador;
    }

    public void setColaborador(ColaboradorDTO colaborador) {
        this.colaborador = colaborador;
    }

    // Nuevos getters y setters para los campos agregados
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

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getFechaIngreso() {
        if (fechaIngreso == null || fechaIngreso.isEmpty()) {
            return LocalDate.now().toString(); // Devolver la fecha actual si está vacía
        }
        return fechaIngreso;
    }

    public void setFechaIngreso(String fechaIngreso) {
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

    // Método para convertir UsuarioDTO a Usuario (Entidad)
    public Usuario toEntity() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(this.idUsuario);
        usuario.setNombreUsuario(this.nombreUsuario);
        usuario.setCorreo(this.correo);
        usuario.setContrasena(this.contrasena);
        usuario.setSalt(this.salt);
        usuario.setDireccion(this.direccion);
        usuario.setTelefono(this.telefono);
        usuario.setApellidos(this.apellidos);
        usuario.setFotoPerfil(this.fotoPerfil);
        usuario.setDepartamento(this.departamento);
        usuario.setDni(this.dni);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (this.fechaNacimiento != null && !this.fechaNacimiento.isEmpty()) {
            usuario.setFechaNacimiento(LocalDate.parse(this.fechaNacimiento, formatter));
        }

        if (this.fechaIngreso != null && !this.fechaIngreso.isEmpty()) {
            usuario.setFechaIngreso(LocalDate.parse(this.fechaIngreso, formatter));
        }

        if (this.fechaRegistro != null && !this.fechaRegistro.isEmpty()) {
            usuario.setFechaRegistro(LocalDate.parse(this.fechaRegistro, formatter));
        }

        if (this.colaborador != null) {
            usuario.setColaborador(this.colaborador.toEntity());
        }

        return usuario;
    }

    // Método para convertir una entidad Usuario en DTO
    public static UsuarioDTO fromEntity(Usuario usuario) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setIdUsuario(usuario.getIdUsuario());
        usuarioDTO.setNombreUsuario(usuario.getNombreUsuario());
        usuarioDTO.setCorreo(usuario.getCorreo());
        usuarioDTO.setContrasena(usuario.getContrasena());
        usuarioDTO.setSalt(usuario.getSalt());
        usuarioDTO.setDireccion(usuario.getDireccion());
        usuarioDTO.setTelefono(usuario.getTelefono());
        usuarioDTO.setApellidos(usuario.getApellidos());
        usuarioDTO.setFotoPerfil(usuario.getFotoPerfil());
        usuarioDTO.setDepartamento(usuario.getDepartamento());
        usuarioDTO.setDni(usuario.getDni());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (usuario.getFechaNacimiento() != null) {
            usuarioDTO.setFechaNacimiento(usuario.getFechaNacimiento().format(formatter));
        }

        if (usuario.getFechaIngreso() != null) {
            usuarioDTO.setFechaIngreso(usuario.getFechaIngreso().format(formatter));
        }

        if (usuario.getFechaRegistro() != null) {
            usuarioDTO.setFechaRegistro(usuario.getFechaRegistro().format(formatter));
        }

        if (usuario.getColaborador() != null) {
            usuarioDTO.setColaborador(ColaboradorDTO.fromEntity(usuario.getColaborador()));
        }

        return usuarioDTO;
    }

    // Método para convertir una lista de entidades en una lista de DTOs
    public static List<UsuarioDTO> fromEntities(List<Usuario> usuarios) {
        List<UsuarioDTO> usuarioDTOList = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            usuarioDTOList.add(UsuarioDTO.fromEntity(usuario));
        }
        return usuarioDTOList;
    }
}
