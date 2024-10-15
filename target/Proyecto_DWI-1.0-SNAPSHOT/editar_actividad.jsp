<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> <!-- JSTL para formatear fecha -->
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Actividad</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css">
    <script>
        function validarFormulario() {
            const nombre = document.getElementById('nombre').value.trim();
            const descripcion = document.getElementById('descripcion').value.trim();
            const fecha = document.getElementById('fecha').value.trim();

            if (nombre === "" || descripcion === "" || fecha === "") {
                alert("Por favor, completa todos los campos requeridos.");
                return false;
            }

            return true;
        }
    </script>
</head>
<body class="bg-gray-100">

    <!-- Validación de sesión de usuario -->
    <c:if test="${sessionScope.usuario == null}">
        <c:redirect url="login.jsp?error=sin-permiso"/>
    </c:if>

    <!-- Header -->
    <header class="bg-blue-600 p-4 text-white">
        <h1 class="text-2xl font-bold">Editar Actividad</h1>
    </header>

    <!-- Contenido principal -->
    <div class="container mx-auto mt-6 bg-white p-6 shadow-lg rounded-lg">
        <h1 class="text-2xl font-bold mb-6 text-center">Editar Actividad</h1>
        <form action="ActividadServlet" method="post" class="space-y-6" onsubmit="return validarFormulario();">
            <!-- Campo oculto para el ID de la actividad -->
            <input type="hidden" name="idActividad" value="${actividad.idActividad}"/>

            <!-- Nombre de la actividad -->
            <div>
                <label for="nombre" class="block text-sm font-medium text-gray-700">Nombre de la Actividad:</label>
                <input type="text" name="nombre" id="nombre" value="${actividad.nombre}" required class="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500">
            </div>

            <!-- Descripción de la actividad -->
            <div>
                <label for="descripcion" class="block text-sm font-medium text-gray-700">Descripción:</label>
                <textarea name="descripcion" id="descripcion" required class="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500">${actividad.descripcion}</textarea>
            </div>

            <!-- Fecha de la actividad -->
            <div>
                <label for="fecha" class="block text-sm font-medium text-gray-700">Fecha:</label>
                <input type="datetime-local" name="fecha" id="fecha"
                       value="<fmt:formatDate value='${actividad.fecha}' pattern='yyyy-MM-dd\'T\'HH:mm'/>"
                       required class="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500">
            </div>

            <!-- Botón para guardar los cambios -->
            <div class="mt-6">
                <button type="submit" name="accion" value="actualizar" class="w-full bg-blue-500 text-white py-2 px-4 rounded-md hover:bg-blue-600 transition duration-300 ease-in-out">
                    Guardar Cambios
                </button>
            </div>
        </form>
    </div>

</body>
</html>
