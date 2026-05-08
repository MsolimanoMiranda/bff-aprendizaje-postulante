package pe.gob.oece.bff.application.AprendizajePostulacion;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.ParametrosClient;

@Service
public class ParametrosService {

    private static final Logger logger = LoggerFactory.getLogger(ParametrosService.class);

    private final ParametrosClient parametrosClient;

    public ParametrosService(ParametrosClient parametrosClient) {
        this.parametrosClient = parametrosClient;
    }

    public JsonNode buscarPorCodigo(String codigo, String ipOrigen) {
        logger.info("AUDIT op=buscarPorCodigo codigo={} ipOrigen={}", codigo, ipOrigen);
        return parametrosClient.buscarPorCodigo(codigo);
    }

    public JsonNode listarPorSeccion(Long idSeccion, String ipOrigen) {
        logger.info("AUDIT op=listarPorSeccion idSeccion={} ipOrigen={}", idSeccion, ipOrigen);
        return parametrosClient.listarPorSeccion(idSeccion);
    }
}
