package pe.edu.utp.isi.dwi.proyecto_dwi.dto;

import java.util.Map;
import pe.edu.utp.isi.dwi.proyecto_dwi.entities.DashboardData;

public class DashboardDataDTO {

    private int totalUsuarios;
    private int totalActividades;
    private int totalSolicitudes;
    private int solicitudesPendientes;
    private int solicitudesCompletadas;
    private int totalAsignaciones;
    private double tiempoPromedioRespuesta;
    private Map<String, Integer> actividadesPorColaborador;

    // Constructor vacío
    public DashboardDataDTO() {
    }

    // Constructor con todos los campos
    public DashboardDataDTO(int totalUsuarios, int totalActividades, int totalSolicitudes, int solicitudesPendientes,
            int solicitudesCompletadas, int totalAsignaciones, double tiempoPromedioRespuesta,
            Map<String, Integer> actividadesPorColaborador) {
        this.totalUsuarios = totalUsuarios;
        this.totalActividades = totalActividades;
        this.totalSolicitudes = totalSolicitudes;
        this.solicitudesPendientes = solicitudesPendientes;
        this.solicitudesCompletadas = solicitudesCompletadas;
        this.totalAsignaciones = totalAsignaciones;
        this.tiempoPromedioRespuesta = tiempoPromedioRespuesta;
        this.actividadesPorColaborador = actividadesPorColaborador;
    }

    // Getters y Setters
    public int getTotalUsuarios() {
        return totalUsuarios;
    }

    public void setTotalUsuarios(int totalUsuarios) {
        this.totalUsuarios = totalUsuarios;
    }

    public int getTotalActividades() {
        return totalActividades;
    }

    public void setTotalActividades(int totalActividades) {
        this.totalActividades = totalActividades;
    }

    public int getTotalSolicitudes() {
        return totalSolicitudes;
    }

    public void setTotalSolicitudes(int totalSolicitudes) {
        this.totalSolicitudes = totalSolicitudes;
    }

    public int getSolicitudesPendientes() {
        return solicitudesPendientes;
    }

    public void setSolicitudesPendientes(int solicitudesPendientes) {
        this.solicitudesPendientes = solicitudesPendientes;
    }

    public int getSolicitudesCompletadas() {
        return solicitudesCompletadas;
    }

    public void setSolicitudesCompletadas(int solicitudesCompletadas) {
        this.solicitudesCompletadas = solicitudesCompletadas;
    }

    public int getTotalAsignaciones() {
        return totalAsignaciones;
    }

    public void setTotalAsignaciones(int totalAsignaciones) {
        this.totalAsignaciones = totalAsignaciones;
    }

    public double getTiempoPromedioRespuesta() {
        return tiempoPromedioRespuesta;
    }

    public void setTiempoPromedioRespuesta(double tiempoPromedioRespuesta) {
        this.tiempoPromedioRespuesta = tiempoPromedioRespuesta;
    }

    public Map<String, Integer> getActividadesPorColaborador() {
        return actividadesPorColaborador;
    }

    public void setActividadesPorColaborador(Map<String, Integer> actividadesPorColaborador) {
        this.actividadesPorColaborador = actividadesPorColaborador;
    }

    // Método para convertir DTO a entidad DashboardData
    public DashboardData toEntity() {
        DashboardData dashboardData = new DashboardData();
        dashboardData.setTotalUsuarios(this.totalUsuarios);
        dashboardData.setTotalActividades(this.totalActividades);
        dashboardData.setTotalSolicitudes(this.totalSolicitudes);
        dashboardData.setSolicitudesPendientes(this.solicitudesPendientes);
        dashboardData.setSolicitudesCompletadas(this.solicitudesCompletadas);
        dashboardData.setTotalAsignaciones(this.totalAsignaciones);
        // Las métricas tiempoPromedioRespuesta y actividadesPorColaborador no se incluyen en la entidad
        return dashboardData;
    }

    // Método para convertir una entidad DashboardData en DTO
    public static DashboardDataDTO fromEntity(DashboardData dashboardData) {
        return new DashboardDataDTO(
                dashboardData.getTotalUsuarios(),
                dashboardData.getTotalActividades(),
                dashboardData.getTotalSolicitudes(),
                dashboardData.getSolicitudesPendientes(),
                dashboardData.getSolicitudesCompletadas(),
                dashboardData.getTotalAsignaciones(),
                0, // Inicializar el tiempoPromedioRespuesta si se requiere
                null // Inicializar actividadesPorColaborador si se requiere
        );
    }

    @Override
    public String toString() {
        return "DashboardDataDTO{"
                + "totalUsuarios=" + totalUsuarios
                + ", totalActividades=" + totalActividades
                + ", totalSolicitudes=" + totalSolicitudes
                + ", solicitudesPendientes=" + solicitudesPendientes
                + ", solicitudesCompletadas=" + solicitudesCompletadas
                + ", totalAsignaciones=" + totalAsignaciones
                + ", tiempoPromedioRespuesta=" + tiempoPromedioRespuesta
                + ", actividadesPorColaborador=" + actividadesPorColaborador
                + '}';
    }
}
