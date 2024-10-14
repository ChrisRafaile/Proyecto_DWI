<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Restablecer Contraseña</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <style>
        .show-password {
            cursor: pointer;
        }
    </style>
</head>
<body class="bg-gray-100">
    <header class="bg-blue-600 p-4 text-white">
        <h1 class="text-2xl font-bold">Restablecer Contraseña</h1>
    </header>
    
    <div class="max-w-md mx-auto mt-10 bg-white p-8 shadow-lg rounded-lg">
        <h1 class="text-2xl font-bold mb-6 text-center">Ingresa tu nueva contraseña</h1>

        <!-- Mostrar mensaje si existe -->
        <% if (request.getAttribute("mensaje") != null) { %>
            <p class="text-red-600 text-center mb-4"><%= request.getAttribute("mensaje") %></p>
        <% } %>

        <!-- Formulario para restablecer contraseña -->
        <form action="RestablecerContrasenaServlet" method="POST" onsubmit="return validarContraseñas();">
            <input type="hidden" name="token" value="<%= request.getParameter("token") %>">
            
            <!-- Nueva Contraseña -->
            <div class="mb-4">
                <label for="nuevaContraseña" class="block text-sm font-medium text-gray-700">Nueva Contraseña:</label>
                <div class="relative">
                    <input type="password" id="nuevaContraseña" name="nuevaContraseña" required 
                           class="mt-1 block w-full border-gray-300 rounded-md shadow-sm" 
                           placeholder="********" aria-label="Nueva Contraseña" aria-required="true" minlength="8"
                           pattern="(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,}">
                    <i class="show-password absolute right-3 top-3 fa fa-eye" onclick="togglePasswordVisibility('nuevaContraseña')"></i>
                </div>
                <small class="text-gray-500">La contraseña debe tener al menos 8 caracteres, incluyendo una letra mayúscula, un número y un carácter especial.</small>
            </div>

            <!-- Confirmar Contraseña -->
            <div class="mb-4">
                <label for="confirmarContraseña" class="block text-sm font-medium text-gray-700">Confirmar Contraseña:</label>
                <div class="relative">
                    <input type="password" id="confirmarContraseña" name="confirmarContraseña" required 
                           class="mt-1 block w-full border-gray-300 rounded-md shadow-sm" 
                           placeholder="********" aria-label="Confirmar Contraseña" aria-required="true" minlength="8">
                    <i class="show-password absolute right-3 top-3 fa fa-eye" onclick="togglePasswordVisibility('confirmarContraseña')"></i>
                </div>
            </div>
            
            <!-- Botón de Restablecer Contraseña -->
            <div class="mt-6">
                <button type="submit" class="w-full bg-blue-500 text-white py-2 px-4 rounded-md hover:bg-blue-600">Restablecer Contraseña</button>
            </div>
        </form>
    </div>

    <!-- Script para mostrar/ocultar contraseñas -->
    <script>
        function togglePasswordVisibility(id) {
            const input = document.getElementById(id);
            const icon = input.parentNode.querySelector('.show-password');

            if (input.type === "password") {
                input.type = "text";
                icon.classList.remove('fa-eye');
                icon.classList.add('fa-eye-slash');
            } else {
                input.type = "password";
                icon.classList.remove('fa-eye-slash');
                icon.classList.add('fa-eye');
            }
        }

        function validarContraseñas() {
            const nuevaContraseña = document.getElementById('nuevaContraseña').value;
            const confirmarContraseña = document.getElementById('confirmarContraseña').value;

            if (nuevaContraseña !== confirmarContraseña) {
                alert('Las contraseñas no coinciden. Por favor, verifica.');
                return false;
            }

            return true;
        }
    </script>
</body>
</html>
