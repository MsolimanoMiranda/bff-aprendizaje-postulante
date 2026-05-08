package pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto;

import java.util.List;

public record RespuestaSimple(
        Integer rpta,
        String mensaje,
        List<String> valores
) {
}
