package pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto;

public record BusquedaCertificadoPostulanteRequest(
        Long tipoDocumento,
        String nDocumento,
        String apellidoPaterno,
        String apellidoMaterno,
        String nombres,
        String estadoCert,
        Long idNivel,
        Integer pagina,
        Integer tamano
) {
}
