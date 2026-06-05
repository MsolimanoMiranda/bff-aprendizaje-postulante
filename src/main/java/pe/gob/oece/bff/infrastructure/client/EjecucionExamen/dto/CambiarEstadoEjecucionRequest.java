package pe.gob.oece.bff.infrastructure.client.EjecucionExamen.dto;

import jakarta.validation.constraints.NotNull;

public record CambiarEstadoEjecucionRequest(
        @NotNull Integer idEjecucion,
        @NotNull String nuevoEstado
) {
}
