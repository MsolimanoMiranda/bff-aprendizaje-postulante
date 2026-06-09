package pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion;

import com.fasterxml.jackson.databind.JsonNode;
import java.time.Duration;
import java.util.Map;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pe.gob.oece.bff.infrastructure.client.DownstreamClientErrorHandler;
import pe.gob.oece.bff.infrastructure.client.DownstreamWebClient;

@Component
public class DetallePostulacionClient {

    private static final String BASE_PATH = "/api/v1/postulaciones";

    private final DownstreamWebClient http;

    public DetallePostulacionClient(
            @Qualifier("aprendizajePostulacionWebClient") WebClient webClient,
            @Value("${app.clients.aprendizaje-postulacion.response-timeout-ms:8000}") long timeoutMs
    ) {
        this.http = new DownstreamWebClient(
                webClient,
                Duration.ofMillis(timeoutMs),
                new DownstreamClientErrorHandler("aprendizaje-postulacion", "ERROR-AP-DP-EX")
        );
    }

    public JsonNode obtenerDetalle(Long idPostulacion, String token) {
        return http.get(
                BASE_PATH + "/{idPostulacion}/detalle",
                Map.of(HttpHeaders.AUTHORIZATION, "Bearer " + token),
                JsonNode.class,
                idPostulacion
        );
    }
}
