<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lista de Usuarios</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css">
</head>
<body class="bg-gray-100">

    <!-- Validación de sesión de usuario -->
    <c:if test="${sessionScope.usuario == null}">
        <c:redirect url="login.jsp"/>
    </c:if>

    <!-- Verificar que el usuario sea Jefe de Área o Coordinador -->
    <c:if test="${sessionScope.usuario.colaborador != null && sessionScope.usuario.colaborador.cargo != 'Jefe de Área' && sessionScope.usuario.colaborador.cargo != 'Coordinador'}">
        <c:redirect url="index.jsp"/>
    </c:if>

    <header class="bg-blue-600 p-4 text-white flex justify-between items-center">
        <h1 class="text-2xl font-bold">Gestión de Usuarios</h1>
        <nav>
            <a href="index.jsp" class="hover:text-gray-300">Inicio</a> |
            <a href="cerrar_sesion.jsp" class="hover:text-gray-300">Cerrar Sesión</a>
        </nav>
    </header>

    <div class="container mx-auto mt-6 bg-white p-6 shadow-lg rounded-lg">
        <h1 class="text-2xl font-bold mb-6 text-center">Lista de Usuarios</h1>

        <!-- Formulario de búsqueda y filtro -->
        <form action="Usuario?accion=listar" method="get" class="mb-6 flex justify-between items-center">
            <input type="text" name="filtroNombre" placeholder="Buscar por nombre" value="<c:out value='${param.filtroNombre}'/>" class="w-1/3 p-2 border border-gray-300 rounded-md" aria-label="Buscar por nombre de usuario">
            <input type="text" name="filtroCorreo" placeholder="Buscar por correo" value="<c:out value='${param.filtroCorreo}'/>" class="w-1/3 p-2 border border-gray-300 rounded-md" aria-label="Buscar por correo electrónico">
            <select name="filtroCargo" class="w-1/4 p-2 border border-gray-300 rounded-md" aria-label="Filtrar por cargo">
                <option value="">Todos los cargos</option>
                <option value="Jefe de Área" <c:if test="${param.filtroCargo == 'Jefe de Área'}">selected</c:if>>Jefe de Área</option>
                <option value="Analista Senior" <c:if test="${param.filtroCargo == 'Analista Senior'}">selected</c:if>>Analista Senior</option>
                <option value="Programador Junior" <c:if test="${param.filtroCargo == 'Programador Junior'}">selected</c:if>>Programador Junior</option>
                <option value="Coordinador" <c:if test="${param.filtroCargo == 'Coordinador'}">selected</c:if>>Coordinador</option>
            </select>
            <button type="submit" class="bg-blue-500 text-white p-2 rounded-md ml-2 hover:bg-blue-600" aria-label="Buscar usuarios">Buscar</button>
        </form>

        <!-- Verificar si hay usuarios disponibles -->
        <c:if test="${not empty usuarios}">
            <!-- Tabla de Usuarios -->
            <table class="table-auto w-full border-collapse bg-white shadow-md rounded-lg">
                <thead>
                    <tr class="bg-blue-600 text-white">
                        <th class="p-3 text-left">ID Usuario</th>
                        <th class="p-3 text-left">Nombre</th>
                        <th class="p-3 text-left">Correo</th>
                        <th class="p-3 text-left">Cargo</th>
                        <th class="p-3 text-left">Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="usuario" items="${usuarios}">
                        <tr class="bg-gray-100 border-b border-gray-200 hover:bg-gray-200">
                            <td class="p-3"><c:out value="${usuario.idUsuario}"/></td>
                            <td class="p-3"><c:out value="${usuario.nombreUsuario}"/></td>
                            <td class="p-3"><c:out value="${usuario.correo}"/></td>
                            <td class="p-3">
                                <c:if test="${usuario.colaborador != null}">
                                    <c:out value="${usuario.colaborador.cargo}"/>
                                </c:if>
                            </td>
                            <td class="p-3">
                                <!-- Solo el Jefe de Área puede editar y eliminar usuarios -->
                                <c:if test="${sessionScope.usuario.colaborador != null && sessionScope.usuario.colaborador.cargo == 'Jefe de Área'}">
                                    <a href="Usuario?accion=editar&id=${usuario.idUsuario}" class="text-blue-500 hover:underline">Editar</a> |
                                    <a href="confirmar_eliminar_usuario.jsp?id=${usuario.idUsuario}" class="text-red-500 hover:underline">Eliminar</a>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>

        <!-- Mostrar mensaje si no hay usuarios -->
        <c:if test="${empty usuarios}">
            <div class="text-center text-gray-500 mt-6">
                <p>No hay usuarios disponibles.</p>
            </div>
        </c:if>
    </div>
</body>
</html>
