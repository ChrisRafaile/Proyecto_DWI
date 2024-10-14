package pe.edu.utp.isi.dwi.proyecto_dwi.factory;

import pe.edu.utp.isi.dwi.proyecto_dwi.dao.*;
import java.sql.Connection;
import java.sql.SQLException;

public class DAOFactory {

    // Instancia única de DAOFactory para aplicar el patrón Singleton
    private static volatile DAOFactory instance;

    // Conexión compartida
    private Connection connection;

    // Constructor privado para evitar instanciación externa
    private DAOFactory() {
        try {
            this.connection = ConexionBD.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear la conexión en DAOFactory", e);
        }
    }

    // Método para obtener la única instancia de DAOFactory (Singleton)
    public static DAOFactory getInstance() {
        DAOFactory localInstance = instance;
        if (localInstance == null) {
            synchronized (DAOFactory.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DAOFactory();
                }
            }
        }
        return localInstance;
    }

    // Método para obtener la conexión compartida
    public Connection getConnection() {
        return connection;
    }

    // Métodos para obtener instancias de los DAOs
    public RolesDAO getRolesDAO() {
        return new RolesDAO(connection);
    }

    public UsuariosRolesDAO getUsuariosRolesDAO() {
        return new UsuariosRolesDAO(connection);
    }

    public LogAuditoriaDAO getLogAuditoriaDAO() {
        return new LogAuditoriaDAO(connection);
    }

    public HistorialCambiosDAO getHistorialCambiosDAO() {
        return new HistorialCambiosDAO(connection);
    }

    public UsuarioDAO getUsuarioDAO() {
        return new UsuarioDAO(connection);
    }

    public SolicitudDAO getSolicitudDAO() {
        return new SolicitudDAO(connection);
    }

    public ActividadDAO getActividadDAO() {
        return new ActividadDAO(connection);
    }

    public AsignacionDAO getAsignacionDAO() {
        return new AsignacionDAO(connection);
    }

    public PermisosDAO getPermisosDAO() {
        return new PermisosDAO(connection);
    }

    public ColaboradorDAO getColaboradorDAO() {
        return new ColaboradorDAO(connection);
    }

    public DashboardDAO getDashboardDAO() {
        return new DashboardDAO(connection);
    }

    public NotificacionDAO getNotificacionDAO() {
        return new NotificacionDAO(connection);
    }
}
