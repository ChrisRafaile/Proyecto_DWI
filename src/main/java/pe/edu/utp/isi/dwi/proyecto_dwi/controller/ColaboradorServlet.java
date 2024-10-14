package pe.edu.utp.isi.dwi.proyecto_dwi.controller;

import pe.edu.utp.isi.dwi.proyecto_dwi.service.ColaboradorFacade;
import pe.edu.utp.isi.dwi.proyecto_dwi.dto.ColaboradorDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "ColaboradorServlet", urlPatterns = {"/Colaborador"})
public class ColaboradorServlet extends HttpServlet {

    private ColaboradorFacade colaboradorFacade;
    private static final Logger logger = Logger.getLogger(ColaboradorServlet.class.getName());

    @Override
    public void init() throws ServletException {
        try {
            colaboradorFacade = new ColaboradorFacade();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al inicializar ColaboradorFacade", e);
            throw new ServletException("No se pudo inicializar ColaboradorFacade", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        procesarAccion(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        procesarAccion(request, response);
    }

    private void procesarAccion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");

        switch (accion) {
            case "guardar" -> registrarColaborador(request, response);
            case "editar" -> editarColaborador(request, response);
            case "eliminar" -> eliminarColaborador(request, response);
            case "listar" -> listarColaboradores(request, response);
            case "mostrarEditar" -> mostrarFormularioEditar(request, response);
            default -> redirigirConMensajeError(request, response, "Acción no válida.");
        }
    }

    private void registrarColaborador(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String idUsuarioStr = request.getParameter("idUsuario");
            if (idUsuarioStr == null || idUsuarioStr.isEmpty()) {
                manejarError(new IllegalArgumentException("ID de usuario no proporcionado"), "Error al registrar el colaborador", request, response);
                return;
            }

            int idUsuario = Integer.parseInt(idUsuarioStr);
            String cargo = request.getParameter("cargo").trim();

            ColaboradorDTO colaboradorDTO = new ColaboradorDTO();
            colaboradorDTO.setIdUsuario(idUsuario);
            colaboradorDTO.setCargo(cargo);
            colaboradorDTO.setFechaIngreso(LocalDate.now());

            colaboradorFacade.registrarColaborador(colaboradorDTO);
            response.sendRedirect("colaborador_exitoso.jsp");
        } catch (NumberFormatException e) {
            manejarError(e, "ID de usuario no válido.", request, response);
        } catch (IOException | ServletException e) {
            manejarError(e, "Error al registrar el colaborador.", request, response);
        }
    }

    private void listarColaboradores(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<ColaboradorDTO> colaboradores = colaboradorFacade.listarColaboradores();
            request.setAttribute("colaboradores", colaboradores);
            request.getRequestDispatcher("listar_colaboradores.jsp").forward(request, response);
        } catch (IOException | ServletException e) {
            manejarError(e, "Error al listar los colaboradores.", request, response);
        }
    }

    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String idColaboradorStr = request.getParameter("idColaborador");
            if (idColaboradorStr == null || idColaboradorStr.isEmpty()) {
                manejarError(new IllegalArgumentException("ID de colaborador no proporcionado"), "Error al obtener el colaborador", request, response);
                return;
            }

            int idColaborador = Integer.parseInt(idColaboradorStr);
            ColaboradorDTO colaboradorDTO = colaboradorFacade.obtenerColaboradorPorId(idColaborador);
            request.setAttribute("colaborador", colaboradorDTO);
            request.getRequestDispatcher("editar_colaborador.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            manejarError(e, "ID de colaborador no válido.", request, response);
        } catch (IOException | ServletException e) {
            manejarError(e, "Error al obtener el colaborador.", request, response);
        }
    }

    private void editarColaborador(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String idColaboradorStr = request.getParameter("idColaborador");
            if (idColaboradorStr == null || idColaboradorStr.isEmpty()) {
                manejarError(new IllegalArgumentException("ID de colaborador no proporcionado"), "Error al editar el colaborador", request, response);
                return;
            }

            int idColaborador = Integer.parseInt(idColaboradorStr);
            String cargo = request.getParameter("cargo").trim();

            ColaboradorDTO colaboradorDTO = new ColaboradorDTO();
            colaboradorDTO.setIdColaborador(idColaborador);
            colaboradorDTO.setCargo(cargo);

            colaboradorFacade.actualizarColaborador(colaboradorDTO);
            response.sendRedirect("Colaborador?accion=listar");
        } catch (NumberFormatException e) {
            manejarError(e, "ID de colaborador no válido.", request, response);
        } catch (IOException | ServletException e) {
            manejarError(e, "Error al editar el colaborador.", request, response);
        }
    }

    private void eliminarColaborador(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String idColaboradorStr = request.getParameter("idColaborador");
            if (idColaboradorStr == null || idColaboradorStr.isEmpty()) {
                manejarError(new IllegalArgumentException("ID de colaborador no proporcionado"), "Error al eliminar el colaborador", request, response);
                return;
            }

            int idColaborador = Integer.parseInt(idColaboradorStr);

            colaboradorFacade.eliminarColaborador(idColaborador);
            response.sendRedirect("Colaborador?accion=listar");
        } catch (NumberFormatException e) {
            manejarError(e, "ID de colaborador no válido.", request, response);
        } catch (IOException | ServletException e) {
            manejarError(e, "Error al eliminar el colaborador.", request, response);
        }
    }

    private void manejarError(Exception e, String mensaje, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.log(Level.SEVERE, mensaje, e);
        request.setAttribute("mensajeError", mensaje);
        request.getRequestDispatcher("error.jsp").forward(request, response);
    }

    private void redirigirConMensajeError(HttpServletRequest request, HttpServletResponse response, String mensaje) throws ServletException, IOException {
        request.setAttribute("mensajeError", mensaje);
        request.getRequestDispatcher("error.jsp").forward(request, response);
    }
}
