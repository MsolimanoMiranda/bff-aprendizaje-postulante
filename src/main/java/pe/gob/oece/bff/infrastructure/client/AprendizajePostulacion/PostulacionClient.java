package pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import pe.gob.oece.bff.infrastructure.client.DownstreamClientErrorHandler;
import pe.gob.oece.bff.infrastructure.client.DownstreamWebClient;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.ConfirmarPostulacionRequest;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.GenerarOrdenPagoRequest;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.IniciarPostulacionRequest;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.SeleccionarProgramacionRequest;

import java.time.Duration;
import java.util.Map;

@Component
public class PostulacionClient {

    private static final String BASE_PATH = "/api/v1/postulaciones";
    private static final String FORMACION_ACADEMICA_BASE_PATH = "/api/v1/formacion-academica";
    private static final String LISTAS_BASE_PATH = "/api/v1/listas";
    private static final String HEADER_POSTULANTE_ID = "X-Postulante-Id";

    private final DownstreamWebClient http;
    private final WebClient webClient;
    private final Duration timeout;
    private final DownstreamClientErrorHandler errorHandler;

    public PostulacionClient(
            @Qualifier("aprendizajePostulacionWebClient") WebClient webClient,
            @Value("${app.clients.aprendizaje-postulacion.response-timeout-ms:8000}") long timeoutMs
    ) {
        this.webClient = webClient;
        this.timeout = Duration.ofMillis(timeoutMs);
        this.errorHandler = new DownstreamClientErrorHandler("aprendizaje-postulacion", "ERROR-AP-PL-EX");
        this.http = new DownstreamWebClient(
                webClient,
                this.timeout,
                this.errorHandler
        );
    }

    public JsonNode iniciarPostulacion(Long postulanteId, IniciarPostulacionRequest request) {
        return http.post(BASE_PATH, request, postulanteHeader(postulanteId), JsonNode.class);
    }

    public JsonNode listarMisPostulaciones(Long postulanteId, String token) {
        return http.get(BASE_PATH, postulanteHeader(postulanteId, token), JsonNode.class);
    }

    public JsonNode listarNivelesCertificacion(String token) {
        return http.get(BASE_PATH + "/niveles-certificacion", bearerHeader(token), JsonNode.class);
    }

    public JsonNode obtenerDetalle(Long idPostulacion, Long postulanteId) {
        return http.get(
                BASE_PATH + "/{idPostulacion}",
                postulanteHeader(postulanteId),
                JsonNode.class,
                idPostulacion
        );
    }

    public JsonNode agregarExperiencia(
            Long idPostulacion,
            Long postulanteId,
            MultiValueMap<String, String> request,
            MultiValueMap<String, MultipartFile> archivos,
            String token) {
        return enviarExperiencia("POST", idPostulacion, null, postulanteId, request, archivos, token);
    }

    public JsonNode editarExperiencia(
            Long idPostulacion,
            Long idExperiencia,
            Long postulanteId,
            MultiValueMap<String, String> request,
            MultiValueMap<String, MultipartFile> archivos,
            String token
    ) {
        return enviarExperiencia("PUT", idPostulacion, idExperiencia, postulanteId, request, archivos, token);
    }

    private JsonNode enviarExperiencia(
            String method,
            Long idPostulacion,
            Long idExperiencia,
            Long postulanteId,
            MultiValueMap<String, String> request,
            MultiValueMap<String, MultipartFile> archivos,
            String token) {
        MultipartBodyBuilder body = multipartBody(request, archivos);
        String uri = idExperiencia == null
                ? BASE_PATH + "/{idPostulacion}/experiencias"
                : BASE_PATH + "/{idPostulacion}/experiencias/{idExperiencia}";

        WebClient.RequestBodySpec spec = "PUT".equals(method)
                ? webClient.put().uri(uri, idPostulacion, idExperiencia)
                : webClient.post().uri(uri, idPostulacion);

        return spec
                .headers(headers -> {
                    headers.set(HEADER_POSTULANTE_ID, String.valueOf(postulanteId));
                    Map<String, String> authorizationHeader = bearerHeader(token);
                    if (authorizationHeader != null) {
                        authorizationHeader.forEach(headers::set);
                    }
                })
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(body.build()))
                .retrieve()
                .onStatus(HttpStatusCode::isError, errorHandler::toError)
                .bodyToMono(JsonNode.class)
                .timeout(timeout)
                .block();
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

