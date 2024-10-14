<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro Exitoso</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet">

    <!-- Redirigir a login.jsp en 5 segundos -->
    <meta http-equiv="refresh" content="5;url=login.jsp" />

    <style>
        .fade-in {
            animation: fadeIn 1s ease-in-out;
        }
        @keyframes fadeIn {
            from {
                opacity: 0;
            }
            to {
                opacity: 1;
            }
        }
    </style>
</head>
<body class="bg-gray-100 text-center py-20">
    <div class="max-w-lg mx-auto bg-white p-8 shadow-lg rounded-lg fade-in">
        <div class="flex justify-center items-center space-x-3 fade-in">
            <svg class="w-8 h-8 text-green-500" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
            </svg>
            <h1 class="text-3xl font-bold text-green-500">¡Registro Exitoso!</h1>
        </div>
        <p class="mt-4 text-lg text-gray-700 fade-in">Tu registro ha sido exitoso. Revisa tu correo para completar la verificación.</p>
        
        <!-- Enlace para ir a la página de inicio de sesión -->
        <a href="login.jsp" class="mt-6 inline-block bg-blue-500 text-white py-2 px-4 rounded-md hover:bg-blue-600 hover:shadow-lg transition duration-300 ease-in-out transform hover:scale-105" aria-label="Iniciar sesión">Iniciar Sesión</a>
    </div>

    <p class="mt-4 text-sm text-gray-600 fade-in" aria-live="polite">
        Serás redirigido automáticamente en <span id="countdown" class="font-bold text-blue-600">5</span> segundos...
    </p>

    <script>
        // Temporizador para la cuenta regresiva
        let countdownElement = document.getElementById('countdown');
        let seconds = 5;
        let countdownInterval = setInterval(() => {
            seconds--;
            countdownElement.textContent = seconds;
            if (seconds <= 0) {
                clearInterval(countdownInterval);
            }
        }, 1000);
    </script>
</body>
</html>
