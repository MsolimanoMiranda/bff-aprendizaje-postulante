package pe.gob.oece.bff.application.AprendizajePostulacion.EncuestaSatisfaccion;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.EncuestaSatisfaccionClient;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.encuesta.EncuestaRespuestaRequest;

@Service
public class EncuestaSatisfaccionCommandService {

    private static final Logger logger = LoggerFactory.getLogger(EncuestaSatisfaccionCommandService.class);

    private final EncuestaSatisfaccionClient encuestaSatisfaccionClient;

    public EncuestaSatisfaccionCommandService(EncuestaSatisfaccionClient encuestaSatisfaccionClient) {
        this.encuestaSatisfaccionClient = encuestaSatisfaccionClient;
    }

    public JsonNode registrarRespuesta(Long idUsuarioGu, EncuestaRespuestaRequest request, String ipOrigen) {
        logger.info(
                "AUDIT op=registrarRespuestaEncuestaSatisfaccion idUsuarioGu={} ipOrigen={} idFormulario={}",
                idUsuarioGu,
                ipOrigen,
                request.toString()
        );
        return encuestaSatisfaccionClient.registrarRespuesta(idUsuarioGu, request);
    }
}
