<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="pe.edu.utp.isi.dwi.proyecto_dwi.dto.UsuarioDTO" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>123digit@l - Plataforma</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css">
        <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    </head>
    <body class="bg-gray-100">

        <!-- Validar sesión de usuario -->
        <%
            HttpSession sesionActual = request.getSession(false);
            UsuarioDTO usuarioLogueado = null;
            String mensajeExito = (String) request.getAttribute("mensajeExito");

            if (sesionActual != null) {
                usuarioLogueado = (UsuarioDTO) sesionActual.getAttribute("usuario");
            }
        %>

        <!-- Header -->
        <header class="bg-blue-600 p-4 text-white flex justify-between items-center">
            <div class="flex items-center">
                <img src="images/logo_empresa.png" alt="Logo Empresa" class="w-12 h-12 mr-4">
                <h1 class="text-2xl font-bold">123digit@l</h1>
            </div>
            <nav>
                <ul class="flex space-x-4">
                    <li><a href="index.jsp" class="hover:text-gray-300">Inicio</a></li>

                    <% if (usuarioLogueado == null) { %>
                        <li><a href="registro_usuario.jsp" class="hover:text-gray-300">Registro de Usuario</a></li>
                        <li><a href="login.jsp" class="hover:text-gray-300">Iniciar Sesión</a></li>
                    <% } else { %>
                        <li>
                            <img src="/images/${sessionScope.usuario.fotoPerfil}" alt="Imagen de perfil">
                            <a href="perfil_usuario.jsp" class="hover:text-gray-300">Perfil de Usuario (<%= usuarioLogueado.getNombreUsuario() %>)</a>
                        </li>
                        <li><a href="logout.jsp" class="hover:text-gray-300">Cerrar Sesión</a></li>

                        <!-- Menú dinámico según el cargo del usuario -->
                        <% if (usuarioLogueado.getColaborador() != null) {
                            String cargo = usuarioLogueado.getColaborador().getCargo();
                            if ("Jefe de Área".equals(cargo)) { %>
                                <li><a href="gestor_area.jsp" class="hover:text-gray-300">Gestor Área</a></li>
                                <li><a href="listar_usuarios.jsp" class="hover:text-gray-300">Gestión de Usuarios</a></li>
                                <li><a href="listar_solicitudes.jsp" class="hover:text-gray-300">Gestión de Solicitudes</a></li>
                                <li><a href="listar_asignaciones.jsp" class="hover:text-gray-300">Gestión de Asignaciones</a></li>
                                <li><a href="listar_actividades.jsp" class="hover:text-gray-300">Gestión de Actividades</a></li>
                            <% } else if ("Coordinador".equals(cargo)) { %>
                                <li><a href="gestor_coordinador.jsp" class="hover:text-gray-300">Gestor Coordinador</a></li>
                                <li><a href="listar_solicitudes.jsp" class="hover:text-gray-300">Gestión de Solicitudes</a></li>
                                <li><a href="listar_asignaciones.jsp" class="hover:text-gray-300">Gestión de Asignaciones</a></li>
                                <li><a href="listar_actividades.jsp" class="hover:text-gray-300">Gestión de Actividades</a></li>
                            <% } else if ("Analista Senior".equals(cargo)) { %>
                                <li><a href="gestor_analista.jsp" class="hover:text-gray-300">Gestor Analista Senior</a></li>
                                <li><a href="listar_solicitudes.jsp" class="hover:text-gray-300">Ver Solicitudes</a></li>
                                <li><a href="listar_actividades.jsp" class="hover:text-gray-300">Ver Actividades</a></li>
                            <% } else if ("Programador Junior".equals(cargo)) { %>
                                <li><a href="gestor_programador.jsp" class="hover:text-gray-300">Gestor Programador Junior</a></li>
                                <li><a href="listar_solicitudes.jsp" class="hover:text-gray-300">Ver Solicitudes</a></li>
                                <li><a href="listar_actividades.jsp" class="hover:text-gray-300">Ver Actividades</a></li>
                            <% }
                        } %>
                    <% } %>
                </ul>
            </nav>
        </header>

        <!-- Mostrar mensaje de éxito si existe -->
        <%
            if (mensajeExito != null) {
        %>
        <div class="bg-green-500 text-white p-4 rounded-md mt-4 text-center mx-auto w-3/4">
            <%= mensajeExito %>
        </div>
        <%
            }
        %>

        <!-- Hero Section -->
        <section class="bg-blue-500 text-white py-20 text-center">
            <h2 class="text-4xl font-bold">Bienvenidos a 123digit@l</h2>
            <p class="mt-4 text-lg">Soluciones innovadoras en software para tu empresa</p>
            <div class="mt-6">
                <% if (usuarioLogueado != null) { %>
                    <a href="listar_usuarios.jsp" class="bg-white text-blue-500 px-4 py-2 rounded-md shadow-md hover:bg-gray-100">Ver Usuarios</a>
                    <a href="listar_solicitudes.jsp" class="bg-white text-blue-500 px-4 py-2 rounded-md shadow-md hover:bg-gray-100 ml-4">Ver Solicitudes</a>
                <% } else { %>
                    <a href="registro_usuario.jsp" class="bg-white text-blue-500 px-4 py-2 rounded-md shadow-md hover:bg-gray-100">Regístrate Ahora</a>
                <% } %>
            </div>
        </section>

        <!-- Main content -->
        <main class="container mx-auto mt-10">
            <section class="text-center mb-20">
                <h3 class="text-3xl font-semibold mb-4">¿Qué hacemos?</h3>
                <p class="text-lg text-gray-700 max-w-2xl mx-auto">En 123digit@l, ofrecemos una plataforma completa para la gestión de usuarios, solicitudes, asignaciones y actividades, especialmente diseñada para optimizar el flujo de trabajo en empresas de software. Nuestra meta es proporcionarte herramientas eficaces para la automatización de procesos y la mejora continua.</p>
            </section>

            <!-- Features Section -->
            <section class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-10 text-center">
                <div class="p-6 bg-white rounded-lg shadow-lg">
                    <i class="fas fa-users text-4xl text-blue-500"></i>
                    <h4 class="text-xl font-bold mt-4">Gestión de Usuarios</h4>
                    <p class="mt-2 text-gray-600">Administra usuarios de manera eficiente y fácil con nuestra plataforma.</p>
                </div>
                <div class="p-6 bg-white rounded-lg shadow-lg">
                    <i class="fas fa-file-alt text-4xl text-blue-500"></i>
                    <h4 class="text-xl font-bold mt-4">Gestión de Solicitudes</h4>
                    <p class="mt-2 text-gray-600">Gestiona y organiza todas las solicitudes de tus clientes.</p>
                </div>
                <div class="p-6 bg-white rounded-lg shadow-lg">
                    <i class="fas fa-tasks text-4xl text-blue-500"></i>
                    <h4 class="text-xl font-bold mt-4">Asignación de Actividades</h4>
                    <p class="mt-2 text-gray-600">Asigna tareas y gestiona actividades de forma ordenada y eficaz.</p>
                </div>
                <div class="p-6 bg-white rounded-lg shadow-lg">
                    <i class="fas fa-chart-line text-4xl text-blue-500"></i>
                    <h4 class="text-xl font-bold mt-4">Reportes y Estadísticas</h4>
                    <p class="mt-2 text-gray-600">Genera reportes detallados para análisis y toma de decisiones.</p>
                </div>
            </section>
        </main>

        <!-- Footer -->
        <footer class="bg-gray-800 text-white py-6 mt-20 text-center">
            <p>&copy; 2024 123digit@l. Todos los derechos reservados.</p>
            <p>Tu socio en soluciones de software innovadoras.</p>
        </footer>

        <script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/js/all.min.js"></script>
    </body>
</html>