    public JsonNode agregarFormacion(
            MultiValueMap<String, String> request,
            MultiValueMap<String, MultipartFile> archivos,
            String token) {
        return enviarFormacion("POST", null, request, archivos, token);
    }

    public JsonNode editarFormacion(
            Long idFormacion,
            MultiValueMap<String, String> request,
            MultiValueMap<String, MultipartFile> archivos,
            String token) {
        return enviarFormacion("PUT", idFormacion, request, archivos, token);
    }

    private JsonNode enviarFormacion(
            String method,
            Long idFormacion,
            MultiValueMap<String, String> request,
            MultiValueMap<String, MultipartFile> archivos,
            String token) {
        MultipartBodyBuilder body = multipartBody(request, archivos);
        String uri = idFormacion == null
                ? FORMACION_ACADEMICA_BASE_PATH
                : FORMACION_ACADEMICA_BASE_PATH + "/{idFormacion}";

        WebClient.RequestBodySpec spec = "PUT".equals(method)
                ? webClient.put().uri(uri, idFormacion)
                : webClient.post().uri(uri);

        return spec
                .headers(headers -> {
                    Map<String, String> authorizationHeader = bearerHeader(token);
                    if (authorizationHeader != null) {
                        authorizationHeader.forEach(headers::set);
                    }
                })
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(body.build()))
                .retrieve()
                .onStatus(HttpStatusCode::isError, errorHandler::toError)
                .bodyToMono(JsonNode.class)
                .timeout(timeout)
                .block();
    }

    public JsonNode eliminarFormacion(Long idFormacion) {
        return http.delete(
                FORMACION_ACADEMICA_BASE_PATH + "/{idFormacion}",
                null,
                JsonNode.class,
                idFormacion
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

    public JsonNode enviarSubsanacion(
            Long idPostulacion,
            MultiValueMap<String, String> request,
            MultiValueMap<String, MultipartFile> archivos,
            String token) {
        MultipartBodyBuilder body = multipartBody(request, archivos);
        return webClient.post()
                .uri(BASE_PATH + "/{idPostulacion}/subsanacion/enviar", idPostulacion)
                .headers(headers -> {
                    Map<String, String> authorizationHeader = bearerHeader(token);
                    if (authorizationHeader != null) {
                        authorizationHeader.forEach(headers::set);
                    }
                })
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(body.build()))
                .retrieve()
                .onStatus(HttpStatusCode::isError, errorHandler::toError)
                .bodyToMono(JsonNode.class)
                .timeout(timeout)
                .block();
    }

    private MultipartBodyBuilder multipartBody(
            MultiValueMap<String, String> request,
            MultiValueMap<String, MultipartFile> archivos) {
        MultipartBodyBuilder body = new MultipartBodyBuilder();
        if (request != null) {
            request.forEach((name, values) -> values.forEach(value -> body.part(name, value)));
        }
        if (archivos != null) {
            archivos.forEach((name, values) -> values.stream()
                    .filter(archivo -> archivo != null && !archivo.isEmpty())
                    .forEach(archivo -> body.part(name, recursoArchivo(archivo))
                            .filename(archivo.getOriginalFilename() == null ? "archivo" : archivo.getOriginalFilename())
                            .contentType(archivo.getContentType() == null
                                    ? MediaType.APPLICATION_OCTET_STREAM
                                    : MediaType.parseMediaType(archivo.getContentType()))));
        }
        return body;
    }

    public JsonNode cancelarPostulacion(JsonNode request) {
        return http.post(BASE_PATH + "/cancelar", request, null, JsonNode.class);
    }

    public JsonNode listarPorTipoLista(String tipo) {
        return http.get(LISTAS_BASE_PATH + "/{tipo}", null, JsonNode.class, tipo);
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

    private Map<String, String> bearerHeader(String token) {
        if (token == null || token.isBlank()) {
            return null;
        }
        return Map.of(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    }

    private static ByteArrayResource recursoArchivo(MultipartFile archivo) {
        try {
            byte[] bytes = archivo.getBytes();
            return new ByteArrayResource(bytes) {
                @Override
                public String getFilename() {
                    return archivo.getOriginalFilename() == null ? "archivo" : archivo.getOriginalFilename();
                }
            };
        } catch (IOException ex) {
            throw new IllegalArgumentException("No se pudo leer el archivo de subsanacion", ex);
        }
    }
}
