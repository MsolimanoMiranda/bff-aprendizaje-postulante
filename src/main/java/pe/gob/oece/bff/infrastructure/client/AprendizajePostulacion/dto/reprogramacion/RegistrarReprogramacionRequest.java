package pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.reprogramacion;

import jakarta.validation.constraints.NotNull;

public record RegistrarReprogramacionRequest(
        @NotNull(message = "La solicitud de reprogramacion es obligatoria")
        Long idSolicitudReprog,
        @NotNull(message = "La nueva programacion es obligatoria")
        Long idProgExamenNueva
) {
}
