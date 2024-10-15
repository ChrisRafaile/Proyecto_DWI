<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cerrando Sesión</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css">
    <style>
        .spinner {
            border: 4px solid rgba(0, 0, 255, 0.1);
            border-left-color: #3498db;
            border-radius: 50%;
            width: 40px;
            height: 40px;
            animation: spin 1s linear infinite;
            margin: 20px auto;
        }
        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }
    </style>
</head>
<body class="bg-gray-100 text-center py-20">
    <div class="max-w-md mx-auto p-6 bg-white rounded-lg shadow-md" aria-live="polite">
        <h1 class="text-3xl font-bold text-blue-500">Cerrando Sesión...</h1>
        <p id="mensajeCarga" class="mt-4 text-gray-600">Por favor, espera mientras cerramos tu sesión.</p>
        <div class="spinner" aria-label="Cargando"></div> <!-- Texto alternativo para accesibilidad -->
    </div>

    <%
        // Invalidar la sesión actual para finalizar la autenticación del usuario
        session.invalidate();
        
        // Tiempo de espera en segundos antes de redirigir
        int tiempoEspera = 2; 

        // Redirigir al login después de 'tiempoEspera' segundos usando el header HTTP
        response.setHeader("Refresh", tiempoEspera + "; URL=login.jsp");
    %>

    <!-- Redirección alternativa con JavaScript -->
    <script>
        const tiempoEspera = <%= tiempoEspera %> * 1000; // Convertir segundos a milisegundos
        setTimeout(function () {
            window.location.href = "login.jsp";
        }, tiempoEspera);

        // Actualizar mensaje durante la espera
        let contador = 0;
        const mensajes = [
            "Cerrando sesión...",
            "Desconectando...",
            "Espere por favor..."
        ];
        setInterval(() => {
            contador = (contador + 1) % mensajes.length;
            document.getElementById("mensajeCarga").innerText = mensajes[contador];
        }, 1000);
    </script>

    <!-- Mensaje alternativo si la redirección automática no funciona -->
    <p class="mt-4">
        Si no redirige automáticamente, por favor haga clic en 
        <a href="login.jsp" class="text-blue-500 hover:underline">aquí</a>.
    </p>

</body>
</html>
