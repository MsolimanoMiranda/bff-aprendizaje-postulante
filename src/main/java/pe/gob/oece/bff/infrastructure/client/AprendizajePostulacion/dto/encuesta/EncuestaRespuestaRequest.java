package pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.encuesta;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record EncuestaRespuestaRequest(
        @NotNull Long idFormulario,
        @Valid @NotEmpty List<EncuestaRespuestaDetalleRequest> respuestas
) {
}
