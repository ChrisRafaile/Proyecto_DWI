<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Iniciar Sesión</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
        <script src="https://www.google.com/recaptcha/api.js" async defer></script>

        <style>
            .toggle-password {
                cursor: pointer;
                position: absolute;
                right: 10px;
                top: 45px;
            }
        </style>
    </head>
    <body class="bg-gray-100">
        <!-- Header -->
        <header class="bg-blue-600 p-4 text-white text-center">
            <h1 class="text-2xl font-bold">Iniciar Sesión</h1>
        </header>

        <!-- Formulario de Inicio de Sesión -->
        <div class="max-w-md mx-auto mt-10 bg-white p-8 shadow-lg rounded-lg">
            <h1 class="text-2xl font-bold mb-6 text-center">Iniciar Sesión</h1>

            <!-- Mostrar mensaje de error si existe -->
            <% if (request.getAttribute("mensajeError") != null) {%>
            <p class="text-red-600 text-center mb-4"><%= request.getAttribute("mensajeError")%></p>
            <% }%>

            <!-- Formulario -->
            <form action="Login" method="POST" onsubmit="return validarFormulario()">
                <div class="mb-4 relative">
                    <label for="correo" class="block text-sm font-medium text-gray-700">Correo:</label>
                    <input type="email" id="correo" name="correo" required
                           class="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:border-blue-500 focus:ring focus:ring-blue-500 focus:ring-opacity-50"
                           placeholder="example@correo.com">
                </div>

                <div class="mb-4 relative">
                    <label for="contraseña" class="block text-sm font-medium text-gray-700">Contraseña:</label>
                    <input type="password" id="contraseña" name="contraseña" required
                           class="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:border-blue-500 focus:ring focus:ring-blue-500 focus:ring-opacity-50"
                           placeholder="********">
                    <i id="togglePassword" class="fas fa-eye toggle-password text-gray-500"></i>
                </div>

                <!-- reCAPTCHA -->
                <div class="g-recaptcha mb-4" data-sitekey="6Ldr2lAqAAAAAEnSz-vlr8CohOrvr018DINCVONX"></div>

                <div class="mt-6">
                    <button type="submit" class="w-full bg-blue-500 text-white py-2 px-4 rounded-md hover:bg-blue-600">
                        Iniciar Sesión
                    </button>
                </div>
            </form>

            <div class="mt-4 text-center">
                <p>¿No tienes una cuenta? <a href="registro_usuario.jsp" class="text-blue-500 hover:underline">Regístrate aquí</a></p>
            </div>
        </div>

        <!-- Validación sencilla del formulario -->
        <script>
            function validarFormulario() {
                const correo = document.getElementById('correo').value;
                const contraseña = document.getElementById('contraseña').value;
                if (correo.trim() === '' || contraseña.trim() === '') {
                    alert('Por favor, complete todos los campos.');
                    return false;
                }
                return true;
            }

            // Mostrar/Ocultar contraseña
            document.getElementById('togglePassword').addEventListener('click', function () {
                const passwordField = document.getElementById('contraseña');
                const type = passwordField.getAttribute('type') === 'password' ? 'text' : 'password';
                passwordField.setAttribute('type', type);
                this.classList.toggle('fa-eye-slash');
            });
        </script>
    </body>
</html>
