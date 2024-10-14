package pe.edu.utp.isi.dwi.proyecto_dwi.entities;

import pe.edu.utp.isi.dwi.proyecto_dwi.model.AuditHelper;
import pe.edu.utp.isi.dwi.proyecto_dwi.model.BaseEntity;

import java.time.LocalDate;

public class Colaborador extends BaseEntity {

    private int idColaborador;  // ID del colaborador (clave primaria)
    private int idUsuario;  // ID del usuario asociado con el colaborador
    private String cargo;  // Cargo del colaborador
    private LocalDate fechaIngreso;  // Fecha de ingreso del colaborador

    // Constructor vacío
    public Colaborador() {
    }

    // Constructor con todos los campos
    public Colaborador(int idColaborador, int idUsuario, String cargo, LocalDate fechaIngreso) {
        this.idColaborador = idColaborador;
        this.idUsuario = idUsuario;
        this.cargo = cargo;
        this.fechaIngreso = fechaIngreso;
    }

    // Getters y Setters con validación y auditoría
    public int getIdColaborador() {
        return idColaborador;
    }

    public void setIdColaborador(int idColaborador) {
        int valorAnterior = this.idColaborador;
        this.idColaborador = idColaborador;
        AuditHelper.registrarCambio(
            idUsuario,
            "colaboradores",
            "idColaborador",
            String.valueOf(valorAnterior),
            String.valueOf(idColaborador),
            "ID del colaborador actualizado"
        );
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        int valorAnterior = this.idUsuario;
        this.idUsuario = idUsuario;
        AuditHelper.registrarCambio(
            idUsuario,
            "colaboradores",
            "idUsuario",
            String.valueOf(valorAnterior),
            String.valueOf(idUsuario),
            "ID del usuario asociado actualizado"
        );
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        String valorAnterior = this.cargo;
        this.cargo = validarCargo(cargo);
        AuditHelper.registrarCambio(
            idUsuario,
            "colaboradores",
            "cargo",
            valorAnterior,
            cargo,
            "Cargo actualizado"
        );
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        LocalDate valorAnterior = this.fechaIngreso;
        this.fechaIngreso = validarFechaIngreso(fechaIngreso);
        AuditHelper.registrarCambio(
            idUsuario,
            "colaboradores",
            "fechaIngreso",
            valorAnterior != null ? valorAnterior.toString() : null,
            fechaIngreso.toString(),
            "Fecha de ingreso actualizada"
        );
    }

    // Métodos privados para validaciones
    private String validarCargo(String cargo) {
        if (cargo == null || cargo.isEmpty()) {
            throw new IllegalArgumentException("El cargo no puede estar vacío.");
        }
        // Validar si el cargo es válido
        if (!cargo.equals("Jefe de Área") && !cargo.equals("Analista Senior") &&
                !cargo.equals("Programador Junior") && !cargo.equals("Coordinador")) {
            throw new IllegalArgumentException("Cargo no válido: " + cargo);
        }
        return cargo;
    }

    private LocalDate validarFechaIngreso(LocalDate fechaIngreso) {
        if (fechaIngreso.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de ingreso no puede ser una fecha futura.");
        }
        return fechaIngreso;
    }
}
