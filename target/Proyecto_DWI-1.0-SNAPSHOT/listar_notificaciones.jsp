<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Lista de Notificaciones</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css">
        <style>
            thead th {
                position: sticky;
                top: 0;
                background: #1E40AF; /* Color similar al header */
            }
            .notificacion-no-leida {
                background-color: #fffbeb; /* Fondo amarillo pálido para diferenciar */
            }
        </style>
    </head>
    <body class="bg-gray-100">
        
        <!-- Validación de sesión de usuario -->
        <c:if test="${sessionScope.usuario == null}">
            <c:redirect url="login.jsp"/>
        </c:if>

        <!-- Header -->
        <header class="bg-blue-600 p-4 text-white flex justify-between items-center">
            <h1 class="text-2xl font-bold">Lista de Notificaciones</h1>
            <nav>
                <a href="index.jsp" class="hover:text-gray-300">Inicio</a> |
                <a href="cerrar_sesion.jsp" class="hover:text-gray-300">Cerrar Sesión</a>
            </nav>
        </header>

        <!-- Contenido Principal -->
        <div class="container mx-auto mt-6 bg-white p-6 shadow-lg rounded-lg">
            <h1 class="text-2xl font-bold mb-6 text-center">Notificaciones</h1>

            <!-- Verificar si hay notificaciones disponibles -->
            <c:if test="${not empty notificaciones}">
                <!-- Tabla de Notificaciones -->
                <table class="table-auto w-full border-collapse bg-white shadow-md rounded-lg">
                    <thead>
                        <tr class="bg-blue-600 text-white">
                            <th class="p-3 text-left">ID Notificación</th>
                            <th class="p-3 text-left">ID Solicitud</th>
                            <th class="p-3 text-left">Tipo</th>
                            <th class="p-3 text-left">Fecha de Envío</th>
                            <th class="p-3 text-left">Estado</th>
                            <th class="p-3 text-left">Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="notificacion" items="${notificaciones}">
                            <tr class="border-b border-gray-200 hover:bg-gray-200 <c:if test='${!notificacion.leida}'>notificacion-no-leida</c:if>">
                                <td class="p-3"><c:out value="${notificacion.idNotificacion}"/></td>
                                <td class="p-3"><c:out value="${notificacion.idSolicitud}"/></td>
                                <td class="p-3"><c:out value="${notificacion.tipo}"/></td>
                                <td class="p-3">
                                    <fmt:formatDate value="${notificacion.fechaEnvio}" pattern="dd/MM/yyyy HH:mm"/>
                                </td>
                                <td class="p-3">
                                    <c:choose>
                                        <c:when test="${notificacion.leida}">
                                            <span class="text-green-500 font-semibold">Leída</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-yellow-500 font-semibold">No Leída</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="p-3">
                                    <c:if test="${!notificacion.leida}">
                                        <a href="Notificacion?accion=marcarLeida&id=${notificacion.idNotificacion}" class="text-blue-500 hover:underline" aria-label="Marcar notificación ${notificacion.idNotificacion} como leída">Marcar como Leída</a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>

            <!-- Mostrar mensaje si no hay notificaciones -->
            <c:if test="${empty notificaciones}">
                <div class="text-center text-gray-500 mt-6">
                    <p>No hay notificaciones disponibles.</p>
                </div>
            </c:if>
        </div>
    </body>
</html>
