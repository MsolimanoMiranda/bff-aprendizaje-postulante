package pe.gob.oece.bff.application.AprendizajePostulacion.ExamenTesteo;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pe.gob.oece.bff.infrastructure.client.EjecucionExamen.ExamenTesteoClient;

@Service
@RequiredArgsConstructor
public class ExamenTesteoService {

    private static final Logger logger = LoggerFactory.getLogger(ExamenTesteoService.class);

    private final ExamenTesteoClient examenTesteoClient;

    public JsonNode antesCreacion(
            Long idSesionTest,
            String usuario,
            String ipOrigen
    ) {
        logger.info(
                "AUDIT op=antesCreacionExamenTesteo idSesionTest={} usuario={} ipOrigen={}",
                idSesionTest,
                usuario,
                ipOrigen
        );
        return extraerData(examenTesteoClient.antesCreacion(idSesionTest, usuario, ipOrigen));
    }

    public JsonNode despCreacion(
            Long idSesionTest,
            Long idExamen,
            String usuario,
            String ipOrigen
    ) {
        logger.info(
                "AUDIT op=despCreacionExamenTesteo idSesionTest={} idExamen={} usuario={} ipOrigen={}",
                idSesionTest,
                idExamen,
                usuario,
                ipOrigen
        );
        return extraerData(examenTesteoClient.despCreacion(idSesionTest, idExamen, usuario, ipOrigen));
    }

    public JsonNode obtenerDetalleSesion(
            Long idSesionTest,
            String usuario,
            String ipOrigen
    ) {
        logger.info(
                "AUDIT op=obtenerDetalleSesionTesteo idSesionTest={} usuario={} ipOrigen={}",
                idSesionTest,
                usuario,
                ipOrigen
        );
        return extraerData(examenTesteoClient.obtenerDetalleSesion(idSesionTest, usuario, ipOrigen));
    }

    private JsonNode extraerData(JsonNode response) {
        if (response != null && response.has("data")) {
            return response.get("data");
        }
        return response;
    }
}
