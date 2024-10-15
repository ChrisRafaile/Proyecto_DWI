<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="pe.edu.utp.isi.dwi.proyecto_dwi.entities.Usuario" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%
    // Validación de sesión
    HttpSession sesion = request.getSession(false);
    Usuario usuarioLogueado = (Usuario) (sesion != null ? sesion.getAttribute("usuario") : null);
    if (usuarioLogueado == null || !usuarioLogueado.getColaborador().getCargo().equals("Jefe de Área")) {
        response.sendRedirect("login.jsp?error=sin-permiso");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Gestor Jefe de Área</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    </head>
    <body class="bg-gray-100">

        <!-- Header -->
        <header class="bg-blue-600 p-4 text-white flex justify-between items-center">
            <h1 class="text-2xl font-bold">Gestión Jefe de Área</h1>
            <!-- Notificaciones y Cerrar Sesión -->
            <div class="flex items-center space-x-4">
                <!-- Notificaciones -->
                <div class="relative">
                    <a href="Notificacion?accion=listar" class="flex items-center">
                        <i class="fa fa-bell text-white text-2xl"></i>
                        <c:if test="${notificacionesPendientes > 0}">
                            <span class="absolute top-0 right-0 bg-red-500 text-white rounded-full px-2 text-sm">
                                ${notificacionesPendientes}
                            </span>
                        </c:if>
                    </a>
                </div>
                <!-- Cerrar Sesión -->
                <a href="cerrar_sesion.jsp" class="text-white hover:underline"><i class="fas fa-sign-out-alt"></i> Cerrar Sesión</a>
            </div>
        </header>

        <!-- Main Content -->
        <div class="container mx-auto mt-10 bg-white p-8 shadow-lg rounded-lg">
            <h2 class="text-xl font-bold mb-4">Bienvenido, <%= usuarioLogueado.getNombreUsuario()%></h2>
            <p class="mb-6 text-gray-700">Aquí puedes gestionar los usuarios, solicitudes, asignaciones y actividades como Jefe de Área.</p>

            <!-- Opciones -->
            <div class="space-y-4">
                <a href="listar_usuarios.jsp" class="block bg-blue-500 text-white py-2 px-4 rounded-md hover:bg-blue-600 transition duration-300 ease-in-out">Gestión de Usuarios</a>
                <a href="listar_solicitudes.jsp" class="block bg-blue-500 text-white py-2 px-4 rounded-md hover:bg-blue-600 transition duration-300 ease-in-out">Gestión de Solicitudes</a>
                <a href="listar_asignaciones.jsp" class="block bg-blue-500 text-white py-2 px-4 rounded-md hover:bg-blue-600 transition duration-300 ease-in-out">Gestión de Asignaciones</a>
                <a href="listar_actividades.jsp" class="block bg-blue-500 text-white py-2 px-4 rounded-md hover:bg-blue-600 transition duration-300 ease-in-out">Gestión de Actividades</a>
                <!-- Enlace al Dashboard -->
                <a href="dashboard.jsp" class="block bg-green-500 text-white py-2 px-4 rounded-md hover:bg-green-600 transition duration-300 ease-in-out">Ver Dashboard</a>
            </div>
        </div>

        <!-- Footer -->
        <footer class="bg-blue-600 p-4 text-white text-center mt-10">
            <p>&copy; 2024 123digit@l. Todos los derechos reservados.</p>
        </footer>

    </body>
</html>
