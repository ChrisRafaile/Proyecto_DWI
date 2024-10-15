<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Solicitud de Restablecimiento de Contraseña</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css">
    <script>
        function validarFormulario() {
            const correo = document.getElementById("correo").value.trim();
            const regexCorreo = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
            const mensajeError = document.getElementById("mensaje-error");

            if (!regexCorreo.test(correo)) {
                mensajeError.textContent = "Por favor, introduce un correo electrónico válido.";
                mensajeError.style.display = "block";
                return false;
            }

            mensajeError.textContent = "";
            mensajeError.style.display = "none";
            return true;
        }
    </script>
</head>
<body class="bg-gray-100">
    <% 
        // Validación de sesión
        if (session.getAttribute("usuario") == null) {
            response.sendRedirect("login.jsp?error=sin-sesion");
            return; // Evitar seguir procesando la página
        }
    %>

    <header class="bg-blue-600 p-4 text-white">
        <h1 class="text-2xl font-bold">Restablecer Contraseña</h1>
    </header>
    
    <div class="max-w-md mx-auto mt-10 bg-white p-8 shadow-lg rounded-lg">
        <h1 class="text-2xl font-bold mb-4 text-center">Solicitud de Restablecimiento de Contraseña</h1>
        <p class="text-center text-gray-700 mb-6">Introduce tu correo electrónico para recibir un enlace de restablecimiento de contraseña.</p>
        
        <!-- Mostrar mensaje si existe -->
        <% if (request.getAttribute("mensaje") != null) { %>
            <p class="text-green-600 text-center mb-4"><%= request.getAttribute("mensaje") %></p>
        <% } %>

        <!-- Formulario para solicitar restablecimiento -->
        <form action="SolicitudRestablecerContrasenaServlet" method="POST" onsubmit="return validarFormulario();">
            <div class="mb-4">
                <label for="correo" class="block text-sm font-medium text-gray-700">Correo:</label>
                <input type="email" id="correo" name="correo" required
                       class="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:border-blue-500 focus:ring focus:ring-blue-500 focus:ring-opacity-50"
                       placeholder="example@correo.com" aria-label="Correo electrónico para restablecer contraseña" aria-describedby="mensaje-error">
                <!-- Mensaje de error para correo -->
                <p id="mensaje-error" class="text-red-600 mt-2" style="display:none;"></p>
            </div>
            
            <div class="mt-6">
                <button type="submit" class="w-full bg-blue-500 text-white py-2 px-4 rounded-md hover:bg-blue-600 transition duration-300">Enviar solicitud</button>
            </div>
        </form>
    </div>
</body>   
</html>
