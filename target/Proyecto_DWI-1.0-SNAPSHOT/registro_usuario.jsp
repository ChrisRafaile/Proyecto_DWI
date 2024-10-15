<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Registro de Usuario</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css">
        <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
        <!-- Asegúrate de incluir el script de reCAPTCHA -->
        <script src="https://www.google.com/recaptcha/api.js" async defer></script>

        <!-- Validación de formulario en JavaScript -->
        <script>
            function validarFormulario() {
                var contraseña = document.getElementById("contraseña").value;
                var confirmacion = document.getElementById("confirmacion").value;
                var correo = document.getElementById("correo").value;
                var cargo = document.getElementById("cargo").value;

                var regexCorreo = /^[\w\.-]+@[\w\.-]+\.[a-zA-Z]{2,}$/;
                var regexContraseña = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,}$/;

                // Validación de la contraseña
                if (!regexContraseña.test(contraseña)) {
                    mostrarError("La contraseña debe tener al menos 8 caracteres, incluyendo mayúsculas, minúsculas, números y caracteres especiales.", "contraseña");
                    return false;
                } else {
                    limpiarError("contraseña");
                }

                // Confirmación de la contraseña
                if (contraseña !== confirmacion) {
                    mostrarError("Las contraseñas no coinciden.", "confirmacion");
                    return false;
                } else {
                    limpiarError("confirmacion");
                }

                // Validación de correo
                if (!regexCorreo.test(correo)) {
                    mostrarError("Por favor, ingresa un correo válido.", "correo");
                    return false;
                } else {
                    limpiarError("correo");
                }

                // Validación del cargo
                if (cargo === "") {
                    alert("Debe seleccionar un cargo.");
                    return false;
                }

                return true;
            }

            // Mostrar mensajes de error al lado de los campos
            function mostrarError(mensaje, idCampo) {
                var errorSpan = document.getElementById("error-" + idCampo);
                if (errorSpan) {
                    errorSpan.innerText = mensaje;
                    errorSpan.style.display = "block";
                }
            }

            function limpiarError(idCampo) {
                var errorSpan = document.getElementById("error-" + idCampo);
                if (errorSpan) {
                    errorSpan.innerText = "";
                    errorSpan.style.display = "none";
                }
            }

<!-- JavaScript para mostrar/ocultar contraseñas -->

            function togglePasswordVisibility() {
                var passwordField = document.getElementById("contrasena");
                var passwordToggle = document.getElementById("passwordToggle");

                if (passwordField.type === "password") {
                    passwordField.type = "text";
                    passwordToggle.classList.add("fa-eye-slash");
                    passwordToggle.classList.remove("fa-eye");
                } else {
                    passwordField.type = "password";
                    passwordToggle.classList.add("fa-eye");
                    passwordToggle.classList.remove("fa-eye-slash");
                }
            }

            function togglePasswordVisibilityConfirm() {
                var confirmPasswordField = document.getElementById("confirmacion");
                var confirmPasswordToggle = document.getElementById("confirmPasswordToggle");

                if (confirmPasswordField.type === "password") {
                    confirmPasswordField.type = "text";
                    confirmPasswordToggle.classList.add("fa-eye-slash");
                    confirmPasswordToggle.classList.remove("fa-eye");
                } else {
                    confirmPasswordField.type = "password";
                    confirmPasswordToggle.classList.add("fa-eye");
                    confirmPasswordToggle.classList.remove("fa-eye-slash");
                }
            }
