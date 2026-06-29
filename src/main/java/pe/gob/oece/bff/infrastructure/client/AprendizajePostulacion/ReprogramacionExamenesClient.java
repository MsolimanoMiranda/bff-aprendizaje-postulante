package pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.reprogramacion.BusquedaProgramacionDisponibleRequest;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.reprogramacion.RegistrarReprogramacionRequest;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.reprogramacion.SolicitudReprogramacionRequest;
import pe.gob.oece.bff.infrastructure.client.DownstreamClientErrorHandler;
import pe.gob.oece.bff.infrastructure.client.DownstreamWebClient;

@Component
public class ReprogramacionExamenesClient {

    private static final String PATH_REPROGRAMACIONES = "/api/reprogramaciones";
    private static final String PATH_PROGRAMACIONES_DISPONIBLES =
            PATH_REPROGRAMACIONES + "/programaciones-disponibles";
    private static final String PATH_REGISTRAR = PATH_REPROGRAMACIONES + "/registrar";

    private final WebClient webClient;
    private final Duration timeout;
    private final DownstreamClientErrorHandler errorHandler;
    private final DownstreamWebClient http;

    public ReprogramacionExamenesClient(
            @Qualifier("aprendizajePostulacionWebClient") WebClient webClient,
            @Value("${app.clients.aprendizaje-postulacion.response-timeout-ms:8000}") long timeoutMs
    ) {
        this.webClient = webClient;
        this.timeout = Duration.ofMillis(timeoutMs);
        this.errorHandler = new DownstreamClientErrorHandler("aprendizaje-postulacion", "ERROR-AP-REP-EX");
        this.http = new DownstreamWebClient(
                webClient,
                this.timeout,
                this.errorHandler
        );
    }

    public JsonNode buscarProgramacionesDisponibles(
            BusquedaProgramacionDisponibleRequest solicitud,
            String authorization
    ) {
        return http.get(
                ub -> appendProgramacionesDisponiblesQuery(ub.path(PATH_PROGRAMACIONES_DISPONIBLES), solicitud)
                        .build(),
                authorizationHeader(authorization),
                JsonNode.class
        );
    }

    public JsonNode registrarReprogramacion(
            RegistrarReprogramacionRequest solicitud,
            String authorization
    ) {
        return http.post(PATH_REGISTRAR, solicitud, authorizationHeader(authorization), JsonNode.class);
    }

    public JsonNode registrarSolicitudReprogramacion(
            SolicitudReprogramacionRequest solicitud,
            List<MultipartFile> archivos,
            String usuario,
            String ip,
            String authorization
    ) {
        MultipartBodyBuilder body = new MultipartBodyBuilder();
        body.part("request", solicitud).contentType(MediaType.APPLICATION_JSON);
        for (MultipartFile archivo : archivos) {
            body.part("archivos", recursoArchivo(archivo))
                    .filename(archivo.getOriginalFilename() == null ? "archivo" : archivo.getOriginalFilename())
                    .contentType(archivo.getContentType() == null
                            ? MediaType.APPLICATION_OCTET_STREAM
                            : MediaType.parseMediaType(archivo.getContentType()));
        }

        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH_REPROGRAMACIONES)
                        .queryParam("usuario", usuario)
                        .queryParam("ip", ip)
                        .build())
                .headers(headers -> {
                    Map<String, String> authorizationHeader = authorizationHeader(authorization);
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
            throw new IllegalArgumentException("No se pudo leer el archivo de solicitud de reprogramacion", ex);
        }
    }

    private static UriBuilder appendProgramacionesDisponiblesQuery(
            UriBuilder ub,
            BusquedaProgramacionDisponibleRequest r
    ) {
        queryParamIfPresent(ub, "idLocal", r.idLocal());
        queryParamIfPresent(ub, "fechaDesde", r.fechaDesde());
        queryParamIfPresent(ub, "fechaHasta", r.fechaHasta());
        queryParamIfPresent(ub, "columnaOrden", r.columnaOrden());
        queryParamIfPresent(ub, "direccionOrden", r.direccionOrden());
        queryParamIfPresent(ub, "pagina", r.pagina());
        queryParamIfPresent(ub, "tamano", r.tamano());
        return ub;
    }

    private static void queryParamIfPresent(UriBuilder ub, String name, Object value) {
        if (value == null) {
            return;
        }
        if (value instanceof String s && s.isEmpty()) {
            return;
        }
        ub.queryParam(name, value);
    }

    private static Map<String, String> authorizationHeader(String authorization) {
        if (authorization == null || authorization.isBlank()) {
            return null;
        }
        return Map.of(HttpHeaders.AUTHORIZATION, authorization);
    }
}
