package pe.edu.utp.isi.dwi.proyecto_dwi.service;

import pe.edu.utp.isi.dwi.proyecto_dwi.dao.DashboardDAO;
import pe.edu.utp.isi.dwi.proyecto_dwi.dto.DashboardDataDTO;
import pe.edu.utp.isi.dwi.proyecto_dwi.factory.DAOFactory;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DashboardFacade {
    private static final Logger logger = Logger.getLogger(DashboardFacade.class.getName());
    private final DashboardDAO dashboardDAO;

    // Constructor que inicializa el DashboardDAO usando DAOFactory
    public DashboardFacade() {
        this.dashboardDAO = DAOFactory.getInstance().getDashboardDAO();
    }

    // Obtener estadísticas del dashboard
    public DashboardDataDTO obtenerEstadisticas() {
        try {
            return dashboardDAO.obtenerDatosDashboard();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener estadísticas", e);
            throw new RuntimeException("Error al obtener estadísticas", e);
        }
    }
}
