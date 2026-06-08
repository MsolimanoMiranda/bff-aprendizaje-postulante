package pe.gob.oece.bff.application.AprendizajePostulacion.DetallePostulacion;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.DetallePostulacionClient;

@Service
public class DetallePostulacionQueryService {

    private static final Logger logger = LoggerFactory.getLogger(DetallePostulacionQueryService.class);

    private final DetallePostulacionClient detallePostulacionClient;

    public DetallePostulacionQueryService(DetallePostulacionClient detallePostulacionClient) {
        this.detallePostulacionClient = detallePostulacionClient;
    }

    public JsonNode obtenerDetalle(Long idPostulacion, String token, String ipOrigen) {
        logger.info("AUDIT op=obtenerDetallePostulacion idPostulacion={} ipOrigen={}", idPostulacion, ipOrigen);
        return detallePostulacionClient.obtenerDetalle(idPostulacion, token);
    }
}
