<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="pe.edu.utp.isi.dwi.proyecto_dwi.dto.UsuarioDTO" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Perfil de Usuario</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css">
        <script>
            // Previsualización de la foto de perfil
            function previsualizarImagen(event) {
                const output = document.getElementById('preview');
                output.src = URL.createObjectURL(event.target.files[0]);
                output.onload = () => {
                    URL.revokeObjectURL(output.src);
                };
            }

            // Validación del formulario antes del envío
            function validarFormulario() {
                const nombre = document.getElementById("nombre_usuario").value.trim();
                const correo = document.getElementById("correo").value.trim();
                if (nombre === "" || correo === "") {
                    alert("Por favor, complete los campos requeridos.");
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

        <header class="bg-blue-600 p-4 text-white flex justify-between items-center">
            <h1 class="text-2xl font-bold">Perfil de Usuario</h1>
            <nav>
                <a href="index.jsp" class="hover:text-gray-300">Inicio</a> |
                <a href="perfil_usuario.jsp" class="hover:text-gray-300">Mi Perfil</a> |
                <a href="cerrar_sesion.jsp" class="hover:text-gray-300">Cerrar Sesión</a>
            </nav>
        </header>

        <div class="container mx-auto mt-6 bg-white p-6 shadow-lg rounded-lg">
            <h2 class="text-2xl font-bold mb-6 text-center">Información del Perfil</h2>

            <!-- Mostrar mensaje si existe (éxito o error) -->
            <c:if test="${not empty sessionScope.mensaje}">
                <p class="text-green-600 text-center mb-4">${sessionScope.mensaje}</p>
                <c:remove var="mensaje" scope="session"/>
            </c:if>
            <c:if test="${not empty requestScope.mensajeError}">
                <p class="text-red-600 text-center mb-4">${requestScope.mensajeError}</p>
            </c:if>

            <form action="Usuario?accion=actualizarPerfil" method="post" enctype="multipart/form-data" onsubmit="return validarFormulario();">
                <!-- ID del usuario oculto -->
                <input type="hidden" name="txtId" value="${sessionScope.usuario.idUsuario}">

                <!-- Campo de nombre de usuario -->
                <div>
                    <label for="nombre_usuario" class="block text-gray-700">Nombre:</label>
                    <input type="text" name="nombre_usuario" id="nombre_usuario" value="${sessionScope.usuario.nombreUsuario}" required class="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:border-blue-500 focus:ring focus:ring-blue-500 focus:ring-opacity-50">
                </div>

                <!-- Campo de apellidos -->
                <div>
                    <label for="apellidos" class="block text-gray-700">Apellidos:</label>
                    <input type="text" name="apellidos" id="apellidos" value="${sessionScope.usuario.apellidos}" required class="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:border-blue-500 focus:ring focus:ring-blue-500 focus:ring-opacity-50">
                </div>

                <!-- Campo de correo -->
                <div>
                    <label for="correo" class="block text-gray-700">Correo:</label>
                    <input type="email" name="correo" id="correo" value="${sessionScope.usuario.correo}" required class="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:border-blue-500 focus:ring focus:ring-blue-500 focus:ring-opacity-50">
                </div>

                <!-- Campo de dirección -->
                <div>
                    <label for="direccion" class="block text-gray-700">Dirección:</label>
                    <input type="text" name="direccion" id="direccion" value="${sessionScope.usuario.direccion}" class="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:border-blue-500 focus:ring focus:ring-blue-500 focus:ring-opacity-50">
                </div>

                <!-- Campo de teléfono -->
                <div>
                    <label for="telefono" class="block text-gray-700">Teléfono:</label>
                    <input type="text" name="telefono" id="telefono" value="${sessionScope.usuario.telefono}" class="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:border-blue-500 focus:ring focus:ring-blue-500 focus:ring-opacity-50">
                </div>

                <!-- Campo de fecha de nacimiento -->
                <div>
                    <label for="fecha_nacimiento" class="block text-gray-700">Fecha de Nacimiento:</label>
                    <input type="date" name="fecha_nacimiento" id="fecha_nacimiento" value="${sessionScope.usuario.fechaNacimiento}" class="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:border-blue-500 focus:ring focus:ring-blue-500 focus:ring-opacity-50">
                </div>

                <!-- Campo de foto de perfil -->
                <div>
                    <label for="foto_perfil" class="block text-gray-700">Foto de Perfil:</label>
                    <input type="file" name="foto_perfil" id="foto_perfil" accept="image/*" class="mt-1 block w-full border-gray-300 rounded-md shadow-sm" onchange="previsualizarImagen(event)">
                    <div class="mt-2">
                        <c:if test="${not empty sessionScope.usuario.fotoPerfil}">
                            <img src="${pageContext.request.contextPath}/${sessionScope.usuario.fotoPerfil}" alt="Foto de perfil actual" id="preview" class="w-24 h-24 object-cover rounded-full">
                        </c:if>
                        <c:if test="${empty sessionScope.usuario.fotoPerfil}">
                            <img id="preview" class="w-24 h-24 object-cover rounded-full" alt="Previsualización de foto de perfil">
                        </c:if>
                    </div>
                </div>

                <!-- Campo de departamento/area (solo lectura) -->
                <div>
                    <label for="departamento" class="block text-gray-700">Departamento/Area:</label>
                    <input type="text" name="departamento" id="departamento" value="${sessionScope.usuario.departamento}" class="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:border-blue-500 focus:ring focus:ring-blue-500 focus:ring-opacity-50" readonly>
                </div>

                <!-- Campo de DNI (solo lectura) -->
                <div>
                    <label for="dni" class="block text-gray-700">DNI:</label>
                    <input type="text" name="dni" id="dni" value="${sessionScope.usuario.dni}" class="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:border-blue-500 focus:ring focus:ring-blue-500 focus:ring-opacity-50" readonly>
                </div>

                <!-- Botón de guardar cambios -->
                <div class="flex justify-end">
                    <button type="submit" class="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700">Guardar Cambios</button>
                </div>
            </form>
        </div>
    </body>
</html>
