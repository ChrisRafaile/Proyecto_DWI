<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> <!-- JSTL para formatear fecha -->
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Asignación</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css">
    <script>
        function validarFormulario() {
            const fechaAsignacion = document.querySelector('[name="fechaAsignacion"]').value.trim();
            if (fechaAsignacion === "") {
                alert("Por favor, selecciona una fecha de asignación válida.");
                return false;
            }
            return true;
        }
    </script>
</head>
<body class="bg-gray-100">

    <!-- Validación de sesión de usuario -->
    <c:if test="${sessionScope.usuario == null}">
        <c:redirect url="login.jsp"/>
    </c:if>

    <!-- Header -->
    <header class="bg-blue-600 p-4 text-white">
        <h1 class="text-2xl font-bold">Editar Asignación</h1>
    </header>

    <!-- Contenido principal -->
    <div class="container mx-auto mt-6 bg-white p-6 shadow-lg rounded-lg">
        <h1 class="text-2xl font-bold mb-6 text-center">Editar Asignación</h1>
        <form action="AsignacionServlet" method="post" class="space-y-6" onsubmit="return validarFormulario();">
            <!-- Campo oculto para el ID de la asignación -->
            <input type="hidden" name="idAsignacion" value="${asignacion.idAsignacion}"/>

            <!-- Selección de la solicitud -->
            <div>
                <label class="block text-sm font-medium text-gray-700">ID Solicitud:</label>
                <select name="idSolicitud" required class="mt-1 block w-full border-gray-300 rounded-md shadow-sm">
                    <c:forEach var="solicitud" items="${solicitudes}">
                        <option value="${solicitud.idSolicitud}" <c:if test="${asignacion.idSolicitud == solicitud.idSolicitud}">selected</c:if>>
                            ${solicitud.idSolicitud} - ${solicitud.tipoSolicitud}
                        </option>
                    </c:forEach>
                </select>
            </div>

            <!-- Selección del colaborador -->
            <div>
                <label class="block text-sm font-medium text-gray-700">ID Colaborador:</label>
                <select name="idColaborador" required class="mt-1 block w-full border-gray-300 rounded-md shadow-sm">
                    <c:forEach var="colaborador" items="${colaboradores}">
                        <option value="${colaborador.idColaborador}" <c:if test="${asignacion.idColaborador == colaborador.idColaborador}">selected</c:if>>
                            ${colaborador.idColaborador} - ${colaborador.cargo}
                        </option>
                    </c:forEach>
                </select>
            </div>

            <!-- Fecha de asignación -->
            <div>
                <label class="block text-sm font-medium text-gray-700">Fecha de Asignación:</label>
                <input type="datetime-local" name="fechaAsignacion" 
                       value="<fmt:formatDate value='${asignacion.fechaAsignacion}' pattern='yyyy-MM-dd\'T\'HH:mm'/>"
                       required class="mt-1 block w-full border-gray-300 rounded-md shadow-sm">
            </div>

            <!-- Botón para guardar los cambios -->
            <div class="mt-6">
                <button type="submit" name="accion" value="actualizar" class="w-full bg-blue-500 text-white py-2 px-4 rounded-md hover:bg-blue-600 transition duration-300 ease-in-out">
                    Guardar Cambios
                </button>
            </div>
        </form>
    </div>

</body>
</html>
