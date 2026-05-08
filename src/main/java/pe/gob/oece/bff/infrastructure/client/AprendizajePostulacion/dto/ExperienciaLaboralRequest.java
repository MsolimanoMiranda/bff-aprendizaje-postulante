package pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto;

public record ExperienciaLaboralRequest(
        Long idTipoExperiencia,
        String entidad,
        String area,
        String cargo,
        String fechaInicio,
        String fechaFin,
        String trabajoActual,
        Long idRegimenLaboral,
        Long idNivelGobierno,
        String urlAdjunto
) {
}
