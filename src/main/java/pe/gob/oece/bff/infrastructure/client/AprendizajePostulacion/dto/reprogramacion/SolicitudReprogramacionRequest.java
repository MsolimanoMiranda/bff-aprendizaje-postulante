package pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.reprogramacion;

public record SolicitudReprogramacionRequest(
        Long idInscripcion,
        Long idProgramacionNueva,
        String tipoMotivo,
        String descripcionSolicitud
) {
}
