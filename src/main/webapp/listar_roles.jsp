<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Listado de Roles</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css">
</head>
<body class="bg-gray-100">

    <!-- Validación de sesión de usuario -->
    <c:if test="${sessionScope.usuario == null}">
        <c:redirect url="login.jsp"/>
    </c:if>

    <!-- Verificar que el usuario tenga permisos para ver los roles (por ejemplo, Jefe de Área o Administrador) -->
    <c:if test="${sessionScope.usuario.colaborador == null || sessionScope.usuario.colaborador.cargo != 'Jefe de Área'}">
        <c:redirect url="index.jsp"/>
    </c:if>

    <header class="bg-blue-600 p-4 text-white flex justify-between items-center">
        <h1 class="text-2xl font-bold">Listado de Roles</h1>
        <nav>
            <a href="index.jsp" class="hover:text-gray-300">Inicio</a> |
            <a href="cerrar_sesion.jsp" class="hover:text-gray-300">Cerrar Sesión</a>
        </nav>
    </header>

    <div class="container mx-auto mt-6 bg-white p-6 shadow-lg rounded-lg">
        <h1 class="text-2xl font-bold mb-6 text-center">Roles Disponibles</h1>

        <!-- Verificar si hay roles disponibles -->
        <c:if test="${not empty roles}">
            <!-- Tabla de Roles -->
            <table class="table-auto w-full border-collapse bg-white shadow-md rounded-lg">
                <thead>
                    <tr class="bg-blue-600 text-white">
                        <th class="p-3 text-left">ID Rol</th>
                        <th class="p-3 text-left">Nombre Rol</th>
                        <th class="p-3 text-left">Descripción</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="rol" items="${roles}">
                        <tr class="bg-gray-100 border-b border-gray-200 hover:bg-gray-200">
                            <td class="p-3"><c:out value="${rol.idRol}"/></td>
                            <td class="p-3"><c:out value="${rol.nombreRol}"/></td>
                            <td class="p-3"><c:out value="${rol.descripcion}"/></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>

        <!-- Mostrar mensaje si no hay roles -->
        <c:if test="${empty roles}">
            <div class="text-center text-gray-500 mt-6">
                <p>No hay roles disponibles en este momento.</p>
            </div>
        </c:if>
    </div>

    <footer class="bg-gray-800 text-white py-6 mt-10 text-center">
        <p>&copy; 2024 123digit@l. Todos los derechos reservados.</p>
    </footer>

</body>
</html>
