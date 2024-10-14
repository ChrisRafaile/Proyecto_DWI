<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="pe.edu.utp.isi.dwi.proyecto_dwi.entities.Usuario" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%
    // Validación de sesión
    HttpSession sesion = request.getSession(false);
    Usuario usuarioLogueado = null;
    if (sesion != null) {
        usuarioLogueado = (Usuario) sesion.getAttribute("usuario");
    }

    if (usuarioLogueado == null || !usuarioLogueado.getColaborador().getCargo().equals("Programador Junior")) {
        response.sendRedirect("login.jsp?error=sin-permiso");
        return; // Evitar seguir procesando la página
    }
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Gestor Programador Junior</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css"> <!-- Incluye iconos -->
    </head>
    <body class="bg-gray-100">
        <!-- Header -->
        <header class="bg-blue-600 p-4 text-white flex justify-between items-center">
            <div>
                <h1 class="text-2xl font-bold">Bienvenido, <%= usuarioLogueado != null ? usuarioLogueado.getNombreUsuario() : "Usuario"%></h1>
                <h2 class="text-xl mt-2">Programador Junior</h2>
            </div>
            <div>
                <a href="cerrar_sesion.jsp" class="text-white hover:underline"><i class="fas fa-sign-out-alt"></i> Cerrar Sesión</a>
            </div>
        </header>

        <!-- Main Content -->
        <div class="container mx-auto mt-10 p-6 bg-white shadow-lg rounded-lg">
            <h3 class="text-xl font-bold mb-6">Opciones Disponibles</h3>
            <ul class="space-y-2">
                <li>
                    <a href="listar_solicitudes.jsp" class="block text-blue-500 hover:underline hover:text-blue-700 transition duration-200">
                        <i class="fas fa-file-alt mr-2"></i> Ver Solicitudes
                    </a>
                </li>
                <li>
                    <a href="listar_actividades.jsp" class="block text-blue-500 hover:underline hover:text-blue-700 transition duration-200">
                        <i class="fas fa-tasks mr-2"></i> Ver Actividades
                    </a>
                </li>
            </ul>
        </div>

        <!-- Footer -->
        <footer class="bg-blue-600 p-4 text-white text-center mt-10">
            <p>&copy; 2024 123digit@l. Todos los derechos reservados.</p>
        </footer>
    </body>
</html>
