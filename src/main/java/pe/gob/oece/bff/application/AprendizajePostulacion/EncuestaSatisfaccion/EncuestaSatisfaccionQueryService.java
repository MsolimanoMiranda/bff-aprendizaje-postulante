package pe.gob.oece.bff.application.AprendizajePostulacion.EncuestaSatisfaccion;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.EncuestaSatisfaccionClient;

@Service
public class EncuestaSatisfaccionQueryService {

    private static final Logger logger = LoggerFactory.getLogger(EncuestaSatisfaccionQueryService.class);

    private final EncuestaSatisfaccionClient encuestaSatisfaccionClient;

    public EncuestaSatisfaccionQueryService(EncuestaSatisfaccionClient encuestaSatisfaccionClient) {
        this.encuestaSatisfaccionClient = encuestaSatisfaccionClient;
    }

    public JsonNode obtenerFormularioActivo(String ipOrigen) {
        logger.info("AUDIT op=obtenerFormularioActivoEncuestaSatisfaccion ipOrigen={}", ipOrigen);
        return encuestaSatisfaccionClient.obtenerFormularioActivo();
    }
}
