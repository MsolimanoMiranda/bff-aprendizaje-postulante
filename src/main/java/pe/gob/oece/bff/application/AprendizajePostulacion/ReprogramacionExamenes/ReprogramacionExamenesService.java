package pe.gob.oece.bff.application.AprendizajePostulacion.ReprogramacionExamenes;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.ReprogramacionExamenesClient;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.reprogramacion.BusquedaProgramacionDisponibleRequest;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.reprogramacion.RegistrarReprogramacionRequest;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.reprogramacion.SolicitudReprogramacionRequest;

@Service
public class ReprogramacionExamenesService {

    private static final Logger logger = LoggerFactory.getLogger(ReprogramacionExamenesService.class);

    private final ReprogramacionExamenesClient reprogramacionExamenesClient;

    public ReprogramacionExamenesService(ReprogramacionExamenesClient reprogramacionExamenesClient) {
        this.reprogramacionExamenesClient = reprogramacionExamenesClient;
    }

    public JsonNode buscarProgramacionesDisponibles(
            BusquedaProgramacionDisponibleRequest solicitud,
            String authorization,
            String ipOrigen
    ) {
        logger.info("AUDIT op=buscarProgramacionesDisponiblesReprogramacion idLocal={} ipOrigen={}",
                solicitud.idLocal(), ipOrigen);
        return reprogramacionExamenesClient.buscarProgramacionesDisponibles(solicitud, authorization);
    }

    public JsonNode registrarReprogramacion(
            RegistrarReprogramacionRequest solicitud,
            String authorization,
            String ipOrigen
    ) {
        logger.info("AUDIT op=registrarReprogramacion idPostulacion={} idProgNueva={} ipOrigen={}",
                solicitud.idPostulacion(), solicitud.idProgExamenNueva(), ipOrigen);
        return reprogramacionExamenesClient.registrarReprogramacion(solicitud, authorization);
    }

    public JsonNode registrarSolicitudReprogramacion(
            SolicitudReprogramacionRequest solicitud,
            List<MultipartFile> archivos,
            String usuario,
            String authorization,
            String ipOrigen
    ) {
        logger.info("AUDIT op=registrarSolicitudReprogramacion idInscripcion={} archivos={} ipOrigen={}",
                solicitud.idInscripcion(), archivos == null ? 0 : archivos.size(), ipOrigen);
        return reprogramacionExamenesClient.registrarSolicitudReprogramacion(
                solicitud,
                archivos,
                usuario,
                ipOrigen,
                authorization);
    }
}
