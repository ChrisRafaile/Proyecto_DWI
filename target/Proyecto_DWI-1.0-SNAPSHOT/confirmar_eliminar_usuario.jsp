<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Confirmar Eliminación</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css">
</head>
<body class="bg-gray-100">

    <!-- Validación de sesión de usuario y permisos -->
    <c:choose>
        <c:when test="${sessionScope.usuario == null}">
            <!-- Redirigir si no hay sesión activa -->
            <c:redirect url="login.jsp?error=sin-sesion"/>
        </c:when>
        <c:when test="${sessionScope.usuario.colaborador.cargo != 'Jefe de Área' && sessionScope.usuario.colaborador.cargo != 'Coordinador'}">
            <!-- Redirigir si el usuario no tiene los permisos adecuados -->
            <c:redirect url="index.jsp?error=sin-permiso"/>
        </c:when>
        <c:otherwise>
            <!-- Página de confirmación de eliminación -->
            <div class="min-h-screen flex items-center justify-center">
                <div class="bg-white p-6 rounded-lg shadow-lg text-center">
                    <h2 class="text-2xl font-bold text-red-600 mb-4">
                        ¿Estás seguro de que deseas eliminar el usuario 
                        <span class="text-black">${param.nombreUsuario}</span>?
                    </h2>
                    <p class="text-gray-700 mb-6">Esta acción no se puede deshacer. El usuario será eliminado permanentemente.</p>

                    <!-- Formulario para confirmar la eliminación -->
                    <form action="UsuarioServlet" method="post" onsubmit="return confirmarEliminacion();">
                        <!-- Acción y ID del usuario a eliminar -->
                        <input type="hidden" name="accion" value="eliminar">
                        <input type="hidden" name="idUsuario" value="${param.id}">

                        <!-- Botones de confirmación -->
                        <div class="flex justify-center space-x-4">
                            <button type="submit" class="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600 transition duration-300 ease-in-out" aria-label="Confirmar eliminación del usuario">Eliminar</button>
                            <a href="UsuarioServlet?accion=listar" class="bg-gray-300 text-black px-4 py-2 rounded hover:bg-gray-400 transition duration-300 ease-in-out" aria-label="Cancelar eliminación">Cancelar</a>
                        </div>
                    </form>
                </div>
            </div>

            <!-- Confirmación adicional por JavaScript -->
            <script>
                function confirmarEliminacion() {
                    return confirm("¿Estás seguro de que deseas eliminar este usuario? Esta acción no se puede deshacer.");
                }
            </script>
        </c:otherwise>
    </c:choose>

</body>
</html>
