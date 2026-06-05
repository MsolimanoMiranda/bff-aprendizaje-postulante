package pe.gob.oece.bff.infrastructure.client.EjecucionExamen;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pe.gob.oece.bff.infrastructure.client.DownstreamClientErrorHandler;
import pe.gob.oece.bff.infrastructure.client.DownstreamWebClient;
import pe.gob.oece.bff.infrastructure.client.EjecucionExamen.dto.*;

import java.time.Duration;
import java.util.List;

@Component
public class EjecucionExamenClient {

    private static final String BASE_PATH = "/api/v1/ejecucion-examen";

    private final DownstreamWebClient http;

    public EjecucionExamenClient(
            @Qualifier("aprendizajePostulacionWebClient") WebClient webClient,
            @Value("${app.clients.aprendizaje-postulacion.response-timeout-ms:8000}") long timeoutMs
    ) {
        this.http = new DownstreamWebClient(
                webClient,
                Duration.ofMillis(timeoutMs),
                new DownstreamClientErrorHandler("aprendizaje-postulacion", "ERROR-AP-EJE-EX")
        );
    }

    public JsonNode validarToken(ValidarTokenExamenRequest request) {
        return http.post(BASE_PATH + "/validar-token", request, null, JsonNode.class);
    }

    public JsonNode iniciar(IniciarEjecucionExamenRequest request) {
        return http.post(BASE_PATH + "/iniciar", request, null, JsonNode.class);
    }

    public JsonNode heartbeat(HeartbeatEjecucionExamenRequest request) {
        return http.post(BASE_PATH + "/heartbeat", request, null, JsonNode.class);
    }

    public JsonNode guardarRespuestas(GuardarRespuestasRequest request) {
        return http.post(BASE_PATH + "/examen/guardar-respuestas", request, null, JsonNode.class);
    }

    public JsonNode listarCursosRecomendados(List<Long> idsCompetencia) {
        return http.post(BASE_PATH + "/examen/cursos-recomendados", idsCompetencia, null, JsonNode.class);
    }

    public JsonNode actualizarPreguntaActual(ActualizarPreguntaActualRequest request) {
        return http.put(BASE_PATH + "/pregunta-actual", request, null, JsonNode.class);
    }

    public JsonNode cambiarEstado(CambiarEstadoEjecucionRequest request) {
        return http.put(BASE_PATH + "/estado", request, null, JsonNode.class);
    }

    public JsonNode obtenerPorExamen(Integer idExamen) {
        return http.get(BASE_PATH + "/{idExamen}", null, JsonNode.class, idExamen);
    }

    public JsonNode obtenerEstadoIndividual(Long idInscripcion) {
        return http.get(
                uriBuilder -> uriBuilder
                        .path(BASE_PATH + "/examen/status")
                        .queryParam("idInscripcion", idInscripcion)
                        .build(),
                null,
                JsonNode.class
        );
    }

    public JsonNode obtenerResultados(Long idExamen) {
        return http.get(
                uriBuilder -> uriBuilder
                        .path(BASE_PATH + "/examen/resultado")
                        .queryParam("idExamen", idExamen)
                        .build(),
                null,
                JsonNode.class
        );
    }

    public JsonNode obtenerInformacionExamen(Long idInscripcion) {
        return http.get(
                uriBuilder -> uriBuilder
                        .path(BASE_PATH + "/examen/informacion")
                        .queryParam("idInscripcion", idInscripcion)
                        .build(),
                null,
                JsonNode.class
        );
    }

    public JsonNode finalizar(Long idExamen) {
        return http.get(
                uriBuilder -> uriBuilder
                        .path(BASE_PATH + "/examen/finalizar")
                        .queryParam("idExamen", idExamen)
                        .build(),
                null,
                JsonNode.class
        );
    }

    public JsonNode obtenerPreguntasRespondidas(Long idInscripcion) {
        return http.get(
                uriBuilder -> uriBuilder
                        .path(BASE_PATH + "/examen/Obtener")
                        .queryParam("idInscripcion", idInscripcion)
                        .build(),
                null,
                JsonNode.class
        );
    }
}
