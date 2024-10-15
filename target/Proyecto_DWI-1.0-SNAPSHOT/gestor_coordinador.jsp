<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="pe.edu.utp.isi.dwi.proyecto_dwi.entities.Usuario" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%
    // Validación de sesión
    HttpSession sesion = request.getSession(false);
    Usuario usuarioLogueado = (Usuario) (sesion != null ? sesion.getAttribute("usuario") : null);
    if (usuarioLogueado == null || !usuarioLogueado.getColaborador().getCargo().equals("Coordinador")) {
        response.sendRedirect("login.jsp?error=sin-permiso");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestor Coordinador</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css">
</head>
<body class="bg-gray-100">

    <!-- Header -->
    <header class="bg-blue-600 p-4 text-white flex justify-between items-center">
        <div>
            <h1 class="text-2xl font-bold">Gestión Coordinador</h1>
            <h2 class="text-xl mt-2">Bienvenido, <%= usuarioLogueado.getNombreUsuario() %></h2>
        </div>
        <div>
            <a href="cerrar_sesion.jsp" class="text-white hover:underline">Cerrar Sesión</a>
        </div>
    </header>

    <!-- Main Content -->
    <div class="container mx-auto mt-10 bg-white p-8 shadow-lg rounded-lg">
        <p class="mb-6 text-gray-700">Aquí puedes gestionar las solicitudes, asignaciones y actividades asignadas como Coordinador.</p>

        <!-- Opciones -->
        <div class="space-y-4">
            <a href="listar_solicitudes.jsp" class="block bg-blue-500 text-white py-2 px-4 rounded-md hover:bg-blue-600 transition duration-300 ease-in-out">Gestión de Solicitudes</a>
            <a href="listar_asignaciones.jsp" class="block bg-blue-500 text-white py-2 px-4 rounded-md hover:bg-blue-600 transition duration-300 ease-in-out">Gestión de Asignaciones</a>
            <a href="listar_actividades.jsp" class="block bg-blue-500 text-white py-2 px-4 rounded-md hover:bg-blue-600 transition duration-300 ease-in-out">Gestión de Actividades</a>
        </div>
    </div>

    <!-- Footer -->
    <footer class="bg-blue-600 p-4 text-white text-center mt-10">
        <p>&copy; 2024 123digit@l. Todos los derechos reservados.</p>
    </footer>

</body>
</html>
