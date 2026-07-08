package pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto;

import java.time.LocalDate;

public record ActualizarCertificadoRequest(
        LocalDate fechaEmision,
        LocalDate vigenciaHasta,
        Long idCertReemplazado,
        String justificacion,
        Long idNulidad
) {
}
