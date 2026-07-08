package pe.gob.oece.bff.application.AprendizajePostulacion;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.ExpedienteClient;

@RequiredArgsConstructor
@Service
public class ExpedienteService {

    private static final Logger logger = LoggerFactory.getLogger(ExpedienteService.class);

    private final ExpedienteClient expedienteClient;

    public JsonNode registrarExpediente(JsonNode request, String authorization, String ipOrigen) {
        logger.info("AUDIT op=registrarExpediente ipOrigen={}", ipOrigen);
        return expedienteClient.registrarExpediente(request, authorization);
    }

    public JsonNode generarCargo(String authorization, String ipOrigen) {
        logger.info("AUDIT op=generarCargo ipOrigen={}", ipOrigen);
        return expedienteClient.generarCargo(authorization);
    }
}
