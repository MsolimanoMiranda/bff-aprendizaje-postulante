package pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.encuesta;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record EncuestaRespuestaDetalleRequest(
        @NotNull Long idPregunta,
        List<String> valores,
        @Valid EncuestaArchivoRequest archivo
) {
}
