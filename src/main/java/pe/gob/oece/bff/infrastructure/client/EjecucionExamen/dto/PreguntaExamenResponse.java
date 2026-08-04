package pe.gob.oece.bff.infrastructure.client.EjecucionExamen.dto;

import java.util.List;

public record PreguntaExamenResponse(
        Long id,
        String nombreCompetencia,
        String codigo,
        String enunciado,
        String tipoPregunta,
        String nivel,
        String estadoPregunta,
        Long tipoEvaluacionId,
        List<AlternativaExamenResponse> alternativas,
        String superNombreCompetencia
) {
}
