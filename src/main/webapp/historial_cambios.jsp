<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="pe.edu.utp.isi.dwi.proyecto_dwi.dto.HistorialCambiosDTO" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Historial de Cambios</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css">
</head>
<body class="bg-gray-100">

    <!-- Validación de sesión -->
    <c:if test="${sessionScope.usuario == null}">
        <c:redirect url="login.jsp"/>
    </c:if>

    <!-- Header -->
    <header class="bg-blue-600 p-4 text-white">
        <h1 class="text-2xl font-bold">Historial de Cambios</h1>
    </header>

    <!-- Contenedor principal -->
    <div class="container mx-auto mt-6 bg-white p-6 shadow-lg rounded-lg">
        <h1 class="text-2xl font-bold mb-6 text-center">Listado de Cambios Realizados</h1>

        <!-- Verificar si hay registros disponibles -->
        <c:if test="${not empty historialCambiosList}">
            <div class="overflow-x-auto">
                <table class="min-w-full bg-white border-collapse rounded-lg">
                    <thead>
                        <tr class="bg-blue-600 text-white">
                            <th class="p-3 text-left">ID Historial</th>
                            <th class="p-3 text-left">ID Usuario</th>
                            <th class="p-3 text-left">Tabla Afectada</th>
                            <th class="p-3 text-left">Campo Afectado</th>
                            <th class="p-3 text-left">Valor Anterior</th>
                            <th class="p-3 text-left">Valor Nuevo</th>
                            <th class="p-3 text-left">Descripción del Cambio</th>
                            <th class="p-3 text-left">Fecha de Cambio</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="cambio" items="${historialCambiosList}">
                            <tr class="bg-gray-100 border-b border-gray-200 hover:bg-gray-200 transition duration-150 ease-in-out">
                                <td class="p-3"><c:out value="${cambio.idHistorial}"/></td>
                                <td class="p-3"><c:out value="${cambio.idUsuario}"/></td>
                                <td class="p-3"><c:out value="${cambio.tablaAfectada}"/></td>
                                <td class="p-3"><c:out value="${cambio.campoAfectado}"/></td>
                                <td class="p-3"><c:out value="${cambio.valorAnterior}"/></td>
                                <td class="p-3"><c:out value="${cambio.valorNuevo}"/></td>
                                <td class="p-3"><c:out value="${cambio.descripcionCambio}"/></td>
                                <td class="p-3">
                                    <fmt:formatDate value="${cambio.fechaCambio}" pattern="dd/MM/yyyy HH:mm"/>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>

        <!-- Mostrar mensaje si no hay cambios -->
        <c:if test="${empty historialCambiosList}">
            <div class="text-center text-gray-500 mt-6">
                <p>No hay registros de cambios disponibles.</p>
            </div>
        </c:if>
    </div>
</body>
</html>
