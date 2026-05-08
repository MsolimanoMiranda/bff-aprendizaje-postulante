package pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto;

public record BusquedaCertificadoRequest(
        String criterio,
        String nroCertificado,
        String tipoDocumento,
        String nroDocumento,
        String nombres,
        String apellidos,
        Integer pagina,
        Integer tamano
) {
}
