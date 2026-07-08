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
import pe.gob.oece.bff.infrastructure.client.EjecucionExamen.dto.*;

import java.time.Duration;
import java.util.List;
import java.util.Map;

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
        return http.post(BASE_PATH + "/validar-token", request, authorizationHeader(), JsonNode.class);
    }

    public JsonNode iniciar(IniciarEjecucionExamenRequest request) {
        return http.post(BASE_PATH + "/iniciar", request, authorizationHeader(), JsonNode.class);
    }

    public JsonNode heartbeat(HeartbeatEjecucionExamenRequest request) {
        return http.post(BASE_PATH + "/heartbeat", request, authorizationHeader(), JsonNode.class);
    }

    public JsonNode guardarRespuestas(GuardarRespuestasRequest request) {
        return http.post(BASE_PATH + "/examen/guardar-respuestas", request, authorizationHeader(), JsonNode.class);
    }

    public JsonNode listarCursosRecomendados(List<Long> idsCompetencia) {
        return http.post(BASE_PATH + "/examen/cursos-recomendados", idsCompetencia, authorizationHeader(), JsonNode.class);
    }

    public JsonNode actualizarPreguntaActual(ActualizarPreguntaActualRequest request) {
        return http.put(BASE_PATH + "/pregunta-actual", request, authorizationHeader(), JsonNode.class);
    }

    public JsonNode cambiarEstado(CambiarEstadoEjecucionRequest request) {
        return http.put(BASE_PATH + "/estado", request, authorizationHeader(), JsonNode.class);
    }

    public JsonNode obtenerPorExamen(Integer idExamen) {
        return http.get(BASE_PATH + "/{idExamen}", authorizationHeader(), JsonNode.class, idExamen);
    }

    public JsonNode obtenerInscripcionPorId(Long idInscripcion) {
        return http.get(
                BASE_PATH + "/obtenerInscripcion/{idInscripcion}",
                authorizationHeader(),
                JsonNode.class,
                idInscripcion
        );
    }

    public JsonNode obtenerEstadoIndividual(Long idInscripcion) {
        return http.get(
                uriBuilder -> uriBuilder
                        .path(BASE_PATH + "/examen/status")
                        .queryParam("idInscripcion", idInscripcion)
                        .build(),
                authorizationHeader(),
                JsonNode.class
        );
    }

    public JsonNode obtenerResultados(Long idExamen) {
        return http.get(
                uriBuilder -> uriBuilder
                        .path(BASE_PATH + "/examen/resultado")
                        .queryParam("idExamen", idExamen)
                        .build(),
                authorizationHeader(),
                JsonNode.class
        );
    }

    public JsonNode obtenerInformacionExamen(Long idInscripcion) {
        return http.get(
                uriBuilder -> uriBuilder
                        .path(BASE_PATH + "/examen/informacion")
                        .queryParam("idInscripcion", idInscripcion)
                        .build(),
                authorizationHeader(),
                JsonNode.class
        );
    }

    public JsonNode finalizar(Long idExamen) {
        return finalizar(idExamen, null);
    }

    public JsonNode finalizar(Long idExamen, String authorization) {
        return http.get(
                uriBuilder -> uriBuilder
                        .path(BASE_PATH + "/examen/finalizar")
                        .queryParam("idExamen", idExamen)
                        .build(),
                authorizationHeader(authorization),
                JsonNode.class
        );
    }

    public JsonNode obtenerPreguntasRespondidas(Long idInscripcion) {
        return http.get(
                uriBuilder -> uriBuilder
                        .path(BASE_PATH + "/examen/Obtener")
                        .queryParam("idInscripcion", idInscripcion)
                        .build(),
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
        return authorizationHeader(authorization);
    }

    private static Map<String, String> authorizationHeader(String authorization) {
        if (authorization == null || authorization.isBlank()) {
            return null;
        }
        return Map.of(HttpHeaders.AUTHORIZATION, authorization);
    }
}
