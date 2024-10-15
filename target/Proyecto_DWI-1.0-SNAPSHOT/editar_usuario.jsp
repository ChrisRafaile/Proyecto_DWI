<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Usuario</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css">
    <script>
        function validarFormulario() {
            const telefono = document.getElementById("telefono").value.trim();
            const dni = document.getElementById("dni").value.trim();
            const regexTelefono = /^[0-9]{9}$/; // Asegura que el número tenga 9 dígitos (formato peruano)

            if (!regexTelefono.test(telefono)) {
                alert("Por favor, ingrese un número de teléfono válido de 9 dígitos.");
                return false;
            }

            if (dni.length !== 8) {
                alert("El DNI debe tener exactamente 8 dígitos.");
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
        <h1 class="text-2xl font-bold">Editar Usuario</h1>
    </header>

    <div class="container mx-auto mt-6 bg-white p-6 shadow-lg rounded-lg">
        <h2 class="text-2xl font-bold mb-6 text-center">Formulario de Edición</h2>
        <form action="Usuario?accion=editar" method="post" class="space-y-4" enctype="multipart/form-data" onsubmit="return validarFormulario();">
            <!-- ID del usuario oculto -->
            <input type="hidden" name="txtId" value="${usuario.idUsuario}">

            <!-- Campo de nombre de usuario -->
            <div>
                <label for="nombre_usuario" class="block text-gray-700">Nombre:</label>
                <input type="text" name="nombre_usuario" id="nombre_usuario" value="${usuario.nombreUsuario}" required class="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:border-blue-500 focus:ring focus:ring-blue-500 focus:ring-opacity-50">
            </div>

            <!-- Campo de apellidos -->
            <div>
                <label for="apellidos" class="block text-gray-700">Apellidos:</label>
                <input type="text" name="apellidos" id="apellidos" value="${usuario.apellidos}" required class="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:border-blue-500 focus:ring focus:ring-blue-500 focus:ring-opacity-50">
            </div>

            <!-- Campo de correo -->
            <div>
                <label for="correo" class="block text-gray-700">Correo:</label>
                <input type="email" name="correo" id="correo" value="${usuario.correo}" required class="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:border-blue-500 focus:ring focus:ring-blue-500 focus:ring-opacity-50">
            </div>

            <!-- Campo de nueva contraseña (opcional) -->
            <div>
                <label for="contraseña" class="block text-gray-700">Nueva Contraseña (opcional):</label>
                <input type="password" name="contraseña" id="contraseña" placeholder="Dejar en blanco para no cambiar" class="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:border-blue-500 focus:ring focus:ring-blue-500 focus:ring-opacity-50">
            </div>

            <!-- Campo de número de teléfono -->
            <div>
                <label for="telefono" class="block text-gray-700">Número de Teléfono:</label>
                <input type="text" name="telefono" id="telefono" value="${usuario.telefono}" required class="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:border-blue-500 focus:ring focus:ring-blue-500 focus:ring-opacity-50">
            </div>

            <!-- Campo de dirección -->
            <div>
                <label for="direccion" class="block text-gray-700">Dirección:</label>
                <input type="text" name="direccion" id="direccion" value="${usuario.direccion}" required class="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:border-blue-500 focus:ring focus:ring-blue-500 focus:ring-opacity-50">
            </div>

            <!-- Campo de fecha de nacimiento -->
            <div>
                <label for="fecha_nacimiento" class="block text-gray-700">Fecha de Nacimiento:</label>
                <input type="date" name="fecha_nacimiento" id="fecha_nacimiento" value="${usuario.fechaNacimiento}" class="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:border-blue-500 focus:ring focus:ring-blue-500 focus:ring-opacity-50">
            </div>

            <!-- Campo de foto de perfil -->
            <div>
                <label for="foto_perfil" class="block text-gray-700">Foto de Perfil:</label>
                <input type="file" name="foto_perfil" id="foto_perfil" accept="image/*" class="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:border-blue-500 focus:ring focus:ring-blue-500 focus:ring-opacity-50">
                <div class="mt-2">
                    <c:if test="${not empty usuario.fotoPerfil}">
                        <img src="images/perfil/${usuario.fotoPerfil}" alt="Foto de perfil actual" class="w-24 h-24 object-cover rounded-full">
                    </c:if>
                </div>
            </div>

            <!-- Campo de departamento/área -->
            <div>
                <label for="departamento" class="block text-gray-700">Departamento/Área:</label>
                <input type="text" name="departamento" id="departamento" value="${usuario.departamento}" class="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:border-blue-500 focus:ring focus:ring-blue-500 focus:ring-opacity-50">
            </div>

            <!-- Campo de DNI -->
            <div>
                <label for="dni" class="block text-gray-700">DNI:</label>
                <input type="text" name="dni" id="dni" value="${usuario.dni}" required class="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:border-blue-500 focus:ring focus:ring-blue-500 focus:ring-opacity-50">
            </div>

            <!-- Campo de selección de cargo -->
            <div>
                <label for="cargo" class="block text-gray-700">Cargo:</label>
                <select name="cargo" id="cargo" class="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:border-blue-500 focus:ring focus:ring-blue-500 focus:ring-opacity-50">
                    <option value="Jefe de Área" <c:if test="${usuario.colaborador.cargo == 'Jefe de Área'}">selected</c:if>>Jefe de Área</option>
                    <option value="Analista Senior" <c:if test="${usuario.colaborador.cargo == 'Analista Senior'}">selected</c:if>>Analista Senior</option>
                    <option value="Programador Junior" <c:if test="${usuario.colaborador.cargo == 'Programador Junior'}">selected</c:if>>Programador Junior</option>
                    <option value="Coordinador" <c:if test="${usuario.colaborador.cargo == 'Coordinador'}">selected</c:if>>Coordinador</option>
                </select>
            </div>

            <!-- Botón de enviar -->
            <div class="flex justify-end">
                <button type="submit" class="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 transition duration-300 ease-in-out">Guardar Cambios</button>
            </div>
        </form>
    </div>
</body>
</html>
