package pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion;

import com.fasterxml.jackson.databind.JsonNode;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pe.gob.oece.bff.infrastructure.client.DownstreamClientErrorHandler;
import pe.gob.oece.bff.infrastructure.client.DownstreamWebClient;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.encuesta.EncuestaRespuestaRequest;

import java.util.Map;

@Component
public class EncuestaSatisfaccionClient {

    private static final String BASE_PATH = "/api/v1/encuesta-satisfaccion";
    private static final String HEADER_POSTULANTE_ID = "X-Postulante-Id";

    private final DownstreamWebClient http;

    public EncuestaSatisfaccionClient(
            @Qualifier("aprendizajePostulacionWebClient") WebClient webClient,
            @Value("${app.clients.aprendizaje-postulacion.response-timeout-ms:8000}") long timeoutMs
    ) {
        this.http = new DownstreamWebClient(
                webClient,
                Duration.ofMillis(timeoutMs),
                new DownstreamClientErrorHandler("aprendizaje-postulacion", "ERROR-AP-ES-EX")
        );
    }

    public JsonNode obtenerFormularioActivo() {
        return http.get(BASE_PATH + "/formulario-activo", null, JsonNode.class);
    }

    public JsonNode registrarRespuesta(Long idUsuarioGu, EncuestaRespuestaRequest request) {
        return http.post(
                BASE_PATH + "/respuestas",
                request,
                Map.of(HEADER_POSTULANTE_ID, String.valueOf(idUsuarioGu)),
                JsonNode.class
        );
    }
}
