package pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.AlfrescoArchivoDownloadResponse;
import pe.gob.oece.bff.infrastructure.client.DownstreamClientErrorHandler;
import pe.gob.oece.bff.infrastructure.client.DownstreamWebClient;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.ActualizarCertificadoRequest;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.BusquedaCertificadoPostulanteRequest;
import pe.gob.oece.bff.shared.ApiWrapper;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CertificacionClient {
    private static final Logger logger = LoggerFactory.getLogger(CertificacionClient.class);
    private final DownstreamWebClient http;
    private static final String BASE_PATH = "/api/v1/certificado";
    private static final String HEADER_POSTULANTE_ID = "X-Postulante-Id";
    public CertificacionClient(
            @Qualifier("aprendizajePostulacionWebClient") WebClient webClient,
            @Value("${app.clients.aprendizaje-postulacion.response-timeout-ms:8000}") long timeoutMs
    ) {
        this.http = new DownstreamWebClient(
                webClient,
                Duration.ofMillis(timeoutMs),
                new DownstreamClientErrorHandler("aprendizaje-postulacion", "ERROR-AP-PAG-EX")
        );
    }


    public JsonNode misCertificados(Long idPostulante, String authorization) {
        logger.debug("GET {} idPostulante={}", BASE_PATH + "/"+idPostulante+"/mis-certificaciones", idPostulante);
        Map<String, Object> queryParams = new HashMap<>();
        if (idPostulante != null && idPostulante!=0) {
            queryParams.put("idPostulante", idPostulante);
        }

        return http.get(
                BASE_PATH + "/{idPostulacion}/mis-certificaciones",
                authorizationHeader(authorization),
                JsonNode.class,
                idPostulante
        );

    }


    public byte[] descargarTodos(Long idPostulante, String authorization) {
        logger.debug("GET {} idPostulante={}", BASE_PATH + "/"+idPostulante+"/descargar-todos", idPostulante);
        Map<String, Object> queryParams = new HashMap<>();
        if (idPostulante != null && idPostulante!=0) {
            queryParams.put("idPostulante", idPostulante);
        }

        return http.get(
                BASE_PATH + "/{idPostulacion}/descargar-todos",
                authorizationHeader(authorization),
                byte[].class,
                idPostulante
        );

    }


    public byte[] descargarArchivoAlfresco(Long idCertificado, String authorization) {
        logger.debug("GET {} idCertificado={}", BASE_PATH + "/"+idCertificado +"/alfresco/archivo", idCertificado);
        Map<String, Object> queryParams = new HashMap<>();
        if (idCertificado != null && idCertificado!=0) {
            queryParams.put("idCertificado", idCertificado);
        }

        return http.get(
                BASE_PATH + "/{idCertificado}/alfresco/archivo",
                authorizationHeader(authorization),
                byte[].class,
                idCertificado
        );

    }

    public JsonNode buscar(BusquedaCertificadoPostulanteRequest solicitud) {
        logger.debug("GET {}", BASE_PATH + "/buscar");
        return http.get(
                ub -> appendBuscarQuery(ub.path(BASE_PATH + "/buscar"), solicitud).build(),
                null,
                JsonNode.class
        );
    }

    public JsonNode actualizar(Long idCertificado, ActualizarCertificadoRequest solicitud, String authorization) {
        logger.debug("PUT {} idCertificado={}", BASE_PATH + "/{idCertificado}", idCertificado);
        return http.put(
                BASE_PATH + "/{idCertificado}",
                solicitud,
                authorizationHeader(authorization),
                JsonNode.class,
                idCertificado
        );
    }

    public JsonNode obtenerDatosDescargaPorExamen(Long idExamen, String authorization) {
        logger.debug("GET {} idExamen={}", BASE_PATH + "/examen/{idExamen}/datos-descarga", idExamen);
        return http.get(
                BASE_PATH + "/examen/{idExamen}/datos-descarga",
                authorizationHeader(authorization),
                JsonNode.class,
                idExamen
        );
    }

    public byte[] descargarArchivoPorExamen(Long idExamen, String authorization) {
        logger.debug("GET {} idExamen={}", BASE_PATH + "/examen/{idExamen}/archivo", idExamen);
        return http.get(
                BASE_PATH + "/examen/{idExamen}/archivo",
                authorizationHeader(authorization),
                byte[].class,
                idExamen
        );
    }

    private Map<String, String> postulanteHeader(Long postulanteId) {
        return Map.of(HEADER_POSTULANTE_ID, String.valueOf(postulanteId));
    }

    private Map<String, String> authorizationHeader(String authorization) {
        if (authorization == null || authorization.isBlank()) {
            return null;
        }
        return Map.of(HttpHeaders.AUTHORIZATION, authorization);
    }

    private static UriBuilder appendBuscarQuery(UriBuilder ub, BusquedaCertificadoPostulanteRequest r) {
        queryParamIfPresent(ub, "tipoDocumento", r.tipoDocumento());
        queryParamIfPresent(ub, "nDocumento", r.nDocumento());
        queryParamIfPresent(ub, "apellidoPaterno", r.apellidoPaterno());
        queryParamIfPresent(ub, "apellidoMaterno", r.apellidoMaterno());
        queryParamIfPresent(ub, "nombres", r.nombres());
        queryParamIfPresent(ub, "estadoCert", r.estadoCert());
        queryParamIfPresent(ub, "idNivel", r.idNivel());
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
