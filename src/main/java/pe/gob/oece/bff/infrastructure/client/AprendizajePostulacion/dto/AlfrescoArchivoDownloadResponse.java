package pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto;

public record AlfrescoArchivoDownloadResponse(
        String nombreArchivo,
        String contentType,
        byte[] contenido
) {
}
