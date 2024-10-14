package pe.edu.utp.isi.dwi.proyecto_dwi.controller;

import pe.edu.utp.isi.dwi.proyecto_dwi.util.EmailService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import jakarta.mail.MessagingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import pe.edu.utp.isi.dwi.proyecto_dwi.service.RestablecerContrasenaFacade;

@WebServlet(name = "SolicitudRestablecerContraseñaServlet", urlPatterns = {"/SolicitudRestablecerContrasenaServlet"})
public class SolicitudRestablecerContrasenaServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(SolicitudRestablecerContrasenaServlet.class.getName());
    private static final String CORREO_INVALIDO_MSG = "El formato del correo es inválido.";
    private static final String ERROR_SQL_MSG = "Ocurrió un error al procesar la solicitud. Inténtalo nuevamente.";
    private static final String ERROR_ENVIO_CORREO_MSG = "Hubo un error al enviar el correo. Inténtalo nuevamente.";
    private static final String RESTABLECIMIENTO_EXITOSO_MSG = "Si el correo existe, recibirás un enlace para restablecer tu contraseña.";

    private final RestablecerContrasenaFacade restablecerContrasenaFacade = new RestablecerContrasenaFacade();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String correo = request.getParameter("correo").toLowerCase().trim();

        try {
            if (!esCorreoValido(correo)) {
                mostrarMensajeError(request, response, CORREO_INVALIDO_MSG);
                return;
            }

            // Procesar solicitud de restablecimiento a través del facade
            String token = restablecerContrasenaFacade.generarTokenRestablecimiento(correo);

            if (token != null) {
                enviarCorreoRestablecimiento(request, response, correo, token);
            } else {
                mostrarMensajeGenerico(request, response);
            }
        } catch (MessagingException e) {
            logger.log(Level.SEVERE, "Error al enviar el correo de restablecimiento", e);
            mostrarMensajeError(request, response, ERROR_ENVIO_CORREO_MSG);
        } catch (ServletException | IOException e) {
            // Captura cualquier excepción que no haya sido prevista específicamente
            logger.log(Level.SEVERE, "Error inesperado al procesar la solicitud de restablecimiento", e);
            mostrarMensajeError(request, response, ERROR_SQL_MSG);
        }
    }

    // Método para validar el formato de un correo electrónico
    private boolean esCorreoValido(String correo) {
        return correo.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }

    // Método para enviar el correo de restablecimiento de contraseña
    private void enviarCorreoRestablecimiento(HttpServletRequest request, HttpServletResponse response, String correo, String token) throws MessagingException, ServletException, IOException {
        // Crear el link de restablecimiento para enviar al correo del usuario
        String linkRestablecimiento = "https://localhost:8084/Proyecto_DWI/restablecer_contrasena.jsp?token=" + token;
        String asunto = "Restablecimiento de contraseña";
        String mensaje = "Haz clic en el siguiente enlace para restablecer tu contraseña: " + linkRestablecimiento;

        EmailService.enviarCorreo(correo, asunto, mensaje);
        logger.log(Level.INFO, "Correo de restablecimiento enviado a {0}", correo);
        mostrarMensajeGenerico(request, response);
    }

    // Método para mostrar un mensaje de error específico
    private void mostrarMensajeError(HttpServletRequest request, HttpServletResponse response, String mensaje) throws ServletException, IOException {
        request.setAttribute("mensaje", mensaje);
        request.getRequestDispatcher("solicitud_restablecimiento.jsp").forward(request, response);
    }

    // Método para mostrar un mensaje genérico tras una solicitud de restablecimiento
    private void mostrarMensajeGenerico(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("mensaje", RESTABLECIMIENTO_EXITOSO_MSG);
        request.getRequestDispatcher("solicitud_restablecimiento.jsp").forward(request, response);
    }
}
