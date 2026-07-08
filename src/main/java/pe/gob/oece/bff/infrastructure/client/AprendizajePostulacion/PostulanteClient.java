package pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import pe.gob.oece.bff.infrastructure.client.DownstreamClientErrorHandler;
import pe.gob.oece.bff.infrastructure.client.DownstreamWebClient;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.RegistroPostulanteRequest;

import java.time.Duration;
import java.util.Map;

@Component
public class PostulanteClient {

    private static final String BASE_PATH = "/api/v1/postulante";
    private static final String PATH_PERFIL = BASE_PATH + "/perfil";
    private static final String PATH_DASHBOARD = BASE_PATH + "/dashboard";
    private static final String HEADER_POSTULANTE_ID = "X-Postulante-Id";

    private final DownstreamWebClient http;

    public PostulanteClient(
            @Qualifier("aprendizajePostulacionWebClient") WebClient webClient,
            @Value("${app.clients.aprendizaje-postulacion.response-timeout-ms:8000}") long timeoutMs
    ) {
        this.http = new DownstreamWebClient(
                webClient,
                Duration.ofMillis(timeoutMs),
                new DownstreamClientErrorHandler("aprendizaje-postulacion", "ERROR-AP-PST-EX")
        );
    }

    public JsonNode registrarPostulante(RegistroPostulanteRequest request) {
        return http.post(BASE_PATH, request, null, JsonNode.class);
    }

    public JsonNode obtenerPerfilWizard(Long postulanteId, Long idPostulacion, String token) {
        return http.get(
                ub -> appendPerfilQuery(ub.path(PATH_PERFIL), idPostulacion).build(),
                postulanteHeader(postulanteId, token),
                JsonNode.class
        );
    }

    public JsonNode obtenerPerfilWizard(Long postulanteId, String token) {
        return obtenerPerfilWizard(postulanteId, null, token);
    }

    public JsonNode obtenerDashboard(Long postulanteId, String token) {
        return http.get(PATH_DASHBOARD, postulanteHeader(postulanteId, token), JsonNode.class);
    }

    public JsonNode obtenerHistorialCertificados(Long idPostulante, String token) {
        return http.get(
                BASE_PATH + "/{idPostulante}/historial-certificados",
                authorizationHeader(token),
                JsonNode.class,
                idPostulante
        );
    }

    private static UriBuilder appendPerfilQuery(UriBuilder ub, Long idPostulacion) {
        if (idPostulacion != null) {
            ub.queryParam("idPostulacion", idPostulacion);
        }
        return ub;
    }

    private static Map<String, String> postulanteHeader(Long postulanteId) {
        return Map.of(HEADER_POSTULANTE_ID, String.valueOf(postulanteId));
    }

    private static Map<String, String> postulanteHeader(Long postulanteId, String token) {
        return Map.of(
                HEADER_POSTULANTE_ID, String.valueOf(postulanteId),
                HttpHeaders.AUTHORIZATION, "Bearer " + token
        );
    }

    private static Map<String, String> authorizationHeader(String token) {
        return Map.of(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    }
}
