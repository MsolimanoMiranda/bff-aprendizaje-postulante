package pe.gob.oece.bff.infrastructure.client.ControlArchivos.dto;

public record ArchivoMultipart (
    String nombreArchivo,
    String contentType,
    byte[] contenido
){}
