package pe.gob.oece.bff.application.AprendizajePostulacion.ReprogramacionExamenes;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.ReprogramacionExamenesClient;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.reprogramacion.BusquedaProgramacionDisponibleRequest;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.reprogramacion.RegistrarReprogramacionRequest;

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
}
