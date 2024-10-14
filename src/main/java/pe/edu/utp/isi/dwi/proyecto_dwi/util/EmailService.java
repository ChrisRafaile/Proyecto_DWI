package pe.edu.utp.isi.dwi.proyecto_dwi.util;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmailService {

    private static final Logger logger = Logger.getLogger(EmailService.class.getName());
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String USERNAME = System.getenv("SMTP_USERNAME"); // Usar variable de entorno para más seguridad
    private static final String PASSWORD = System.getenv("SMTP_PASSWORD"); // Usar variable de entorno para más seguridad

    // Método para enviar correo
    public static void enviarCorreo(String correo, String asunto, String mensaje) {
        // No enviar correo a direcciones ficticias
        if (correo.endsWith("@email.com")) {
            logger.log(Level.WARNING, "No se envía correo a direcciones ficticias: {0}", correo);
            return;
        }

        // Configurar las propiedades del servidor SMTP
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // Usar STARTTLS para seguridad
        props.put("mail.smtp.connectiontimeout", "10000"); // Tiempo de conexión (10 segundos)
        props.put("mail.smtp.timeout", "10000"); // Tiempo de respuesta (10 segundos)

        try {
            // Crear una sesión de correo con autenticación
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(USERNAME, PASSWORD);
                }
            });

            // Crear el mensaje de correo
            Message email = new MimeMessage(session);
            email.setFrom(new InternetAddress(USERNAME));
            email.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correo));
            email.setSubject(asunto);
            email.setText(mensaje);

            // Enviar el mensaje
            Transport.send(email);

            logger.log(Level.INFO, "Correo enviado exitosamente a {0}", correo);
        } catch (MessagingException e) {
            logger.log(Level.SEVERE, "Error al enviar el correo a: " + correo, e);
        }
    }
}
