package pe.gob.oece.bff.infrastructure.client.EjecucionExamen.dto;

import jakarta.validation.constraints.NotNull;

public record IniciarEjecucionExamenRequest(
        @NotNull Integer idExamen
) {
}
