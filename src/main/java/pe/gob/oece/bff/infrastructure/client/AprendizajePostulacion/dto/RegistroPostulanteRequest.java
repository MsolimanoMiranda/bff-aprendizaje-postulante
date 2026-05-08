package pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto;

public record RegistroPostulanteRequest(
        Long idTipoDocumento,
        String nroDocumento,
        String apellidoPaterno,
        String apellidoMaterno,
        String nombres,
        String email,
        String emailRecuperacion,
        String telefono,
        Boolean aceptaTerminos
) {
}
