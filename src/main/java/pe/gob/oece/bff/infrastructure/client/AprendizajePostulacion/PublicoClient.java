package pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import pe.gob.oece.bff.infrastructure.client.DownstreamClientErrorHandler;
import pe.gob.oece.bff.infrastructure.client.DownstreamWebClient;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.BusquedaCertificadoRequest;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.BusquedaProgramacionRequest;

import java.time.Duration;

@Component
public class PublicoClient {

    private static final String PATH_PROGRAMACIONES = "/api/v1/publico/programaciones";
    private static final String PATH_CERTIFICADOS = "/api/v1/publico/certificados";

    private final DownstreamWebClient http;

    public PublicoClient(
            @Qualifier("aprendizajePostulacionWebClient") WebClient webClient,
            @Value("${app.clients.aprendizaje-postulacion.response-timeout-ms:8000}") long timeoutMs
    ) {
        this.http = new DownstreamWebClient(
                webClient,
                Duration.ofMillis(timeoutMs),
                new DownstreamClientErrorHandler("aprendizaje-postulacion", "ERROR-AP-PUB-EX")
        );
    }

    public JsonNode buscarProgramaciones(BusquedaProgramacionRequest solicitud) {
        return http.get(
                ub -> appendProgramacionQuery(ub.path(PATH_PROGRAMACIONES), solicitud).build(),
                null,
                JsonNode.class
        );
    }

    public JsonNode buscarCertificados(BusquedaCertificadoRequest solicitud) {
        return http.get(
                ub -> appendCertificadoQuery(ub.path(PATH_CERTIFICADOS), solicitud).build(),
                null,
                JsonNode.class
        );
    }

    public JsonNode obtenerDetalleCertificado(Long idCertificado) {
        return http.get(
                PATH_CERTIFICADOS + "/{idCertificado}",
                null,
                JsonNode.class,
                idCertificado
        );
    }

    private static UriBuilder appendProgramacionQuery(UriBuilder ub, BusquedaProgramacionRequest r) {
        queryParamIfPresent(ub, "idDepartamento", r.idDepartamento());
        queryParamIfPresent(ub, "idLocal", r.idLocal());
        queryParamIfPresent(ub, "direccion", r.direccion());
        queryParamIfPresent(ub, "columnaOrden", r.columnaOrden());
        queryParamIfPresent(ub, "direccionOrden", r.direccionOrden());
        queryParamIfPresent(ub, "pagina", r.pagina());
        queryParamIfPresent(ub, "tamano", r.tamano());
        return ub;
    }

    private static UriBuilder appendCertificadoQuery(UriBuilder ub, BusquedaCertificadoRequest r) {
        queryParamIfPresent(ub, "criterio", r.criterio());
        queryParamIfPresent(ub, "nroCertificado", r.nroCertificado());
        queryParamIfPresent(ub, "tipoDocumento", r.tipoDocumento());
        queryParamIfPresent(ub, "nroDocumento", r.nroDocumento());
        queryParamIfPresent(ub, "nombres", r.nombres());
        queryParamIfPresent(ub, "apellidos", r.apellidos());
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
}
