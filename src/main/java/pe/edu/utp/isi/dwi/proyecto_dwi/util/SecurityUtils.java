package pe.edu.utp.isi.dwi.proyecto_dwi.util;

import java.io.BufferedReader;
import java.io.IOException;
import org.json.JSONObject;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import org.json.JSONException;

import pe.edu.utp.isi.dwi.proyecto_dwi.entities.Usuario;

public class SecurityUtils {

    // Clave secreta para reCAPTCHA. Debes reemplazar esto con tu propia clave secreta.
    private static final String RECAPTCHA_SECRET_KEY = "6Ldr2lAqAAAAAP188mrYeHua3VZ6rms4JxII6Xpu";

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

    public static boolean tienePermiso(Usuario usuario, String permisoRequerido) {
        if (usuario == null || usuario.getColaborador() == null) {
            return false;
        }
        return usuario.getColaborador().getCargo().equalsIgnoreCase(permisoRequerido);
    }

    /**
     * Verificar la respuesta de reCAPTCHA usando la API de Google.
     *
     * @param recaptchaResponse La respuesta del reCAPTCHA enviada desde el
     * formulario.
     * @return true si el reCAPTCHA es válido, false en caso contrario.
     */
    public static boolean verificarReCaptcha(String recaptchaResponse) {
        try {
            // Definir la URL del servicio reCAPTCHA de Google
            String url = "https://www.google.com/recaptcha/api/siteverify";

            // Preparar la clave secreta y los parámetros para la solicitud POST
            String secret = RECAPTCHA_SECRET_KEY;  // Utiliza la clave secreta almacenada como constante
            String postParams = "secret=" + secret + "&response=" + recaptchaResponse;

            // Abrir la conexión HTTP
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);

            // Enviar los parámetros POST
            try (OutputStream os = con.getOutputStream()) {
                os.write(postParams.getBytes(StandardCharsets.UTF_8));
            }

            // Leer la respuesta de la API
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }

                    // Convertir la respuesta JSON en un objeto JSON
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    return jsonResponse.getBoolean("success");
                }
            }
        } catch (IOException | JSONException e) {
        }
        return false;
    }
}
/**/
