package pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion;

import com.fasterxml.jackson.databind.JsonNode;
import java.time.Duration;
import java.util.Map;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.reprogramacion.BusquedaProgramacionDisponibleRequest;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.reprogramacion.RegistrarReprogramacionRequest;
import pe.gob.oece.bff.infrastructure.client.DownstreamClientErrorHandler;
import pe.gob.oece.bff.infrastructure.client.DownstreamWebClient;

@Component
public class ReprogramacionExamenesClient {

    private static final String PATH_REPROGRAMACIONES = "/api/reprogramaciones";
    private static final String PATH_PROGRAMACIONES_DISPONIBLES =
            PATH_REPROGRAMACIONES + "/programaciones-disponibles";
    private static final String PATH_REGISTRAR = PATH_REPROGRAMACIONES + "/registrar";

    private final DownstreamWebClient http;

    public ReprogramacionExamenesClient(
            @Qualifier("aprendizajePostulacionWebClient") WebClient webClient,
            @Value("${app.clients.aprendizaje-postulacion.response-timeout-ms:8000}") long timeoutMs
    ) {
        this.http = new DownstreamWebClient(
                webClient,
                Duration.ofMillis(timeoutMs),
                new DownstreamClientErrorHandler("aprendizaje-postulacion", "ERROR-AP-REP-EX")
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
