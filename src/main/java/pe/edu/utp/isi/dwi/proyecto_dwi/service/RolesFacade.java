package pe.edu.utp.isi.dwi.proyecto_dwi.service;

import pe.edu.utp.isi.dwi.proyecto_dwi.dao.RolesDAO;
import pe.edu.utp.isi.dwi.proyecto_dwi.dao.UsuariosRolesDAO;
import pe.edu.utp.isi.dwi.proyecto_dwi.dto.RolDTO;
import pe.edu.utp.isi.dwi.proyecto_dwi.factory.DAOFactory;

import java.sql.SQLException;
import java.util.List;

public class RolesFacade {

    private final DAOFactory daoFactory;

    public RolesFacade() {
        this.daoFactory = DAOFactory.getInstance();
    }

    public List<RolDTO> listarRoles() throws SQLException {
        try (RolesDAO rolesDAO = daoFactory.getRolesDAO()) {
            return rolesDAO.obtenerRoles();
        }
    }

    public RolDTO obtenerRolPorId(int idRol) throws SQLException {
        try (RolesDAO rolesDAO = daoFactory.getRolesDAO()) {
            return rolesDAO.obtenerRolPorId(idRol);
        }
    }

    public void agregarRol(String nombreRol, String descripcion) throws SQLException {
        try (RolesDAO rolesDAO = daoFactory.getRolesDAO()) {
            rolesDAO.agregarRol(nombreRol, descripcion);
        }
    }

    public void actualizarRol(RolDTO rolDTO) throws SQLException {
        try (RolesDAO rolesDAO = daoFactory.getRolesDAO()) {
            rolesDAO.actualizarRol(rolDTO);
        }
    }

    public void eliminarRol(int idRol) throws SQLException {
        try (RolesDAO rolesDAO = daoFactory.getRolesDAO()) {
            rolesDAO.eliminarRol(idRol);
        }
    }

    public List<RolDTO> obtenerRolesDeUsuario(int idUsuario) throws SQLException {
        try (UsuariosRolesDAO usuariosRolesDAO = daoFactory.getUsuariosRolesDAO()) {
            return usuariosRolesDAO.obtenerRolesDeUsuario(idUsuario);
        }
    }

    public void asignarRolAUsuario(int idUsuario, int idRol) throws SQLException {
        try (UsuariosRolesDAO usuariosRolesDAO = daoFactory.getUsuariosRolesDAO()) {
            usuariosRolesDAO.asignarRolAUsuario(idUsuario, idRol);
        }
    }

    public void eliminarRolDeUsuario(int idUsuario, int idRol) throws SQLException {
        try (UsuariosRolesDAO usuariosRolesDAO = daoFactory.getUsuariosRolesDAO()) {
            usuariosRolesDAO.eliminarRolDeUsuario(idUsuario, idRol);
        }
    }
}
