package pe.edu.utp.isi.dwi.proyecto_dwi.dto;

import java.time.LocalDate;
import pe.edu.utp.isi.dwi.proyecto_dwi.entities.Colaborador;

public class ColaboradorDTO {
    private int idColaborador;
    private int idUsuario;
    private String cargo;
    private LocalDate fechaIngreso;

    public ColaboradorDTO() {
    }

    public ColaboradorDTO(int idColaborador, int idUsuario, String cargo, LocalDate fechaIngreso) {
        this.idColaborador = idColaborador;
        this.idUsuario = idUsuario;
        this.cargo = cargo;
        this.fechaIngreso = fechaIngreso;
    }

    public int getIdColaborador() {
        return idColaborador;
    }

    public void setIdColaborador(int idColaborador) {
        this.idColaborador = idColaborador;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Colaborador toEntity() {
        Colaborador colaborador = new Colaborador();
        colaborador.setIdColaborador(this.idColaborador);
        colaborador.setIdUsuario(this.idUsuario);
        colaborador.setCargo(this.cargo);
        colaborador.setFechaIngreso(this.fechaIngreso);
        return colaborador;
    }

    public static ColaboradorDTO fromEntity(Colaborador colaborador) {
        return new ColaboradorDTO(
            colaborador.getIdColaborador(),
            colaborador.getIdUsuario(),
            colaborador.getCargo(),
            colaborador.getFechaIngreso()
        );
    }

    @Override
    public String toString() {
        return "ColaboradorDTO{" +
                "idColaborador=" + idColaborador +
                ", idUsuario=" + idUsuario +
                ", cargo='" + cargo + '\'' +
                ", fechaIngreso=" + fechaIngreso +
                '}';
    }
}
