package pe.edu.utp.isi.dwi.proyecto_dwi.entities;

import pe.edu.utp.isi.dwi.proyecto_dwi.model.AuditHelper;

public class DashboardData {
    private int totalUsuarios;
    private int totalActividades;
    private int totalSolicitudes; // Nuevo campo para el total de solicitudes
    private int solicitudesPendientes;
    private int solicitudesCompletadas;
    private int totalAsignaciones;

    // Getters y Setters con validaciones y auditoría
    public int getTotalUsuarios() {
        return totalUsuarios;
    }

    public void setTotalUsuarios(int totalUsuarios) {
        if (totalUsuarios < 0) {
            throw new IllegalArgumentException("El total de usuarios no puede ser negativo.");
        }
        int valorAnterior = this.totalUsuarios;
        this.totalUsuarios = totalUsuarios;
        AuditHelper.registrarCambio(
                0, 
                "dashboard", 
                "totalUsuarios", 
                String.valueOf(valorAnterior), 
                String.valueOf(totalUsuarios), 
                "Total de usuarios actualizado"
        );
    }

    public int getTotalActividades() {
        return totalActividades;
    }

    public void setTotalActividades(int totalActividades) {
        if (totalActividades < 0) {
            throw new IllegalArgumentException("El total de actividades no puede ser negativo.");
        }
        int valorAnterior = this.totalActividades;
        this.totalActividades = totalActividades;
        AuditHelper.registrarCambio(
                0, 
                "dashboard", 
                "totalActividades", 
                String.valueOf(valorAnterior), 
                String.valueOf(totalActividades), 
                "Total de actividades actualizado"
        );
    }

    public int getTotalSolicitudes() { // Nuevo getter
        return totalSolicitudes;
    }

    public void setTotalSolicitudes(int totalSolicitudes) { // Nuevo setter
        if (totalSolicitudes < 0) {
            throw new IllegalArgumentException("El total de solicitudes no puede ser negativo.");
        }
        int valorAnterior = this.totalSolicitudes;
        this.totalSolicitudes = totalSolicitudes;
        AuditHelper.registrarCambio(
                0, 
                "dashboard", 
                "totalSolicitudes", 
                String.valueOf(valorAnterior), 
                String.valueOf(totalSolicitudes), 
                "Total de solicitudes actualizado"
        );
    }

    public int getSolicitudesPendientes() {
        return solicitudesPendientes;
    }

    public void setSolicitudesPendientes(int solicitudesPendientes) {
        if (solicitudesPendientes < 0) {
            throw new IllegalArgumentException("El número de solicitudes pendientes no puede ser negativo.");
        }
        int valorAnterior = this.solicitudesPendientes;
        this.solicitudesPendientes = solicitudesPendientes;
        AuditHelper.registrarCambio(
                0, 
                "dashboard", 
                "solicitudesPendientes", 
                String.valueOf(valorAnterior), 
                String.valueOf(solicitudesPendientes), 
                "Solicitudes pendientes actualizadas"
        );
    }

    public int getSolicitudesCompletadas() {
        return solicitudesCompletadas;
    }

    public void setSolicitudesCompletadas(int solicitudesCompletadas) {
        if (solicitudesCompletadas < 0) {
            throw new IllegalArgumentException("El número de solicitudes completadas no puede ser negativo.");
        }
        int valorAnterior = this.solicitudesCompletadas;
        this.solicitudesCompletadas = solicitudesCompletadas;
        AuditHelper.registrarCambio(
                0, 
                "dashboard", 
                "solicitudesCompletadas", 
                String.valueOf(valorAnterior), 
                String.valueOf(solicitudesCompletadas), 
                "Solicitudes completadas actualizadas"
        );
    }

    public int getTotalAsignaciones() {
        return totalAsignaciones;
    }

    public void setTotalAsignaciones(int totalAsignaciones) {
        if (totalAsignaciones < 0) {
            throw new IllegalArgumentException("El total de asignaciones no puede ser negativo.");
        }
        int valorAnterior = this.totalAsignaciones;
        this.totalAsignaciones = totalAsignaciones;
        AuditHelper.registrarCambio(
                0, 
                "dashboard", 
                "totalAsignaciones", 
                String.valueOf(valorAnterior), 
                String.valueOf(totalAsignaciones), 
                "Total de asignaciones actualizado"
        );
    }
}
