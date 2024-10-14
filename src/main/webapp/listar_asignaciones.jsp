<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> <!-- Importamos JSTL fmt para formatear fechas -->
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Lista de Asignaciones</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css">
        <style>
            thead th {
                position: sticky;
                top: 0;
                background: #1E40AF; /* Color similar al header */
            }
            tbody tr:hover {
                background-color: #f1f5f9; /* Color de fondo para la fila al pasar el cursor */
            }
        </style>
    </head>
    <body class="bg-gray-100">

        <!-- Validación de sesión de usuario -->
        <c:if test="${sessionScope.usuario == null}">
            <c:redirect url="login.jsp"/>
        </c:if>

        <!-- Restricción de acceso para Coordinadores y Jefe de Área -->
        <c:if test="${sessionScope.usuario.colaborador.cargo != 'Coordinador' && sessionScope.usuario.colaborador.cargo != 'Jefe de Área'}">
            <c:redirect url="index.jsp" />
        </c:if>

        <header class="bg-blue-600 p-4 text-white flex justify-between items-center">
            <h1 class="text-2xl font-bold">Gestión de Asignaciones</h1>
            <nav>
                <a href="index.jsp" class="hover:text-gray-300">Inicio</a> |
                <a href="cerrar_sesion.jsp" class="hover:text-gray-300">Cerrar Sesión</a>
            </nav>
        </header>

        <div class="container mx-auto mt-6 bg-white p-6 shadow-lg rounded-lg">
            <h1 class="text-2xl font-bold mb-6 text-center">Lista de Asignaciones</h1>

            <!-- Formatear la fecha de filtro para el input -->
            <fmt:formatDate value="${param.filtroFecha}" pattern="yyyy-MM-dd" var="formattedDate" />

            <!-- Formulario de búsqueda y filtro -->
            <form action="AsignacionServlet?accion=listar" method="get" class="mb-6 flex justify-between items-center">
                <input type="text" name="filtroSolicitud" placeholder="Buscar por ID de solicitud" value="<c:out value='${param.filtroSolicitud}'/>" class="w-1/4 p-2 border border-gray-300 rounded-md" aria-label="Filtro por ID de solicitud"/>
                <input type="text" name="filtroColaborador" placeholder="Buscar por ID de colaborador" value="<c:out value='${param.filtroColaborador}'/>" class="w-1/4 p-2 border border-gray-300 rounded-md" aria-label="Filtro por ID de colaborador"/>
                
                <c:choose>
                    <c:when test="${not empty formattedDate}">
                        <input type="date" name="filtroFecha" value="${formattedDate}" class="w-1/4 p-2 border border-gray-300 rounded-md" aria-label="Filtro por fecha"/>
                    </c:when>
                    <c:otherwise>
                        <input type="date" name="filtroFecha" class="w-1/4 p-2 border border-gray-300 rounded-md" aria-label="Filtro por fecha"/>
                    </c:otherwise>
                </c:choose>
                
                <button type="submit" class="bg-blue-500 text-white p-2 rounded-md ml-2 hover:bg-blue-600" aria-label="Buscar asignaciones">Buscar</button>
            </form>

            <!-- Verificar si hay asignaciones disponibles -->
            <c:if test="${not empty asignaciones}">
                <!-- Tabla de Asignaciones -->
                <table class="table-auto w-full border-collapse bg-white shadow-md rounded-lg">
                    <thead>
                        <tr class="bg-blue-600 text-white">
                            <th class="p-3 text-left">ID Asignación</th>
                            <th class="p-3 text-left">ID Solicitud</th>
                            <th class="p-3 text-left">Colaborador</th>
                            <th class="p-3 text-left">Fecha de Asignación</th>
                            <th class="p-3 text-left">Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="asignacion" items="${asignaciones}">
                            <tr class="bg-gray-100 border-b border-gray-200">
                                <td class="p-3"><c:out value="${asignacion.idAsignacion}"/></td>
                                <td class="p-3"><c:out value="${asignacion.idSolicitud}"/></td>
                                <td class="p-3"><c:out value="${asignacion.colaborador.nombre}"/></td>
                                <td class="p-3">
                                    <fmt:formatDate value="${asignacion.fechaAsignacion}" pattern="dd/MM/yyyy HH:mm"/>
                                </td>
                                <td class="p-3">
                                    <!-- Solo Coordinador y Jefe de Área pueden eliminar -->
                                    <c:if test="${sessionScope.usuario.colaborador.cargo == 'Coordinador' || sessionScope.usuario.colaborador.cargo == 'Jefe de Área'}">
                                        <a href="AsignacionServlet?accion=eliminar&id=${asignacion.idAsignacion}" class="text-red-500 hover:underline" onclick="return confirm('¿Estás seguro de que deseas eliminar esta asignación?')" aria-label="Eliminar asignación ${asignacion.idAsignacion}">Eliminar</a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>

            <!-- Mostrar mensaje si no hay asignaciones -->
            <c:if test="${empty asignaciones}">
                <div class="text-center text-gray-500 mt-6">
                    <p>No hay asignaciones disponibles.</p>
                </div>
            </c:if>
        </div>
    </body>
</html>
