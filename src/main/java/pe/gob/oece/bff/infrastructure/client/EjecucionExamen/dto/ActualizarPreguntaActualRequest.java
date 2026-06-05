package pe.gob.oece.bff.infrastructure.client.EjecucionExamen.dto;

import jakarta.validation.constraints.NotNull;

public record ActualizarPreguntaActualRequest(
        @NotNull Integer idEjecucion,
        @NotNull Integer idPregunta
) {
}
