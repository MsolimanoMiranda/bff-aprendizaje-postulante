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

    public JsonNode obtenerPerfilWizard(Long postulanteId, Long idPostulacion, String token, String ipOrigen) {
        logger.info("AUDIT op=obtenerPerfilWizard postulanteId={} ipOrigen={} idPostulacion={}",
                postulanteId, ipOrigen, idPostulacion);
        if (idPostulacion == null) {
            return postulanteClient.obtenerPerfilWizard(postulanteId, token);
        }
        return postulanteClient.obtenerPerfilWizard(postulanteId, idPostulacion, token);
    }

    public JsonNode obtenerDashboard(Long postulanteId, String token, String ipOrigen) {
        logger.info("AUDIT op=obtenerDashboard postulanteId={} ipOrigen={}", postulanteId, ipOrigen);
        return postulanteClient.obtenerDashboard(postulanteId, token);
    }

    public JsonNode obtenerUrlLoginAulaVirtual(String token, String ipOrigen) {
        logger.info("AUDIT op=obtenerUrlLoginAulaVirtual ipOrigen={}", ipOrigen);
        return postulanteClient.obtenerUrlLoginAulaVirtual(token);
    }

    public JsonNode obtenerDocumentosPostulante(Long idUsuarioGu, Long idPostulacion, String token, String ipOrigen) {
        Long idPostulante = resolverIdPostulante(idUsuarioGu, token);
        logger.info("AUDIT op=obtenerDocumentosPostulante idUsuarioGu={} postulanteId={} idPostulacion={} ipOrigen={}",
                idUsuarioGu, idPostulante, idPostulacion, ipOrigen);
        return postulanteClient.obtenerDocumentosPostulante(idPostulante, idPostulacion, token);
    }

    private Long resolverIdPostulante(Long idUsuarioGu, String token) {
        JsonNode response = postulanteClient.obtenerIdPostulantePorUsuarioGu(idUsuarioGu, token);
        JsonNode idPostulante = response == null ? null : response.get("idPostulante");
        if (idPostulante == null || !idPostulante.canConvertToLong()) {
            throw new IllegalStateException("El MS no retorno un idPostulante valido para el usuario GU " + idUsuarioGu);
        }
        return idPostulante.asLong();
    }

    public JsonNode obtenerHistorialCertificados(Long idPostulante, String token, String ipOrigen) {
        logger.info("AUDIT op=obtenerHistorialCertificados idPostulante={} ipOrigen={}", idPostulante, ipOrigen);
        return postulanteClient.obtenerHistorialCertificados(idPostulante, token);
    }
}
