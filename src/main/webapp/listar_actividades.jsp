<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lista de Actividades</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css">
    <script>
        // Validación sencilla de formulario (JavaScript)
        function validarFormulario() {
            const descripcion = document.getElementById('descripcionActividad').value.trim();
            const tiempoEmpleado = document.getElementById('tiempoEmpleado').value;

            if (descripcion === '' || isNaN(tiempoEmpleado) || tiempoEmpleado <= 0) {
                alert('Por favor, complete todos los campos correctamente.');
                return false;
            }
            return true;
        }
    </script>
</head>
<body class="bg-gray-100">

    <!-- Validación de sesión -->
    <c:if test="${sessionScope.usuario == null}">
        <c:redirect url="login.jsp"/>
    </c:if>

    <!-- Header -->
    <header class="bg-blue-600 p-4 text-white flex justify-between items-center">
        <h1 class="text-2xl font-bold">Gestión de Actividades</h1>
        <nav>
            <a href="index.jsp" class="hover:text-gray-300">Inicio</a> |
            <a href="perfil_usuario.jsp" class="hover:text-gray-300">Perfil de Usuario</a> |
            <a href="cerrar_sesion.jsp" class="hover:text-gray-300">Cerrar Sesión</a>
        </nav>
    </header>

    <!-- Mensaje de éxito -->
    <c:if test="${not empty param.mensaje}">
        <div class="bg-green-200 border-l-4 border-green-500 text-green-700 p-4 mb-6" role="alert">
            <p><c:out value="${param.mensaje}"/></p>
        </div>
    </c:if>

    <!-- Contenedor principal -->
    <div class="container mx-auto mt-6 bg-white p-6 shadow-lg rounded-lg">
        <h1 class="text-2xl font-bold mb-6 text-center">Lista de Actividades</h1>

        <!-- Formulario de búsqueda -->
        <form action="ActividadServlet?accion=listar" method="get" class="mb-6 flex justify-between items-center">
            <input type="text" name="filtroNombre" placeholder="Buscar por nombre de actividad" value="<c:out value='${param.filtroNombre}'/>" class="w-1/3 p-2 border border-gray-300 rounded-md">
            <input type="date" name="filtroFecha" value="<c:out value='${param.filtroFecha}'/>" class="w-1/3 p-2 border border-gray-300 rounded-md">
            <button type="submit" class="bg-blue-500 text-white p-2 rounded-md ml-2 hover:bg-blue-600">Buscar</button>
        </form>

        <!-- Tabla de Actividades -->
        <c:if test="${not empty actividades}">
            <table class="table-auto w-full border-collapse bg-white shadow-md rounded-lg">
                <thead>
                    <tr class="bg-blue-600 text-white">
                        <th class="p-3 text-left">ID Actividad</th>
                        <th class="p-3 text-left">Nombre</th>
                        <th class="p-3 text-left">Descripción</th>
                        <th class="p-3 text-left">Fecha</th>
                        <th class="p-3 text-left">Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="actividad" items="${actividades}">
                        <tr class="bg-gray-100 border-b border-gray-200">
                            <td class="p-3"><c:out value="${actividad.id}"/></td>
                            <td class="p-3"><c:out value="${actividad.nombre}"/></td>
                            <td class="p-3"><c:out value="${actividad.descripcion}"/></td>
                            <td class="p-3">
                                <fmt:formatDate value="${actividad.fecha}" pattern="dd/MM/yyyy HH:mm"/>
                            </td>
                            <td class="p-3">
                                <c:choose>
                                    <c:when test="${sessionScope.usuario.colaborador.cargo == 'Programador Junior'}">
                                        <span class="text-gray-600">Ver</span>
                                    </c:when>
                                    <c:when test="${sessionScope.usuario.colaborador.cargo == 'Coordinador'}">
                                        <a href="ActividadServlet?accion=editar&id=${actividad.id}" class="text-blue-500 hover:underline">Editar</a>
                                    </c:when>
                                    <c:when test="${sessionScope.usuario.colaborador.cargo == 'Jefe de Área'}">
                                        <a href="ActividadServlet?accion=editar&id=${actividad.id}" class="text-blue-500 hover:underline">Editar</a> |
                                        <a href="ActividadServlet?accion=eliminar&id=${actividad.id}" class="text-red-500 hover:underline" onclick="return confirm('¿Estás seguro de que deseas eliminar esta actividad?')">Eliminar</a>
                                    </c:when>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>

        <!-- Mostrar mensaje si no hay actividades -->
        <c:if test="${empty actividades}">
            <div class="text-center text-gray-500 mt-6">
                <p>No hay actividades disponibles.</p>
            </div>
        </c:if>
    </div>
</body>
</html>
