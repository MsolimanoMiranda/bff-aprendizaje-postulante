package pe.gob.oece.bff.infrastructure.client.EjecucionExamen.dto;

import jakarta.validation.constraints.NotNull;

public record ValidarTokenExamenRequest(
        @NotNull Long idInscripcion,
        @NotNull String token
) {
}
