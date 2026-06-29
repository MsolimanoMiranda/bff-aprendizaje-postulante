package pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pe.gob.oece.bff.infrastructure.client.DownstreamClientErrorHandler;
import pe.gob.oece.bff.infrastructure.client.DownstreamWebClient;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.ConfirmarPostulacionRequest;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.ExperienciaLaboralRequest;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.GenerarOrdenPagoRequest;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.IniciarPostulacionRequest;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.SeleccionarProgramacionRequest;

import java.time.Duration;
import java.util.Map;

@Component
public class PostulacionClient {

    private static final String BASE_PATH = "/api/v1/postulaciones";
    private static final String HEADER_POSTULANTE_ID = "X-Postulante-Id";

    private final DownstreamWebClient http;

    public PostulacionClient(
            @Qualifier("aprendizajePostulacionWebClient") WebClient webClient,
            @Value("${app.clients.aprendizaje-postulacion.response-timeout-ms:8000}") long timeoutMs
    ) {
        this.http = new DownstreamWebClient(
                webClient,
                Duration.ofMillis(timeoutMs),
                new DownstreamClientErrorHandler("aprendizaje-postulacion", "ERROR-AP-PL-EX")
        );
    }

    public JsonNode iniciarPostulacion(Long postulanteId, IniciarPostulacionRequest request) {
        return http.post(BASE_PATH, request, postulanteHeader(postulanteId), JsonNode.class);
    }

    public JsonNode listarMisPostulaciones(Long postulanteId, String token) {
        return http.get(BASE_PATH, postulanteHeader(postulanteId, token), JsonNode.class);
    }

    public JsonNode obtenerDetalle(Long idPostulacion, Long postulanteId) {
        return http.get(
                BASE_PATH + "/{idPostulacion}",
                postulanteHeader(postulanteId),
                JsonNode.class,
                idPostulacion
        );
    }

    public JsonNode agregarExperiencia(Long idPostulacion, Long postulanteId, ExperienciaLaboralRequest request) {
        return http.post(
                BASE_PATH + "/{idPostulacion}/experiencias",
                request,
                postulanteHeader(postulanteId),
                JsonNode.class,
                idPostulacion
        );
    }

    public JsonNode editarExperiencia(
            Long idPostulacion,
            Long idExperiencia,
            Long postulanteId,
            ExperienciaLaboralRequest request
    ) {
        return http.put(
                BASE_PATH + "/{idPostulacion}/experiencias/{idExperiencia}",
                request,
                postulanteHeader(postulanteId),
                JsonNode.class,
                idPostulacion,
                idExperiencia
        );
    }

    public JsonNode eliminarExperiencia(Long idPostulacion, Long idExperiencia, Long postulanteId) {
        return http.delete(
                BASE_PATH + "/{idPostulacion}/experiencias/{idExperiencia}",
                postulanteHeader(postulanteId),
                JsonNode.class,
                idPostulacion,
                idExperiencia
        );
    }

    public JsonNode seleccionarProgramacion(
            Long idPostulacion,
            Long postulanteId,
            SeleccionarProgramacionRequest request
    ) {
        return http.post(
                BASE_PATH + "/{idPostulacion}/programacion",
                request,
                postulanteHeader(postulanteId),
                JsonNode.class,
                idPostulacion
        );
    }

    public JsonNode generarOrdenPago(
            Long idPostulacion,
            Long postulanteId,
            GenerarOrdenPagoRequest request
    ) {
        return http.post(
                BASE_PATH + "/{idPostulacion}/pagos",
                request,
                postulanteHeader(postulanteId),
                JsonNode.class,
                idPostulacion
        );
    }

    public JsonNode confirmarPostulacion(
            Long idPostulacion,
            Long postulanteId,
            ConfirmarPostulacionRequest request
    ) {
        return http.post(
                BASE_PATH + "/{idPostulacion}/confirmar",
                request,
                postulanteHeader(postulanteId),
                JsonNode.class,
                idPostulacion
        );
    }

    private Map<String, String> postulanteHeader(Long postulanteId) {
        return Map.of(HEADER_POSTULANTE_ID, String.valueOf(postulanteId));
    }

    private Map<String, String> postulanteHeader(Long postulanteId, String token) {
        return Map.of(
                HEADER_POSTULANTE_ID, String.valueOf(postulanteId),
                HttpHeaders.AUTHORIZATION, "Bearer " + token
        );
    }
}
