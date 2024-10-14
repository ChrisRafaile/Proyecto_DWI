package pe.edu.utp.isi.dwi.proyecto_dwi.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.IOException;

import pe.edu.utp.isi.dwi.proyecto_dwi.entities.Usuario;
import pe.edu.utp.isi.dwi.proyecto_dwi.util.SecurityUtils;

@WebServlet("/FrontController")
public class FrontControllerServlet extends HttpServlet {
    
    private static final Logger logger = Logger.getLogger(FrontControllerServlet.class.getName());

    // Definir las acciones como constantes para evitar errores tipográficos
    private static final String ACTION_LOGIN = "login";
    private static final String ACTION_REGISTRO_USUARIO = "registroUsuario";
    private static final String ACTION_GUARDAR_ACTIVIDAD = "guardarActividad";
    private static final String ACTION_GUARDAR_ASIGNACION = "guardarAsignacion";
    private static final String ACTION_GUARDAR_SOLICITUD = "guardarSolicitud";

    // Definir los roles como constantes
    private static final String ROLE_JEFE_DE_AREA = "Jefe de Área";
    private static final String ROLE_COORDINADOR = "Coordinador";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        procesarSolicitud(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        procesarSolicitud(request, response);
    }

    /**
     * Método principal para procesar la solicitud.
     *
     * @param request  La solicitud HTTP
     * @param response La respuesta HTTP
     * @throws ServletException
     * @throws IOException
     */
    private void procesarSolicitud(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener la acción solicitada por el usuario
        String action = request.getParameter("action");
        HttpSession session = request.getSession(false); // Obtener la sesión si existe, sin crear una nueva
        Usuario usuario = (Usuario) (session != null ? session.getAttribute("usuario") : null);

        // Verificar si la acción es nula o vacía
        if (action == null || action.isEmpty()) {
            redirigirConError(request, response, "Acción no definida.");
            return;
        }

        // Verificar si el usuario ha iniciado sesión
        if (usuario == null) {
            redirigirConError(request, response, "Debes iniciar sesión para acceder a esta sección.");
            return;
        }

        // Redirigir la solicitud según la acción recibida y el rol del usuario
        try {
            switch (action) {
                case ACTION_LOGIN -> request.getRequestDispatcher("/LoginServlet").forward(request, response);
                case ACTION_REGISTRO_USUARIO -> {
                    if (SecurityUtils.tienePermiso(usuario, ROLE_JEFE_DE_AREA)) {
                        request.getRequestDispatcher("/UsuarioServlet").forward(request, response);
                    } else {
                        redirigirConError(request, response, "No tienes permisos para registrar un usuario.");
                    }
                }
                case ACTION_GUARDAR_ACTIVIDAD -> {
                    if (SecurityUtils.tienePermiso(usuario, ROLE_JEFE_DE_AREA) || SecurityUtils.tienePermiso(usuario, ROLE_COORDINADOR)) {
                        request.getRequestDispatcher("/ActividadServlet").forward(request, response);
                    } else {
                        redirigirConError(request, response, "No tienes permisos para guardar una actividad.");
                    }
                }
                case ACTION_GUARDAR_ASIGNACION -> {
                    if (SecurityUtils.tienePermiso(usuario, ROLE_JEFE_DE_AREA)) {
                        request.getRequestDispatcher("/AsignacionServlet").forward(request, response);
                    } else {
                        redirigirConError(request, response, "No tienes permisos para guardar una asignación.");
                    }
                }
                case ACTION_GUARDAR_SOLICITUD -> request.getRequestDispatcher("/SolicitudServlet").forward(request, response);
                default -> redirigirConError(request, response, "Acción desconocida.");
            }
        } catch (ServletException | IOException e) {
            logger.log(Level.SEVERE, "Error al procesar la solicitud: {0}", e);
            redirigirConError(request, response, "Error al procesar la solicitud: " + e.getMessage());
        }
    }

    /**
     * Método para redirigir a la página de error con un mensaje específico.
     *
     * @param request      La solicitud HTTP
     * @param response     La respuesta HTTP
     * @param mensajeError El mensaje de error a mostrar
     * @throws ServletException
     * @throws IOException
     */
    private void redirigirConError(HttpServletRequest request, HttpServletResponse response, String mensajeError) throws ServletException, IOException {
        request.setAttribute("mensajeError", mensajeError);
        request.getRequestDispatcher("error.jsp").forward(request, response);
    }
}
