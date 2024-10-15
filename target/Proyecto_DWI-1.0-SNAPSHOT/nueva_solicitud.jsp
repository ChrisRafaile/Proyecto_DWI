<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nueva Solicitud</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css">
    <script>
        function validarFormulario() {
            const idCliente = document.getElementById("idCliente").value.trim();
            const tipoSolicitud = document.getElementById("tipoSolicitud").value.trim();
            const descripcion = document.getElementById("descripcion").value.trim();

            if (idCliente === "" || tipoSolicitud === "" || descripcion === "") {
                alert("Por favor, completa todos los campos requeridos.");
                return false;
            }

            if (descripcion.length < 10) {
                alert("La descripción debe tener al menos 10 caracteres.");
                return false;
            }

            return true;
        }
    </script>
</head>
<body class="bg-gray-100">

<% 
    // Validación de sesión y permisos
    if (session.getAttribute("usuario") == null || (!session.getAttribute("cargo").equals("Jefe de Área") && !session.getAttribute("cargo").equals("Coordinador"))) {
        response.sendRedirect("login.jsp?error=sin-sesion");
        return; // Para evitar seguir procesando la página después del redireccionamiento
    }
%>

<header class="bg-blue-600 p-4 text-white">
    <h1 class="text-2xl font-bold">Crear Nueva Solicitud</h1>
</header>

<div class="container mx-auto mt-6 bg-white p-6 shadow-lg rounded-lg">
    <h1 class="text-2xl font-bold mb-6 text-center">Nueva Solicitud</h1>

    <!-- Mostrar mensaje si existe -->
    <% if (request.getAttribute("mensaje") != null) { %>
        <p class="text-green-600 text-center mb-4"><%= request.getAttribute("mensaje") %></p>
    <% } %>

    <form action="SolicitudServlet" method="post" class="space-y-6" onsubmit="return validarFormulario();">
        <div>
            <label for="idCliente" class="block text-sm font-medium text-gray-700">ID Cliente:</label>
            <input type="text" id="idCliente" name="idCliente" required minlength="1" maxlength="10" 
                class="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
                placeholder="Ingrese el ID del cliente" aria-label="ID del cliente">
        </div>

        <div>
            <label for="tipoSolicitud" class="block text-sm font-medium text-gray-700">Tipo de Solicitud:</label>
            <select id="tipoSolicitud" name="tipoSolicitud" required 
                class="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
                aria-label="Tipo de solicitud">
                <option value="" disabled selected>Selecciona un tipo de solicitud</option>
                <option value="Soporte técnico">Soporte técnico</option>
                <option value="Consulta general">Consulta general</option>
                <option value="Solicitud de información">Solicitud de información</option>
                <option value="Reclamación">Reclamación</option>
            </select>
        </div>

        <div>
            <label for="descripcion" class="block text-sm font-medium text-gray-700">Descripción:</label>
            <textarea id="descripcion" name="descripcion" required minlength="10" 
                class="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
                placeholder="Describa la solicitud" aria-label="Descripción de la solicitud"></textarea>
        </div>

        <div class="mt-6">
            <button type="submit" name="accion" value="guardar" 
                class="w-full bg-blue-500 text-white py-2 px-4 rounded-md hover:bg-blue-600">
                Crear Solicitud
            </button>
        </div>
    </form>
</div>

</body>
</html>
