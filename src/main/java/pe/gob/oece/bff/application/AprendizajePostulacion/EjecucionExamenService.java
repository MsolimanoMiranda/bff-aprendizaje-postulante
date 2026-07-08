package pe.gob.oece.bff.application.AprendizajePostulacion;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pe.gob.oece.bff.infrastructure.client.EjecucionExamen.EjecucionExamenClient;
import pe.gob.oece.bff.infrastructure.client.EjecucionExamen.dto.*;

import java.util.List;

@Service
public class EjecucionExamenService {
    private static final Logger logger = LoggerFactory.getLogger(EjecucionExamenService.class);

    private final EjecucionExamenClient ejecucionExamenClient;

    public EjecucionExamenService(EjecucionExamenClient ejecucionExamenClient) {
        this.ejecucionExamenClient = ejecucionExamenClient;
    }

    public JsonNode validarToken(ValidarTokenExamenRequest request, String ipOrigen) {
        logger.info(
                "AUDIT op=validarToken idInscripcion={} ipOrigen={}",
                request.idInscripcion(),
                ipOrigen
        );
        return ejecucionExamenClient.validarToken(request);
    }

    public JsonNode iniciar(IniciarEjecucionExamenRequest request, String ipOrigen) {
        logger.info(
                "AUDIT op=iniciar idExamen={} ipOrigen={}",
                request.idExamen(),
                ipOrigen
        );
        return ejecucionExamenClient.iniciar(request);
    }

    public JsonNode heartbeat(HeartbeatEjecucionExamenRequest request, String ipOrigen) {
        logger.info(
                "AUDIT op=heartbeat idEjecucion={} ipOrigen={}",
                request.idEjecucion(),
                ipOrigen
        );
        return ejecucionExamenClient.heartbeat(request);
    }

    public JsonNode guardarRespuestas(GuardarRespuestasRequest request, String ipOrigen) {
        logger.info(
                "AUDIT op=guardarRespuestas idExamen={} ipOrigen={}",
                request.idExamen(),
                ipOrigen
        );
        return ejecucionExamenClient.guardarRespuestas(request);
    }

    public JsonNode listarCursosRecomendados(List<Long> idsCompetencia, String ipOrigen) {
        logger.info(
                "AUDIT op=listarCursosRecomendados totalCompetencias={} ipOrigen={}",
                idsCompetencia != null ? idsCompetencia.size() : 0,
                ipOrigen
        );
        return ejecucionExamenClient.listarCursosRecomendados(idsCompetencia);
    }

    public JsonNode actualizarPreguntaActual(ActualizarPreguntaActualRequest request, String ipOrigen) {
        logger.info(
                "AUDIT op=actualizarPreguntaActual idEjecucion={} idPregunta={} ipOrigen={}",
                request.idEjecucion(),
                request.idPregunta(),
                ipOrigen
        );
        return ejecucionExamenClient.actualizarPreguntaActual(request);
    }

    public JsonNode cambiarEstado(CambiarEstadoEjecucionRequest request, String ipOrigen) {
        logger.info(
                "AUDIT op=cambiarEstado idEjecucion={} nuevoEstado={} ipOrigen={}",
                request.idEjecucion(),
                request.nuevoEstado(),
                ipOrigen
        );
        return ejecucionExamenClient.cambiarEstado(request);
    }

    public JsonNode obtenerPorExamen(Integer idExamen, String ipOrigen) {
        logger.info(
                "AUDIT op=obtenerPorExamen idExamen={} ipOrigen={}",
                idExamen,
                ipOrigen
        );
        return ejecucionExamenClient.obtenerPorExamen(idExamen);
    }

    public JsonNode obtenerEstadoIndividual(Long idInscripcion, String ipOrigen) {
        logger.info(
                "AUDIT op=obtenerEstadoIndividual idInscripcion={} ipOrigen={}",
                idInscripcion,
                ipOrigen
        );
        return ejecucionExamenClient.obtenerEstadoIndividual(idInscripcion);
    }

    public JsonNode obtenerResultados(Long idExamen, String ipOrigen) {
        logger.info(
                "AUDIT op=obtenerResultados idExamen={} ipOrigen={}",
                idExamen,
                ipOrigen
        );
        return ejecucionExamenClient.obtenerResultados(idExamen);
    }

    public JsonNode obtenerInformacionExamen(Long idInscripcion, String ipOrigen) {
        logger.info(
                "AUDIT op=obtenerInformacionExamen idInscripcion={} ipOrigen={}",
                idInscripcion,
                ipOrigen
        );
        return ejecucionExamenClient.obtenerInformacionExamen(idInscripcion);
    }

    public JsonNode finalizar(Long idExamen, String ipOrigen) {
        return finalizar(idExamen, ipOrigen, null);
    }

    public JsonNode finalizar(Long idExamen, String ipOrigen, String authorization) {
        logger.info(
                "AUDIT op=finalizar idExamen={} ipOrigen={}",
                idExamen,
                ipOrigen
        );
        return ejecucionExamenClient.finalizar(idExamen, authorization);
    }

    public JsonNode obtenerPreguntasRespondidas(Long idInscripcion, String ipOrigen) {
        logger.info(
                "AUDIT op=obtenerPreguntasRespondidas idInscripcion={} ipOrigen={}",
                idInscripcion,
                ipOrigen
        );
        return ejecucionExamenClient.obtenerPreguntasRespondidas(idInscripcion);
    }
}
