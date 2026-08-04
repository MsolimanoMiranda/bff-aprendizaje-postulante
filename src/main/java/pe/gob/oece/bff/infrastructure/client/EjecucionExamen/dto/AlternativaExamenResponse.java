package pe.gob.oece.bff.infrastructure.client.EjecucionExamen.dto;

public record AlternativaExamenResponse(
        Long id,
        String contenido,
        String columnaPar,
        Long idPar
) {
}
