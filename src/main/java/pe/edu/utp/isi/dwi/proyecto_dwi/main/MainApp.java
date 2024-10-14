package pe.edu.utp.isi.dwi.proyecto_dwi.main;

import pe.edu.utp.isi.dwi.proyecto_dwi.dao.ConexionBD;

public class MainApp {
    public static void main(String[] args) {
        // Configurar el shutdown hook para cerrar las conexiones de la base de datos
        ConexionBD.setupShutdownHook();

        // Aquí puedes agregar el resto del código principal de tu aplicación
        System.out.println("Aplicación iniciada correctamente.");

        // Ejemplo: Puedes probar una operación DAO aquí si es necesario, por ejemplo:
        // DAOFactory daoFactory = DAOFactory.getInstance();
        // UsuarioDAO usuarioDAO = daoFactory.getUsuarioDAO(ConexionBD.getConnection());
        // usuarioDAO.listarUsuarios(); (manejar try-catch)
    }
}
