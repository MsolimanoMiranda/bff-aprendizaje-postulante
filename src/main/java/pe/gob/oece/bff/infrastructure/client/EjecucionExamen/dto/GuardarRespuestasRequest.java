package pe.gob.oece.bff.infrastructure.client.EjecucionExamen.dto;

import jakarta.validation.constraints.NotNull;

public record GuardarRespuestasRequest(
        @NotNull Long idExamen,
        @NotNull String respuestasJson
) {
}
