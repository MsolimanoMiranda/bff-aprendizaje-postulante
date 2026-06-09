package pe.gob.oece.bff.infrastructure.client.ControlArchivos;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pe.gob.oece.bff.infrastructure.client.ControlArchivos.dto.*;
import pe.gob.oece.bff.infrastructure.client.DownstreamClientErrorHandler;
import pe.gob.oece.bff.infrastructure.client.DownstreamWebClient;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static pe.gob.oece.bff.config.ControlArchivosEndpoints.*;

@Component
public class ControlArchivosClient {

    private static final String HEADER_APP_NAME = "X-APP-NAME";
    private static final String HEADER_PUBLIC_KEY = "X-PUBLIC-KEY";
    private static final String HEADER_API_KEY = "X-API-KEY";
    private static final String BEARER_PREFIX = "Bearer ";

    private static final String MULTIPART_ARCHIVO = "archivo";
    private static final String MULTIPART_DATA = "data";

    private final DownstreamWebClient http;
    private final String appName;
    private final String publicKey;
    private final String apiKey;
    private final String jwtToken;

    public ControlArchivosClient(
            @Qualifier("controlArchivosWebClient") WebClient webClient,
            @Value("${app.clients.control-archivos.response-timeout-ms:30000}") long timeoutMs,
            @Value("${app.clients.control-archivos.app-name:CONFSEACE}") String appName,
            @Value("${app.clients.control-archivos.public-key}") String publicKey,
            @Value("${app.clients.control-archivos.api-key}") String apiKey,
            @Value("${app.clients.control-archivos.jwt-token:}") String jwtToken
    ) {
        this.http = new DownstreamWebClient(
                webClient,
                Duration.ofMillis(timeoutMs),
                new DownstreamClientErrorHandler("control-archivos", "ERROR-CA")
        );
        this.appName = appName;
        this.publicKey = publicKey;
        this.apiKey = apiKey;
        this.jwtToken = jwtToken;
    }

    public ListarEstructuraResponse listarEstructura(
            String codConfiguracion,
            Boolean incluirDocumento,
            Boolean incluirMetadata
    ) {
        return http.get(
                uriBuilder -> uriBuilder
                        .path(LISTAR_ESTRUCTURA)
                        .queryParam("codConfiguracion", codConfiguracion)
                        .queryParam("incluirDocumento", incluirDocumento)
                        .queryParam("incluirMetadata", incluirMetadata)
                        .build(),
                apiKeyHeaders(),
                ListarEstructuraResponse.class
        );
    }

    public ValidarUuidResponse validarUuid(String uuid) {
        return http.get(
                uriBuilder -> uriBuilder
                        .path(VALIDAR_UUID)
                        .queryParam("uuid", uuid)
                        .build(),
                apiKeyHeaders(),
                ValidarUuidResponse.class
        );
    }

    public PermisoAsignadoResponse asignarPermiso(PermisoDocumentoRequest request) {
        return http.post(
                ASIGNAR_PERMISO,
                request,
                apiKeyHeaders(),
                PermisoAsignadoResponse.class
        );
    }

    public QuitarPermisoResponse quitarPermiso(PermisoDocumentoRequest request) {
        return http.patch(
                QUITAR_PERMISO,
                request,
                apiKeyHeaders(),
                QuitarPermisoResponse.class
        );
    }

    public CrearExpedienteResponse crearExpediente(CrearExpedienteRequest request) {
        return http.post(
                CREAR_EXPEDIENTE,
                request,
                apiKeyHeaders(),
                CrearExpedienteResponse.class
        );
    }

    public LeerExpedienteResponse leerExpediente(LeerExpedienteRequest request) {
        return http.post(
                LEER_EXPEDIENTE,
                request,
                apiKeyHeaders(),
                LeerExpedienteResponse.class
        );
    }

    public CrearCarpetaResponse crearCarpetaV2(CrearCarpetaV2Request request) {
        return http.post(
                CREAR_CARPETA_V2,
                request,
                apiKeyHeaders(),
                CrearCarpetaResponse.class
        );
    }

    public ValidarCarpetaV2Response validarCarpetaV2(ValidarCarpetaV2Request request) {
        return http.post(
                VALIDAR_CARPETA_V2,
                request,
                apiKeyHeaders(),
                ValidarCarpetaV2Response.class
        );
    }

    public byte[] descargarArchivo(String uuid, String nombre) {
        return http.get(
                uriBuilder -> uriBuilder
                        .path(DESCARGAR_ARCHIVO)
                        .queryParam("uuid", uuid)
                        .queryParam("nombre", nombre)
                        .build(),
                apiKeyHeaders(),
                byte[].class
        );
    }

    public byte[] descargarArchivoConToken(String uuid, String nombre) {
        return http.get(
                uriBuilder -> uriBuilder
                        .path(DESCARGAR_ARCHIVO_TOKEN)
                        .queryParam("uuid", uuid)
                        .queryParam("nombre", nombre)
                        .build(),
                tokenHeaders(),
                byte[].class
        );
    }

    public ArchivoRegistradoResponse insertarArchivo(
            ArchivoMultipart archivo,
            InsertarArchivoRequest request
    ) {
        return http.postMultipart(
                INSERTAR_ARCHIVO,
                buildMultipartBody(archivo, request),
                apiKeyHeaders(),
                ArchivoRegistradoResponse.class
        );
    }

    public ArchivoRegistradoResponse insertarArchivoConToken(
            ArchivoMultipart archivo,
            InsertarArchivoRequest request
    ) {
        return http.postMultipart(
                INSERTAR_ARCHIVO_TOKEN,
                buildMultipartBody(archivo, request),
                tokenHeaders(),
                ArchivoRegistradoResponse.class
        );
    }

    public ArchivoRegistradoResponse actualizarArchivo(
            ArchivoMultipart archivo,
            ActualizarArchivoRequest request
    ) {
        return http.putMultipart(
                ACTUALIZAR_ARCHIVO,
                buildMultipartBody(archivo, request),
                apiKeyHeaders(),
                ArchivoRegistradoResponse.class
        );
    }

    private Map<String, String> apiKeyHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put(HEADER_APP_NAME, appName);
        headers.put(HEADER_PUBLIC_KEY, publicKey);
        headers.put(HEADER_API_KEY, apiKey);
        return headers;
    }

    private Map<String, String> tokenHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put(HEADER_APP_NAME, appName);
        headers.put(HEADER_PUBLIC_KEY, publicKey);
        headers.put(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwtToken);
        return headers;
    }

    private MultipartBodyBuilder buildMultipartBody(
            ArchivoMultipart archivo,
            Object data
    ) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        builder.part(MULTIPART_ARCHIVO, new ByteArrayResource(archivo.contenido()) {
                    @Override
                    public String getFilename() {
                        return archivo.nombreArchivo();
                    }
                })
                .contentType(resolveContentType(archivo.contentType()));

        builder.part(MULTIPART_DATA, data)
                .contentType(MediaType.APPLICATION_JSON);

        return builder;
    }

    private MediaType resolveContentType(String contentType) {
        if (contentType == null || contentType.isBlank()) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }

        return MediaType.parseMediaType(contentType);
    }
}