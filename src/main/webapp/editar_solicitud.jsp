<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Solicitud</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css">
    <script>
        function validarFormulario() {
            const idCliente = document.querySelector('[name="idCliente"]').value.trim();
            const descripcion = document.querySelector('[name="descripcion"]').value.trim();

            if (idCliente === "" || descripcion === "") {
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

    <!-- Validación de sesión de usuario -->
    <c:if test="${sessionScope.usuario == null}">
        <c:redirect url="login.jsp"/>
    </c:if>

    <header class="bg-blue-600 p-4 text-white">
        <h1 class="text-2xl font-bold">Editar Solicitud</h1>
    </header>

    <div class="container mx-auto mt-6 bg-white p-6 shadow-lg rounded-lg">
        <h1 class="text-2xl font-bold mb-6 text-center">Editar Solicitud</h1>
        <form action="SolicitudServlet" method="post" class="space-y-6" onsubmit="return validarFormulario();">
            <!-- ID de la solicitud oculto -->
            <input type="hidden" name="idSolicitud" value="${solicitud.idSolicitud}"/>

            <!-- Campo para ID del Cliente -->
            <div>
                <label class="block text-sm font-medium text-gray-700">ID Cliente:</label>
                <input type="text" name="idCliente" value="${solicitud.idCliente}" required class="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:border-blue-500 focus:ring focus:ring-blue-500 focus:ring-opacity-50">
            </div>

            <!-- Campo para Tipo de Solicitud -->
            <div>
                <label class="block text-sm font-medium text-gray-700">Tipo de Solicitud:</label>
                <input type="text" name="tipoSolicitud" value="${solicitud.tipoSolicitud}" required class="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:border-blue-500 focus:ring focus:ring-blue-500 focus:ring-opacity-50">
            </div>

            <!-- Campo para Descripción -->
            <div>
                <label class="block text-sm font-medium text-gray-700">Descripción:</label>
                <textarea name="descripcion" required class="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:border-blue-500 focus:ring focus:ring-blue-500 focus:ring-opacity-50">${solicitud.descripcion}</textarea>
            </div>

            <!-- Campo para Estado -->
            <div>
                <label class="block text-sm font-medium text-gray-700">Estado:</label>
                <select name="estado" required class="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:border-blue-500 focus:ring focus:ring-blue-500 focus:ring-opacity-50">
                    <option value="pendiente" <c:if test="${solicitud.estado == 'pendiente'}">selected</c:if>>Pendiente</option>
                    <option value="completado" <c:if test="${solicitud.estado == 'completado'}">selected</c:if>>Completado</option>
                </select>
            </div>

            <!-- Botón para guardar los cambios -->
            <div class="mt-6">
                <button type="submit" name="accion" value="actualizar" class="w-full bg-blue-500 text-white py-2 px-4 rounded-md hover:bg-blue-600 transition duration-300 ease-in-out">Guardar Cambios</button>
            </div>
        </form>
    </div>

    <!-- Footer -->
    <footer class="bg-blue-600 p-4 text-white text-center mt-10">
        <p>&copy; 2024 123digit@l. Todos los derechos reservados.</p>
    </footer>

</body>
</html>
