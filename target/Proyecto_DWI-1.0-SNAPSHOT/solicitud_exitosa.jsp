<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Solicitud Exitosa</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css">
    <style>
        .hover\:scale-105:hover {
            transform: scale(1.05);
        }
    </style>
</head>
<body class="bg-gray-100 text-center py-20">

<% 
    // Validación de sesión y permisos
    if (session.getAttribute("usuario") == null) {
        response.sendRedirect("login.jsp?error=sin-sesion");
        return; // Para evitar seguir procesando la página después del redireccionamiento
    }
%>

    <div class="container mx-auto" aria-live="polite">
        <div class="flex justify-center items-center space-x-3">
            <svg class="w-8 h-8 text-green-500" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
            </svg>
            <h1 class="text-3xl font-bold text-green-500">¡Solicitud Procesada Exitosamente!</h1>
        </div>
        <p class="mt-4 text-lg text-gray-700">Tu solicitud ha sido registrada correctamente.</p>
        <p class="mt-4 text-sm text-gray-500">Gracias por utilizar nuestros servicios. Si tienes alguna otra solicitud, no dudes en contactar con nosotros.</p>
        <a href="SolicitudServlet?accion=listar" class="mt-8 inline-block bg-blue-500 text-white py-2 px-4 rounded-md hover:bg-blue-600 hover:shadow-lg transition duration-300 ease-in-out transform hover:scale-105">
            Volver a la lista de solicitudes
        </a>
        <p class="mt-4 text-sm text-gray-500">Serás redirigido a la lista de solicitudes en unos momentos...</p>
    </div>

    <!-- Redirección automática -->
    <script>
        setTimeout(function() {
            window.location.href = "SolicitudServlet?accion=listar";
        }, 5000); // Redirigir después de 5 segundos
    </script>
</body>
</html>
