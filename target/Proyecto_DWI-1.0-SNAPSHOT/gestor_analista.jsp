<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestor Analista Senior</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css">
</head>
<body class="bg-gray-100">

    <!-- Validación de sesión del usuario -->
    <c:if test="${sessionScope.usuario == null || sessionScope.usuario.colaborador.cargo != 'Analista Senior'}">
        <c:redirect url="login.jsp?error=sin-permiso"/>
    </c:if>

    <!-- Header -->
    <header class="bg-blue-600 p-4 text-white flex justify-between items-center">
        <div>
            <h1 class="text-2xl font-bold">Gestión Analista Senior</h1>
        </div>
        <div>
            <a href="cerrar_sesion.jsp" class="text-white hover:underline">Cerrar Sesión</a>
        </div>
    </header>

    <!-- Main Content -->
    <div class="container mx-auto mt-10 bg-white p-8 shadow-lg rounded-lg">
        <h2 class="text-xl font-bold mb-4">Bienvenido, ${sessionScope.usuario.nombreUsuario}</h2>
        <p class="mb-6 text-gray-700">Aquí puedes gestionar las solicitudes y actividades como Analista Senior.</p>

        <!-- Opciones -->
        <div class="space-y-4">
            <a href="listar_solicitudes.jsp" class="block bg-blue-500 text-white py-2 px-4 rounded-md hover:bg-blue-700 transition duration-300 ease-in-out">
                Ver Solicitudes
            </a>
            <a href="listar_actividades.jsp" class="block bg-blue-500 text-white py-2 px-4 rounded-md hover:bg-blue-700 transition duration-300 ease-in-out">
                Ver Actividades
            </a>
        </div>
    </div>

    <!-- Footer -->
    <footer class="bg-blue-600 p-4 text-white text-center mt-10">
        <p>&copy; 2024 123digit@l. Todos los derechos reservados.</p>
    </footer>

</body>
</html>
