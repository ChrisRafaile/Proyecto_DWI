<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Rol</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css">
</head>
<body class="bg-gray-100">

    <!-- Validación de sesión de usuario -->
    <c:if test="${sessionScope.usuario == null}">
        <c:redirect url="login.jsp"/>
    </c:if>

    <!-- Header -->
    <header class="bg-blue-600 p-4 text-white">
        <h1 class="text-2xl font-bold">Editar Rol</h1>
    </header>

    <div class="container mx-auto mt-6 bg-white p-6 shadow-lg rounded-lg">
        <h2 class="text-2xl font-bold mb-6 text-center">Editar Rol</h2>

        <!-- Mostrar mensaje de error si existe -->
        <c:if test="${not empty mensajeError}">
            <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-6">
                ${mensajeError}
            </div>
        </c:if>

        <!-- Formulario para editar rol -->
        <form action="roles" method="post" class="space-y-6" onsubmit="return validarFormulario();">
            <!-- Campo oculto para la acción y el ID del rol -->
            <input type="hidden" name="accion" value="editar">
            <input type="hidden" name="idRol" value="${rol.idRol}">

            <!-- Campo de nombre del rol -->
            <div>
                <label for="nombreRol" class="block text-sm font-medium text-gray-700">Nombre del Rol:</label>
                <input type="text" id="nombreRol" name="nombreRol" value="${rol.nombreRol}" required class="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:border-blue-500 focus:ring focus:ring-blue-500 focus:ring-opacity-50">
            </div>

            <!-- Campo de descripción del rol -->
            <div>
                <label for="descripcion" class="block text-sm font-medium text-gray-700">Descripción:</label>
                <textarea id="descripcion" name="descripcion" required class="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:border-blue-500 focus:ring focus:ring-blue-500 focus:ring-opacity-50">${rol.descripcion}</textarea>
            </div>

            <!-- Botones para guardar o cancelar -->
            <div class="flex justify-end space-x-4">
                <button type="submit" class="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 transition duration-300 ease-in-out">
                    Guardar Cambios
                </button>
                <a href="roles?accion=listar" class="bg-gray-300 text-black px-4 py-2 rounded-md hover:bg-gray-400 transition duration-300 ease-in-out">
                    Cancelar
                </a>
            </div>
        </form>
    </div>

    <!-- Footer -->
    <footer class="bg-blue-600 p-4 text-white text-center mt-10">
        <p>&copy; 2024 123digit@l. Todos los derechos reservados.</p>
    </footer>

</body>
</html>
