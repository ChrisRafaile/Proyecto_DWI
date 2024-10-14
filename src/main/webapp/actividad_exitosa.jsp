<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Actividad Registrada</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css">
</head>
<body class="bg-gray-100">

    <!-- Validación de sesión de usuario -->
    <c:choose>
        <c:when test="${sessionScope.usuario == null}">
            <!-- Si no hay una sesión de usuario activa, redirigir al login -->
            <c:redirect url="login.jsp?error=sin-sesion"/>
        </c:when>
        <c:otherwise>
            <!-- Mensaje de confirmación de actividad registrada -->
            <div class="container mx-auto mt-10 bg-white p-6 shadow-lg rounded-lg text-center">
                <h1 class="text-2xl font-bold mb-6 text-green-500">¡Actividad Registrada Exitosamente!</h1>
                <p class="text-lg text-gray-700 mb-6">La actividad ha sido registrada correctamente.</p>
                
                <!-- Enlace para volver a la lista de actividades -->
                <a href="listar_actividades.jsp" class="bg-blue-500 text-white py-2 px-4 rounded-md hover:bg-blue-600 transition duration-300 ease-in-out" aria-label="Volver a la lista de actividades">
                    Volver a la lista de actividades
                </a>
                
                <!-- Mensaje de redirección automática -->
                <p class="text-sm text-gray-500 mt-4">Será redirigido automáticamente en <span id="countdown">5</span> segundos...</p>
                
                <!-- Script para redirigir automáticamente después de una cuenta regresiva -->
                <script>
                    let countdown = 5;
                    const countdownElement = document.getElementById('countdown');
                    const interval = setInterval(() => {
                        countdown--;
                        countdownElement.textContent = countdown;
                        if (countdown === 0) {
                            clearInterval(interval);
                            window.location.href = 'listar_actividades.jsp';
                        }
                    }, 1000);
                </script>
            </div>
        </c:otherwise>
    </c:choose>

</body>
</html>
