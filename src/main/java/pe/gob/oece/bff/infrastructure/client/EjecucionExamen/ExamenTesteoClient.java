package pe.gob.oece.bff.infrastructure.client.EjecucionExamen;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.WebClient;
import pe.gob.oece.bff.infrastructure.client.DownstreamClientErrorHandler;
import pe.gob.oece.bff.infrastructure.client.DownstreamWebClient;

import java.time.Duration;
import java.util.Map;

@Component
public class ExamenTesteoClient {

    private static final String BASE_PATH = "/api/v1/examen-testeo";

    private final DownstreamWebClient http;

    public ExamenTesteoClient(
            @Qualifier("aprendizajePostulacionWebClient") WebClient webClient,
            @Value("${app.clients.aprendizaje-postulacion.response-timeout-ms:8000}") long timeoutMs
    ) {
        this.http = new DownstreamWebClient(
                webClient,
                Duration.ofMillis(timeoutMs),
                new DownstreamClientErrorHandler("aprendizaje-postulacion", "ERROR-AP-EXA-TEST")
        );
    }

    public JsonNode antesCreacion(
            Long idSesionTest,
            String usuario,
            String ip
    ) {
        return http.post(
                uriBuilder -> uriBuilder
                        .path(BASE_PATH + "/antes-creacion")
                        .queryParam("idSesionTest", idSesionTest)
                        .queryParam("usuario", usuario)
                        .queryParam("ip", ip)
                        .build(),
                null,
                authorizationHeader(),
                JsonNode.class
        );
    }

    public JsonNode despCreacion(
            Long idSesionTest,
            Long idExamen,
            String usuario,
            String ip
    ) {
        return http.post(
                uriBuilder -> uriBuilder
                        .path(BASE_PATH + "/desp-creacion")
                        .queryParam("idSesionTest", idSesionTest)
                        .queryParam("idExamen", idExamen)
                        .queryParam("usuario", usuario)
                        .queryParam("ip", ip)
                        .build(),
                null,
                authorizationHeader(),
                JsonNode.class
        );
    }

    public JsonNode obtenerDetalleSesion(
            Long idSesionTest,
            String usuario,
            String ip
    ) {
        return http.post(
                uriBuilder -> uriBuilder
                        .path(BASE_PATH + "/obtener-det-sesion")
                        .queryParam("idSesionTest", idSesionTest)
                        .queryParam("usuario", usuario)
                        .queryParam("ip", ip)
                        .build(),
                null,
                authorizationHeader(),
                JsonNode.class
        );
    }

    private static Map<String, String> authorizationHeader() {
        if (!(RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes)) {
            return null;
        }

        HttpServletRequest request = attributes.getRequest();
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null || authorization.isBlank()) {
            return null;
        }
        return Map.of(HttpHeaders.AUTHORIZATION, authorization);
    }
}
