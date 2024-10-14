<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> <!-- Importamos JSTL fmt para formatear fechas -->
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lista de Solicitudes</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css">
</head>
<body class="bg-gray-100">

    <!-- Validación de sesión de usuario -->
    <c:if test="${sessionScope.usuario == null}">
        <c:redirect url="login.jsp"/>
    </c:if>

    <header class="bg-blue-600 p-4 text-white flex justify-between items-center">
        <h1 class="text-2xl font-bold">Gestión de Solicitudes</h1>
        <nav>
            <a href="index.jsp" class="hover:text-gray-300">Inicio</a> |
            <a href="cerrar_sesion.jsp" class="hover:text-gray-300">Cerrar Sesión</a>
        </nav>
    </header>

    <div class="container mx-auto mt-6 bg-white p-6 shadow-lg rounded-lg">
        <h1 class="text-2xl font-bold mb-6 text-center">Lista de Solicitudes</h1>

        <!-- Formulario de búsqueda y filtro -->
        <form action="SolicitudServlet?accion=listar" method="get" class="mb-6 flex flex-wrap justify-between items-center space-y-4 md:space-y-0">
            <input type="text" name="filtroCliente" placeholder="Buscar por ID de cliente" value="<c:out value='${param.filtroCliente}'/>" class="w-full md:w-1/4 p-2 border border-gray-300 rounded-md" aria-label="Buscar por ID de cliente">
            <input type="text" name="filtroTipo" placeholder="Buscar por tipo de solicitud" value="<c:out value='${param.filtroTipo}'/>" class="w-full md:w-1/4 p-2 border border-gray-300 rounded-md" aria-label="Buscar por tipo de solicitud">
            <select name="filtroEstado" class="w-full md:w-1/4 p-2 border border-gray-300 rounded-md" aria-label="Filtrar por estado de solicitud">
                <option value="">Todos los estados</option>
                <option value="pendiente" <c:if test="${param.filtroEstado == 'pendiente'}">selected</c:if>>Pendiente</option>
                <option value="completado" <c:if test="${param.filtroEstado == 'completado'}">selected</c:if>>Completado</option>
            </select>
            <button type="submit" class="w-full md:w-auto bg-blue-500 text-white p-2 rounded-md hover:bg-blue-600" aria-label="Buscar solicitudes">Buscar</button>
        </form>

        <!-- Verificar si hay solicitudes disponibles -->
        <c:if test="${not empty solicitudes}">
            <!-- Tabla de Solicitudes -->
            <table class="table-auto w-full border-collapse bg-white shadow-md rounded-lg">
                <thead>
                    <tr class="bg-blue-600 text-white">
                        <th class="p-3 text-left">ID Solicitud</th>
                        <th class="p-3 text-left">ID Cliente</th>
                        <th class="p-3 text-left">Tipo de Solicitud</th>
                        <th class="p-3 text-left">Descripción</th>
                        <th class="p-3 text-left">Estado</th>
                        <th class="p-3 text-left">Fecha de Creación</th>
                        <th class="p-3 text-left">Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="solicitud" items="${solicitudes}">
                        <tr class="bg-gray-100 border-b border-gray-200 hover:bg-gray-200">
                            <td class="p-3"><c:out value="${solicitud.idSolicitud}"/></td>
                            <td class="p-3"><c:out value="${solicitud.idCliente}"/></td>
                            <td class="p-3"><c:out value="${solicitud.tipoSolicitud}"/></td>
                            <td class="p-3"><c:out value="${solicitud.descripcion}"/></td>
                            <td class="p-3">
                                <c:choose>
                                    <c:when test="${solicitud.estado == 'pendiente'}">
                                        <span class="text-yellow-500 font-semibold">Pendiente</span>
                                    </c:when>
                                    <c:when test="${solicitud.estado == 'completado'}">
                                        <span class="text-green-500 font-semibold">Completado</span>
                                    </c:when>
                                    <c:otherwise>
                                        <c:out value="${solicitud.estado}"/>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="p-3">
                                <fmt:formatDate value="${solicitud.fechaCreacion}" pattern="dd/MM/yyyy HH:mm"/>
                            </td>
                            <td class="p-3">
                                <!-- Solo Jefe de Área y Coordinador pueden editar y eliminar solicitudes -->
                                <c:if test="${sessionScope.usuario.colaborador.cargo == 'Jefe de Área' || sessionScope.usuario.colaborador.cargo == 'Coordinador'}">
                                    <a href="SolicitudServlet?accion=editar&id=${solicitud.idSolicitud}" class="text-blue-500 hover:underline">Editar</a> |
                                    <a href="SolicitudServlet?accion=eliminar&id=${solicitud.idSolicitud}" class="text-red-500 hover:underline" onclick="return confirm('¿Estás seguro de que deseas eliminar esta solicitud?')">Eliminar</a>
                                </c:if>
                                <!-- Los demás cargos solo pueden ver las solicitudes -->
                                <c:if test="${sessionScope.usuario.colaborador.cargo == 'Programador Junior' || sessionScope.usuario.colaborador.cargo == 'Analista Senior'}">
                                    <span class="text-gray-500">Ver</span>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>

        <!-- Mostrar mensaje si no hay solicitudes -->
        <c:if test="${empty solicitudes}">
            <div class="text-center text-gray-500 mt-6">
                <p>No hay solicitudes disponibles.</p>
            </div>
        </c:if>
    </div>
</body>
</html>
