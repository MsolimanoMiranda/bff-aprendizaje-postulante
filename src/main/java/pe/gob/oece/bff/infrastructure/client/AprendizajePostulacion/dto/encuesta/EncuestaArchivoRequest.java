package pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.encuesta;

import jakarta.validation.constraints.NotBlank;

public record EncuestaArchivoRequest(
        @NotBlank
        String nombreArchivo,

        @NotBlank
        String tipoMime,

        @NotBlank
        String contenidoBase64
) {
}
