package pe.gob.oece.bff.application.AprendizajePostulacion;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.PostulanteClient;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.RegistroPostulanteRequest;

@Service
public class PostulanteService {

    private static final Logger logger = LoggerFactory.getLogger(PostulanteService.class);

    private final PostulanteClient postulanteClient;

    public PostulanteService(PostulanteClient postulanteClient) {
        this.postulanteClient = postulanteClient;
    }

    public JsonNode registrarPostulante(RegistroPostulanteRequest request, String ipOrigen) {
        logger.info("AUDIT op=registrarPostulante ipOrigen={}", ipOrigen);
        return postulanteClient.registrarPostulante(request);
    }

    public JsonNode obtenerPerfilWizard(Long postulanteId, Long idPostulacion, String ipOrigen) {
        logger.info("AUDIT op=obtenerPerfilWizard postulanteId={} ipOrigen={} idPostulacion={}",
                postulanteId, ipOrigen, idPostulacion);
        if (idPostulacion == null) {
            return postulanteClient.obtenerPerfilWizard(postulanteId);
        }
        return postulanteClient.obtenerPerfilWizard(postulanteId, idPostulacion);
    }

    public JsonNode obtenerDashboard(Long postulanteId, String ipOrigen) {
        logger.info("AUDIT op=obtenerDashboard postulanteId={} ipOrigen={}", postulanteId, ipOrigen);
        return postulanteClient.obtenerDashboard(postulanteId);
    }
}
