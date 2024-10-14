package pe.edu.utp.isi.dwi.proyecto_dwi.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConexionBD {

    // Instancia única de HikariDataSource
    private static volatile HikariDataSource dataSource;
    private static final Logger logger = Logger.getLogger(ConexionBD.class.getName());

    // Constructor privado para evitar la creación de instancias de esta clase
    private ConexionBD() {
    }

    // Método para inicializar y obtener el DataSource usando el patrón Singleton
    private static HikariDataSource getDataSource() {
        HikariDataSource localDataSource = dataSource; // Variable local para mejorar el rendimiento
        if (localDataSource == null) {
            synchronized (ConexionBD.class) {
                localDataSource = dataSource;
                if (localDataSource == null) {
                    try {
                        // Cargar configuraciones desde el archivo de propiedades
                        Properties props = new Properties();
                        try (InputStream input = ConexionBD.class.getClassLoader().getResourceAsStream("pe/edu/utp/isi/dwi/proyecto_dwi/config/dbconfig.properties")) {
                            if (input == null) {
                                throw new RuntimeException("Archivo de configuración dbconfig.properties no encontrado");
                            }
                            props.load(input);
                        }

                        // Configuración de HikariCP
                        HikariConfig config = new HikariConfig();
                        config.setJdbcUrl(props.getProperty("jdbc.url"));
                        config.setUsername(props.getProperty("db.username"));
                        config.setPassword(props.getProperty("db.password"));
                        config.setDriverClassName("org.postgresql.Driver"); // <-- AÑADIR ESTA LÍNEA

                        config.setMaximumPoolSize(Integer.parseInt(props.getProperty("pool.maxPoolSize", "10")));
                        config.setMinimumIdle(Integer.parseInt(props.getProperty("pool.minIdle", "2")));
                        config.setConnectionTimeout(Long.parseLong(props.getProperty("pool.connectionTimeout", "30000")));
                        config.setIdleTimeout(Long.parseLong(props.getProperty("pool.idleTimeout", "600000")));
                        config.setMaxLifetime(Long.parseLong(props.getProperty("pool.maxLifetime", "1800000")));

                        localDataSource = new HikariDataSource(config);
                        dataSource = localDataSource; // Asignación a la variable estática
                        logger.log(Level.INFO, "HikariDataSource inicializado correctamente desde archivo de configuración");
                    } catch (IOException | RuntimeException e) {
                        logger.log(Level.SEVERE, "Error al inicializar HikariDataSource", e);
                        throw new RuntimeException("Error al inicializar el pool de conexiones", e);
                    }
                }
            }
        }
        return localDataSource;
    }

    // Método público para obtener una conexión a la base de datos desde el pool
    public static Connection getConnection() throws SQLException {
        return getDataSource().getConnection(); // Obtener una conexión del pool de Hikari
    }

    // Método para cerrar el DataSource cuando la aplicación se apague
    public static void closeDataSource() {
        if (dataSource != null) {
            dataSource.close();
            logger.log(Level.INFO, "HikariDataSource cerrado correctamente");
        }
    }

    // Método para registrar un hook de cierre
    public static void setupShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(ConexionBD::closeDataSource));
    }
}
