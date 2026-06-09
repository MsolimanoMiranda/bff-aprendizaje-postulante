package pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.reprogramacion;

import jakarta.validation.constraints.NotNull;

public record RegistrarReprogramacionRequest(
        @NotNull(message = "La postulacion es obligatoria")
        Long idPostulacion,
        @NotNull(message = "La nueva programacion es obligatoria")
        Long idProgExamenNueva
) {
}
