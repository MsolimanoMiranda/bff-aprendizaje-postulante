package pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pe.gob.oece.bff.infrastructure.client.DownstreamClientErrorHandler;
import pe.gob.oece.bff.infrastructure.client.DownstreamWebClient;

import java.time.Duration;

@Component
public class ParametrosClient {

    private static final String BASE_PATH = "/api/v1/parametros";

    private final DownstreamWebClient http;

    public ParametrosClient(
            @Qualifier("aprendizajePostulacionWebClient") WebClient webClient,
            @Value("${app.clients.aprendizaje-postulacion.response-timeout-ms:8000}") long timeoutMs
    ) {
        this.http = new DownstreamWebClient(
                webClient,
                Duration.ofMillis(timeoutMs),
                new DownstreamClientErrorHandler("aprendizaje-postulacion", "ERROR-AP-PRM-EX")
        );
    }

    public JsonNode buscarPorCodigo(String codigo) {
        return http.get(BASE_PATH + "/{codigo}", null, JsonNode.class, codigo);
    }

    public JsonNode listarPorSeccion(Long idSeccion) {
        return http.get(BASE_PATH + "/seccion/{idSeccion}", null, JsonNode.class, idSeccion);
    }
}
