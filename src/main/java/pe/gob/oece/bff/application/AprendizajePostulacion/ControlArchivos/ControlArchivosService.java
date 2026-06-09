package pe.gob.oece.bff.application.AprendizajePostulacion.ControlArchivos;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pe.gob.oece.bff.infrastructure.client.ControlArchivos.ControlArchivosClient;
import pe.gob.oece.bff.infrastructure.client.ControlArchivos.dto.*;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ControlArchivosService {

    private final ControlArchivosClient controlArchivosClient;

    public ListarEstructuraResponse listarEstructura(
            String codConfiguracion,
            Boolean incluirDocumento,
            Boolean incluirMetadata
    ) {
        return controlArchivosClient.listarEstructura(
                codConfiguracion,
                incluirDocumento,
                incluirMetadata
        );
    }

    public ValidarUuidResponse validarUuid(String uuid) {
        return controlArchivosClient.validarUuid(uuid);
    }

    public PermisoAsignadoResponse asignarPermiso(PermisoDocumentoRequest request) {
        return controlArchivosClient.asignarPermiso(request);
    }

    public QuitarPermisoResponse quitarPermiso(PermisoDocumentoRequest request) {
        return controlArchivosClient.quitarPermiso(request);
    }

    public CrearExpedienteResponse crearExpediente(CrearExpedienteRequest request) {
        return controlArchivosClient.crearExpediente(request);
    }

    public CrearCarpetaResponse crearCarpetaV2(CrearCarpetaV2Request request) {
        return controlArchivosClient.crearCarpetaV2(request);
    }

    public ValidarCarpetaV2Response validarCarpetaV2(ValidarCarpetaV2Request request) {
        return controlArchivosClient.validarCarpetaV2(request);
    }

    public ArchivoRegistradoResponse insertarArchivo(
            MultipartFile archivo,
            InsertarArchivoRequest request
    ) {
        return controlArchivosClient.insertarArchivo(
                toArchivoMultipart(archivo),
                request
        );
    }

    public ArchivoRegistradoResponse insertarArchivoConToken(
            MultipartFile archivo,
            InsertarArchivoRequest request
    ) {
        return controlArchivosClient.insertarArchivoConToken(
                toArchivoMultipart(archivo),
                request
        );
    }

    public ArchivoRegistradoResponse actualizarArchivo(
            MultipartFile archivo,
            ActualizarArchivoRequest request
    ) {
        return controlArchivosClient.actualizarArchivo(
                toArchivoMultipart(archivo),
                request
        );
    }

    public byte[] descargarArchivo(String uuid, String nombre) {
        return controlArchivosClient.descargarArchivo(uuid, nombre);
    }

    public byte[] descargarArchivoConToken(String uuid, String nombre) {
        return controlArchivosClient.descargarArchivoConToken(uuid, nombre);
    }

    /**
     * Flujo útil: valida si la carpeta existe; si existe, retorna la validación.
     * Si no existe, crea la carpeta y luego vuelve a validarla para obtener datos completos.
     */
    public ValidarCarpetaV2Response validarOCrearCarpetaV2(CrearCarpetaV2Request request) {
        ValidarCarpetaV2Request validarRequest = ValidarCarpetaV2Request.builder()
                .codigoConfiguracion(request.getCodigoConfiguracion())
                .codigoCarpeta(request.getCodigoCarpeta())
                .nombreCarpeta(request.getNombreCarpeta())
                .uuidCarpetaPadre(request.getUuidCarpetaPadre())
                .build();

        ValidarCarpetaV2Response validacion = controlArchivosClient.validarCarpetaV2(validarRequest);

        if (Boolean.TRUE.equals(validacion.getExiste())) {
            return validacion;
        }

        controlArchivosClient.crearCarpetaV2(request);

        return controlArchivosClient.validarCarpetaV2(validarRequest);
    }

    private ArchivoMultipart toArchivoMultipart(MultipartFile multipartFile) {
        try {
            return new ArchivoMultipart(
                    resolveFileName(multipartFile),
                    resolveContentType(multipartFile),
                    multipartFile.getBytes()
            );
        } catch (IOException e) {
            throw new IllegalArgumentException("No se pudo leer el archivo multipart", e);
        }
    }

    private String resolveFileName(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();

        if (originalFilename == null || originalFilename.isBlank()) {
            return "archivo";
        }

        return originalFilename;
    }

    private String resolveContentType(MultipartFile multipartFile) {
        String contentType = multipartFile.getContentType();

        if (contentType == null || contentType.isBlank()) {
            return "application/octet-stream";
        }

        return contentType;
    }
}