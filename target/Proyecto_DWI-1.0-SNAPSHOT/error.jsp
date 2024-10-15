<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page isErrorPage="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css">
</head>
<body class="bg-gray-100">

    <!-- Contenedor del mensaje de error -->
    <div class="container mx-auto mt-10 bg-white p-6 shadow-lg rounded-lg text-center">
        <h1 class="text-2xl font-bold text-red-600">Ocurrió un error</h1>
        <p class="mt-4 text-lg text-gray-700">Lo sentimos, algo salió mal. Por favor, intenta nuevamente más tarde.</p>

        <!-- Detalles del error: Mostrar solo si está en modo depuración -->
        <c:if test="${not empty pageContext.errorData.throwable}">
            <p class="text-gray-600 mt-4">
                Detalles del error: ${pageContext.errorData.throwable.message}
            </p>
        </c:if>

        <!-- Enlaces para volver al inicio o intentar de nuevo -->
        <div class="mt-6">
            <a href="index.jsp" class="inline-block bg-blue-500 text-white py-2 px-4 rounded-md hover:bg-blue-600 transition duration-300 ease-in-out">
                Volver al inicio
            </a>
            <button onclick="history.back()" class="mt-4 inline-block bg-gray-300 text-black py-2 px-4 rounded-md hover:bg-gray-400 transition duration-300 ease-in-out">
                Intentar de Nuevo
            </button>
        </div>
    </div>

</body>
</html>
