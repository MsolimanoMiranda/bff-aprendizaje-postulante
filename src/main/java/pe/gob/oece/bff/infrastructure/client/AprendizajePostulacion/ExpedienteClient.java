package pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pe.gob.oece.bff.infrastructure.client.DownstreamClientErrorHandler;
import pe.gob.oece.bff.infrastructure.client.DownstreamWebClient;

import java.time.Duration;
import java.util.Map;

@Component
public class ExpedienteClient {

    private static final String BASE_PATH = "/api/v1/expedientes";

    private final DownstreamWebClient http;

    public ExpedienteClient(
            @Qualifier("aprendizajePostulacionWebClient") WebClient webClient,
            @Value("${app.clients.aprendizaje-postulacion.response-timeout-ms:8000}") long timeoutMs
    ) {
        this.http = new DownstreamWebClient(
                webClient,
                Duration.ofMillis(timeoutMs),
                new DownstreamClientErrorHandler("aprendizaje-postulacion", "ERROR-AP-EXP-EX")
        );
    }

    public JsonNode registrarExpediente(JsonNode request, String authorization) {
        return http.post(BASE_PATH, request, authorizationHeader(authorization), JsonNode.class);
    }

    public JsonNode generarCargo(String authorization) {
        return http.post(BASE_PATH + "/cargo/generar", null, authorizationHeader(authorization), JsonNode.class);
    }

    private static Map<String, String> authorizationHeader(String authorization) {
        if (authorization == null || authorization.isBlank()) {
            return null;
        }
        return Map.of(HttpHeaders.AUTHORIZATION, authorization);
    }
}
