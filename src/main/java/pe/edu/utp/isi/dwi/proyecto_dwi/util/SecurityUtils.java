package pe.edu.utp.isi.dwi.proyecto_dwi.util;

import com.google.cloud.recaptchaenterprise.v1.RecaptchaEnterpriseServiceClient;
import com.google.recaptchaenterprise.v1.Assessment;
import com.google.recaptchaenterprise.v1.CreateAssessmentRequest;
import com.google.recaptchaenterprise.v1.Event;
import com.google.recaptchaenterprise.v1.ProjectName;
import com.google.recaptchaenterprise.v1.RiskAnalysis.ClassificationReason;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import pe.edu.utp.isi.dwi.proyecto_dwi.entities.Usuario;

public class SecurityUtils {

    // ID del proyecto de Google Cloud
    private static final String PROJECT_ID = "proyecto-soporte-1727406256080";
    // Clave del sitio reCAPTCHA asociada con tu aplicación
    private static final String RECAPTCHA_SITE_KEY = "6Ldr2lAqAAAAAEnSz-vlr8CohOrvr018DINCVONX";
    // Nombre de la acción que especificaste para el reCAPTCHA
    private static final String RECAPTCHA_ACTION = "registroUsuario";

    // Método para generar un salt aleatorio para el hash de la contraseña
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[32];
        random.nextBytes(salt);
        StringBuilder sb = new StringBuilder();
        for (byte b : salt) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // Método para hash de la contraseña con el salt
    public static String hashPasswordWithSalt(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String saltedPassword = password + salt;
            byte[] hash = md.digest(saltedPassword.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al generar el hash de la contraseña", e);
        }
    }

    // Método para verificar el token reCAPTCHA usando el cliente de reCAPTCHA Enterprise
    public static boolean verificarReCaptcha(String token) {
        try (RecaptchaEnterpriseServiceClient client = RecaptchaEnterpriseServiceClient.create()) {
            // Crear el evento que contiene el token y la clave del sitio
            Event event = Event.newBuilder()
                    .setSiteKey(RECAPTCHA_SITE_KEY)
                    .setToken(token)
                    .build();

            // Crear la solicitud de evaluación
            CreateAssessmentRequest createAssessmentRequest = CreateAssessmentRequest.newBuilder()
                    .setParent(ProjectName.of(PROJECT_ID).toString())
                    .setAssessment(Assessment.newBuilder().setEvent(event).build())
                    .build();

            // Evaluar el token usando el cliente
            Assessment response = client.createAssessment(createAssessmentRequest);

            // Verificar si el token es válido
            if (!response.getTokenProperties().getValid()) {
                System.out.println("El token de reCAPTCHA es inválido: " + response.getTokenProperties().getInvalidReason().name());
                return false;
            }

            // Verificar si se ejecutó la acción esperada
            if (!response.getTokenProperties().getAction().equals(RECAPTCHA_ACTION)) {
                System.out.println("La acción del reCAPTCHA no coincide con la acción esperada: " + response.getTokenProperties().getAction());
                return false;
            }

            // Obtener la puntuación del reCAPTCHA
            float recaptchaScore = response.getRiskAnalysis().getScore();
            System.out.println("Puntuación reCAPTCHA: " + recaptchaScore);

            // Usualmente se considera seguro si la puntuación es mayor o igual a 0.5
            return recaptchaScore >= 0.5;
        } catch (IOException e) {
            System.err.println("Error al validar reCAPTCHA: " + e.getMessage());
            return false;
        }
    }

    // Método para verificar permisos de usuario
    public static boolean tienePermiso(Usuario usuario, String permisoRequerido) {
        if (usuario == null || usuario.getColaborador() == null) {
            return false;
        }
        return usuario.getColaborador().getCargo().equalsIgnoreCase(permisoRequerido);
    }
}
