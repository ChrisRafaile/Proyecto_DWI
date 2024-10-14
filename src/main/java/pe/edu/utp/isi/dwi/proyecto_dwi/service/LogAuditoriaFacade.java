package pe.edu.utp.isi.dwi.proyecto_dwi.service;

import pe.edu.utp.isi.dwi.proyecto_dwi.dao.LogAuditoriaDAO;
import pe.edu.utp.isi.dwi.proyecto_dwi.dto.LogAuditoriaDTO;
import pe.edu.utp.isi.dwi.proyecto_dwi.entities.LogAuditoria;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LogAuditoriaFacade {

    private final LogAuditoriaDAO logAuditoriaDAO;

    public LogAuditoriaFacade(Connection connection) {
        this.logAuditoriaDAO = new LogAuditoriaDAO(connection);
    }

    public void registrarLog(LogAuditoriaDTO logAuditoriaDTO) throws SQLException {
        LogAuditoria logAuditoria = logAuditoriaDTO.toEntity();
        logAuditoriaDAO.registrarLog(logAuditoria);
    }

    public List<LogAuditoriaDTO> obtenerLogsAuditoria() throws SQLException {
        List<LogAuditoria> logsAuditoriaList = logAuditoriaDAO.obtenerLogsAuditoria();
        List<LogAuditoriaDTO> logsAuditoriaDTOList = new ArrayList<>();
        for (LogAuditoria log : logsAuditoriaList) {
            LogAuditoriaDTO dto = new LogAuditoriaDTO(
                    log.getIdLog(),
                    log.getIdUsuario(),
                    log.getAccion(),
                    log.getFecha()
            );
            logsAuditoriaDTOList.add(dto);
        }
        return logsAuditoriaDTOList;
    }
}
