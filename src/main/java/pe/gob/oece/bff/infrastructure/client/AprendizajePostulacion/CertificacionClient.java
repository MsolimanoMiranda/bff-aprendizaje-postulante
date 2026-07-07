package pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pe.gob.oece.bff.infrastructure.client.DownstreamClientErrorHandler;
import pe.gob.oece.bff.infrastructure.client.DownstreamWebClient;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Component
public class CertificacionClient {
    private static final Logger logger = LoggerFactory.getLogger(CertificacionClient.class);
    private final DownstreamWebClient http;
    private static final String BASE_PATH = "/api/v1/certificado";
    private static final String HEADER_POSTULANTE_ID = "X-Postulante-Id";
    public CertificacionClient(
            @Qualifier("aprendizajePostulacionWebClient") WebClient webClient,
            @Value("${app.clients.aprendizaje-postulacion.response-timeout-ms:8000}") long timeoutMs
    ) {
        this.http = new DownstreamWebClient(
                webClient,
                Duration.ofMillis(timeoutMs),
                new DownstreamClientErrorHandler("aprendizaje-postulacion", "ERROR-AP-PAG-EX")
        );
    }


    public JsonNode misCertificados(Long idPostulante) {
        logger.debug("GET {} idPostulante={}", BASE_PATH + "/"+idPostulante+"/mis-certificaciones", idPostulante);
        Map<String, Object> queryParams = new HashMap<>();
        if (idPostulante != null && idPostulante!=0) {
            queryParams.put("idPostulante", idPostulante);
        }

        return http.get(
                BASE_PATH + "/{idPostulacion}/mis-certificaciones",
                postulanteHeader(idPostulante),
                JsonNode.class,
                idPostulante
        );

    }

    public JsonNode obtenerDatosDescargaPorExamen(Long idExamen) {
        logger.debug("GET {} idExamen={}", BASE_PATH + "/examen/{idExamen}/datos-descarga", idExamen);
        return http.get(
                BASE_PATH + "/examen/{idExamen}/datos-descarga",
                null,
                JsonNode.class,
                idExamen
        );
    }

    private Map<String, String> postulanteHeader(Long postulanteId) {
        return Map.of(HEADER_POSTULANTE_ID, String.valueOf(postulanteId));
    }

}