</script>
    </head>
    <body class="bg-gray-100">
        <header class="bg-blue-600 p-4 text-white flex justify-between items-center">
            <div class="flex items-center">
                <img src="images/logo_empresa.png" alt="Logo Empresa" class="w-12 h-12 mr-4">
                <h1 class="text-2xl font-bold">123digit@l</h1>
            </div>
            <nav>
                <ul class="flex space-x-4">
                    <li><a href="index.jsp" class="hover:text-gray-300">Inicio</a></li>
                    <li><a href="login.jsp" class="hover:text-gray-300">Iniciar Sesión</a></li>
                    <li><a href="registro_usuario.jsp" class="hover:text-gray-300">Registro de Usuario</a></li>
                </ul>
            </nav>
        </header>

        <div class="max-w-lg mx-auto mt-10 bg-white p-8 shadow-lg rounded-lg">
            <h1 class="text-2xl font-bold mb-6 text-center">Registrar Usuario</h1>

            <!-- Mostrar mensaje de error si existe -->
            <% if (request.getAttribute("mensajeError") != null) {%>
        <p class="text-red-600 text-center mb-4"><%= request.getAttribute("mensajeError")%></p>
        <% }%>

        <form action="${pageContext.request.contextPath}/Usuario?accion=guardar" method="POST" enctype="multipart/form-data" onsubmit="return validarFormulario();">
            <!-- Input oculto para enviar la acción -->
            <input type="hidden" name="accion" value="guardar">
            <!-- Nombre de Usuario -->
            <div class="mb-4">
                <label for="nombre_usuario" class="block text-sm font-medium text-gray-700">Nombre:</label>
                <input type="text" id="nombre_usuario" name="nombre_usuario" required class="mt-1 block w-full border-gray-300 rounded-md shadow-sm" placeholder="Nombre completo">
                <span id="error-nombre_usuario" class="text-red-500 text-sm" style="display:none;"></span>
            </div>

            <!-- Apellidos -->
            <div class="mb-4">
                <label for="apellidos" class="block text-sm font-medium text-gray-700">Apellidos:</label>
                <input type="text" id="apellidos" name="apellidos" required class="mt-1 block w-full border-gray-300 rounded-md shadow-sm" placeholder="Apellidos completos">
                <span id="error-apellidos" class="text-red-500 text-sm" style="display:none;"></span>
            </div>

            <!-- Correo -->
            <div class="mb-4">
                <label for="correo" class="block text-sm font-medium text-gray-700">Correo:</label>
                <input type="email" id="correo" name="correo" required class="mt-1 block w-full border-gray-300 rounded-md shadow-sm" placeholder="example@correo.com">
                <span id="error-correo" class="text-red-500 text-sm" style="display:none;"></span>
            </div>

            <!-- Contraseña -->
            <div class="mb-4 relative">
                <label for="contrasena" class="block text-sm font-medium text-gray-700">Contraseña:</label>
                <input type="password" id="contrasena" name="contrasena" required class="mt-1 block w-full border-gray-300 rounded-md shadow-sm" placeholder="********">
                <i id="passwordToggle" class="fa fa-eye text-blue-500 cursor-pointer absolute right-4 top-9" onclick="togglePasswordVisibility()"></i>
                <span id="error-contraseña" class="text-red-500 text-sm" style="display:none;"></span>
            </div>

            <!-- Confirmación de Contraseña -->
            <div class="mb-4 relative">
                <label for="confirmacion" class="block text-sm font-medium text-gray-700">Confirmar Contraseña:</label>
                <input type="password" id="confirmacion" name="confirmacion" required class="mt-1 block w-full border-gray-300 rounded-md shadow-sm" placeholder="********">
                <i id="confirmPasswordToggle" class="fa fa-eye text-blue-500 cursor-pointer absolute right-4 top-9" onclick="togglePasswordVisibilityConfirm()"></i>
                <span id="error-confirmacion" class="text-red-500 text-sm" style="display:none;"></span>
            </div>

            <!-- Teléfono -->
            <div class="mb-4">
                <label for="telefono" class="block text-sm font-medium text-gray-700">Número de Teléfono:</label>
                <input type="text" id="telefono" name="telefono" required class="mt-1 block w-full border-gray-300 rounded-md shadow-sm" placeholder="Número de teléfono">
            </div>

            <!-- Dirección -->
            <div class="mb-4">
                <label for="direccion" class="block text-sm font-medium text-gray-700">Dirección:</label>
                <input type="text" id="direccion" name="direccion" required class="mt-1 block w-full border-gray-300 rounded-md shadow-sm" placeholder="Dirección completa">
            </div>

            <!-- Fecha de Nacimiento -->
            <div class="mb-4">
                <label for="fecha_nacimiento" class="block text-sm font-medium text-gray-700">Fecha de Nacimiento:</label>
                <input type="date" id="fecha_nacimiento" name="fecha_nacimiento" class="mt-1 block w-full border-gray-300 rounded-md shadow-sm">
            </div>

            <!-- Foto de Perfil -->
            <div class="mb-4">
                <label for="foto_perfil" class="block text-sm font-medium text-gray-700">Foto de Perfil:</label>
                <input type="file" id="foto_perfil" name="foto_perfil" accept="image/*" class="mt-1 block w-full border-gray-300 rounded-md shadow-sm">
            </div>

            <!-- Departamento/Área -->
            <div class="mb-4">
                <label for="departamento" class="block text-sm font-medium text-gray-700">Departamento/Área:</label>
                <input type="text" id="departamento" name="departamento" class="mt-1 block w-full border-gray-300 rounded-md shadow-sm" placeholder="Departamento o Área">
            </div>

            <!-- DNI -->
            <div class="mb-4">
                <label for="dni" class="block text-sm font-medium text-gray-700">DNI:</label>
                <input type="text" id="dni" name="dni" required class="mt-1 block w-full border-gray-300 rounded-md shadow-sm" placeholder="DNI o Identificación">
            </div>

            <!-- Cargo -->
            <div class="mb-4">
                <label for="cargo" class="block text-sm font-medium text-gray-700">Cargo:</label>
                <select id="cargo" name="cargo" required class="mt-1 block w-full border-gray-300 rounded-md shadow-sm">
                    <option value="">Seleccione un cargo</option>
                    <option value="Jefe de Área">Jefe de Área</option>
                    <option value="Analista Senior">Analista Senior</option>
                    <option value="Programador Junior">Programador Junior</option>
                    <option value="Coordinador">Coordinador</option> <!-- Agregar esta línea -->
                    <option value="Cargo por Defecto">Cargo por Defecto</option>
                </select>
            </div>


            <!-- reCAPTCHA -->
            <div class="mb-4">
                <div class="g-recaptcha" data-sitekey="6Ldr2lAqAAAAAEnSz-vlr8CohOrvr018DINCVONX"></div>
            </div>

            <!-- Botón de Registro -->
            <div class="mt-6">
                <button type="submit" class="w-full bg-blue-500 text-white py-2 px-4 rounded-md hover:bg-blue-600">Registrar</button>
            </div>
        </form>
    </div>
</body>
</html>