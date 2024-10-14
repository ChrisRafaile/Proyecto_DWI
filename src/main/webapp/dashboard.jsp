<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Jefe de Área</title>
    <!-- Enlaces a CSS y bibliotecas externas -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/4.0.1/chart.min.js"></script>
    <style>
        body {
            background-color: #f4f6f9;
        }
        .dashboard-header {
            background-color: #343a40;
            color: #fff;
            padding: 20px;
            text-align: center;
        }
        .dashboard-content {
            padding: 20px;
        }
        .card {
            margin-bottom: 20px;
        }
    </style>
</head>
<body>

    <!-- Validación de sesión y permisos -->
    <c:if test="${sessionScope.usuario == null}">
        <c:redirect url="login.jsp?error=sin-sesion"/>
    </c:if>
    <c:if test="${sessionScope.usuario.colaborador.cargo != 'Jefe de Área'}">
        <c:redirect url="index.jsp?error=sin-permiso"/>
    </c:if>

    <!-- Encabezado del Dashboard -->
    <div class="dashboard-header">
        <h1>Panel de Control - Jefe de Área</h1>
        <p>Bienvenido, aquí puedes ver un resumen de las solicitudes, asignaciones y actividades.</p>
    </div>

    <!-- Contenido del Dashboard -->
    <div class="container dashboard-content">
        <div class="row">
            <!-- Total de Solicitudes -->
            <div class="col-lg-4">
                <div class="card text-white bg-primary">
                    <div class="card-body">
                        <h5 class="card-title">Total de Solicitudes</h5>
                        <h2>${dashboardData.totalSolicitudes}</h2>
                    </div>
                </div>
            </div>

            <!-- Solicitudes Pendientes -->
            <div class="col-lg-4">
                <div class="card text-white bg-warning">
                    <div class="card-body">
                        <h5 class="card-title">Solicitudes Pendientes</h5>
                        <h2>${dashboardData.solicitudesPendientes}</h2>
                    </div>
                </div>
            </div>

            <!-- Solicitudes Completadas -->
            <div class="col-lg-4">
                <div class="card text-white bg-success">
                    <div class="card-body">
                        <h5 class="card-title">Solicitudes Completadas</h5>
                        <h2>${dashboardData.solicitudesCompletadas}</h2>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <!-- Total de Asignaciones -->
            <div class="col-lg-6">
                <div class="card text-white bg-info">
                    <div class="card-body">
                        <h5 class="card-title">Total de Asignaciones</h5>
                        <h2>${dashboardData.totalAsignaciones}</h2>
                    </div>
                </div>
            </div>

            <!-- Total de Actividades -->
            <div class="col-lg-6">
                <div class="card text-white bg-danger">
                    <div class="card-body">
                        <h5 class="card-title">Total de Actividades</h5>
                        <h2>${dashboardData.totalActividades}</h2>
                    </div>
                </div>
            </div>
        </div>

        <!-- Tiempo Promedio de Respuesta -->
        <div class="card mt-4">
            <div class="card-body">
                <h5 class="card-title">Tiempo Promedio de Respuesta (horas)</h5>
                <h2><c:out value="${dashboardData.tiempoPromedioRespuesta != null ? dashboardData.tiempoPromedioRespuesta : 'No disponible'}"/></h2>
            </div>
        </div>

        <!-- Listado de Solicitudes con opción para asignar colaborador -->
        <div class="row mt-4">
            <div class="col-lg-12">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Listado de Solicitudes</h5>
                        <table class="table table-hover">
                            <thead>
                                <tr>
                                    <th>ID Solicitud</th>
                                    <th>Nombre Solicitud</th>
                                    <th>Acciones</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="solicitud" items="${solicitudes}">
                                    <tr>
                                        <td>${solicitud.idSolicitud}</td>
                                        <td>${solicitud.nombre}</td>
                                        <td>
                                            <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#modalAsignacion" onclick="document.getElementById('idSolicitud').value='${solicitud.idSolicitud}'">Asignar Colaborador</button>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal para asignar colaborador -->
        <div class="modal fade" id="modalAsignacion" tabindex="-1" aria-labelledby="modalAsignacionLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="modalAsignacionLabel">Asignar Colaborador</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form action="AsignacionServlet" method="post">
                            <input type="hidden" name="idSolicitud" id="idSolicitud"/>
                            <div class="mb-3">
                                <label for="idColaborador" class="form-label">Seleccionar Colaborador:</label>
                                <select name="idColaborador" id="idColaborador" required class="form-select">
                                    <c:forEach var="colaborador" items="${colaboradores}">
                                        <option value="${colaborador.idColaborador}">${colaborador.nombre}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <button type="submit" name="accion" value="asignar" class="btn btn-primary w-100">Asignar</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Gráfico de Barras (Resumen de Solicitudes) -->
        <div class="card mt-4">
            <div class="card-body">
                <h5 class="card-title">Resumen de Solicitudes</h5>
                <canvas id="chartSolicitudes" width="400" height="200" aria-label="Gráfico de resumen de solicitudes"></canvas>
            </div>
        </div>

        <!-- Gráfico de Asignaciones -->
        <div class="card mt-4">
            <div class="card-body">
                <h5 class="card-title">Resumen de Asignaciones</h5>
                <canvas id="chartAsignaciones" width="400" height="200" aria-label="Gráfico de resumen de asignaciones"></canvas>
            </div>
        </div>

        <!-- Gráfico de Actividades por Colaborador -->
        <div class="card mt-4">
            <div class="card-body">
                <h5 class="card-title">Actividades por Colaborador</h5>
                <canvas id="chartActividadesPorColaborador" width="400" height="200"></canvas>
            </div>
        </div>

    </div>

    <!-- Scripts para Bootstrap y Chart.js -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
    <script>
        // Gráficos con Chart.js
        const ctxSolicitudes = document.getElementById('chartSolicitudes').getContext('2d');
        const chartSolicitudes = new Chart(ctxSolicitudes, {
            type: 'bar',
            data: {
                labels: ['Total', 'Pendientes', 'Completadas'],
                datasets: [{
                    label: 'Solicitudes',
                    data: [${dashboardData.totalSolicitudes}, ${dashboardData.solicitudesPendientes}, ${dashboardData.solicitudesCompletadas}],
                    backgroundColor: ['#007bff', '#ffc107', '#28a745']
                }]
            }
        });

        const ctxAsignaciones = document.getElementById('chartAsignaciones').getContext('2d');
        const chartAsignaciones = new Chart(ctxAsignaciones, {
            type: 'doughnut',
            data: {
                labels: ['Total de Asignaciones', 'Total de Actividades'],
                datasets: [{
                    label: 'Asignaciones y Actividades',
                    data: [${dashboardData.totalAsignaciones}, ${dashboardData.totalActividades}],
                    backgroundColor: ['#17a2b8', '#dc3545']
                }]
            }
        });

        const ctxActividadesPorColaborador = document.getElementById('chartActividadesPorColaborador').getContext('2d');
        const nombresColaboradores = [
            <c:forEach var="entry" items="${actividadesPorColaborador}">
                "${entry.key}",
            </c:forEach>
        ];
        const actividadesPorColaborador = [
            <c:forEach var="entry" items="${actividadesPorColaborador}">
                ${entry.value},
            </c:forEach>
        ];
        const chartActividadesPorColaborador = new Chart(ctxActividadesPorColaborador, {
            type: 'bar',
            data: {
                labels: nombresColaboradores,
                datasets: [{
                    label: 'Actividades',
                    data: actividadesPorColaborador,
                    backgroundColor: '#17a2b8'
                }]
            }
        });
    </script>
</body>
</html>
