<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> <!-- Importamos JSTL fmt para formatear fechas -->
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro de Logs de Auditoría</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css">
    <style>
        thead th {
            position: sticky;
            top: 0;
            background: #1E40AF; /* Color similar al header */
        }
        .accion-critica {
            background-color: #ffe4e6; /* Color de fondo para acciones críticas */
        }
    </style>
</head>
<body class="bg-gray-100">

    <!-- Validación de sesión -->
    <c:if test="${sessionScope.usuario == null}">
        <c:redirect url="login.jsp"/>
    </c:if>

    <!-- Header -->
    <header class="bg-blue-600 p-4 text-white flex justify-between items-center">
        <h1 class="text-2xl font-bold">Registro de Logs de Auditoría</h1>
        <nav>
            <a href="index.jsp" class="hover:text-gray-300">Inicio</a> |
            <a href="cerrar_sesion.jsp" class="hover:text-gray-300">Cerrar Sesión</a>
        </nav>
    </header>

    <!-- Contenedor principal -->
    <div class="container mx-auto mt-6 bg-white p-6 shadow-lg rounded-lg">
        <h1 class="text-2xl font-bold mb-6 text-center">Lista de Logs de Auditoría</h1>

        <!-- Formulario de búsqueda y filtro -->
        <form action="LogAuditoriaServlet?accion=listar" method="get" class="mb-6 flex justify-between items-center">
            <input type="text" name="filtroUsuario" placeholder="Buscar por ID de usuario" value="<c:out value='${param.filtroUsuario}'/>" class="w-1/4 p-2 border border-gray-300 rounded-md"/>
            <input type="date" name="fechaInicio" placeholder="Fecha inicio" value="<c:out value='${param.fechaInicio}'/>" class="w-1/4 p-2 border border-gray-300 rounded-md"/>
            <input type="date" name="fechaFin" placeholder="Fecha fin" value="<c:out value='${param.fechaFin}'/>" class="w-1/4 p-2 border border-gray-300 rounded-md"/>
            <button type="submit" class="bg-blue-500 text-white p-2 rounded-md ml-2 hover:bg-blue-600">Buscar</button>
        </form>

        <!-- Verificar si hay logs disponibles -->
        <c:if test="${not empty logsAuditoria}">
            <!-- Tabla de Logs -->
            <table class="table-auto w-full border-collapse bg-white shadow-md rounded-lg">
                <thead>
                    <tr class="bg-blue-600 text-white">
                        <th class="p-3 text-left">ID Log</th>
                        <th class="p-3 text-left">ID Usuario</th>
                        <th class="p-3 text-left">Nombre Usuario</th>
                        <th class="p-3 text-left">Acción</th>
                        <th class="p-3 text-left">Fecha</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="log" items="${logsAuditoria}">
                        <tr class="bg-gray-100 border-b border-gray-200 <c:if test='${log.accion == "Eliminación de usuario" || log.accion == "Cambio de rol"}'>accion-critica</c:if>">
                            <td class="p-3"><c:out value="${log.idLog}"/></td>
                            <td class="p-3"><c:out value="${log.idUsuario}"/></td>
                            <td class="p-3"><c:out value="${log.nombreUsuario}"/></td>
                            <td class="p-3"><c:out value="${log.accion}"/></td>
                            <td class="p-3">
                                <fmt:formatDate value="${log.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
        
        <!-- Mostrar mensaje si no hay logs -->
        <c:if test="${empty logsAuditoria}">
            <div class="text-center text-gray-500 mt-6">
                <p>No hay registros de auditoría disponibles.</p>
            </div>
        </c:if>
    </div>
</body>
</html>
