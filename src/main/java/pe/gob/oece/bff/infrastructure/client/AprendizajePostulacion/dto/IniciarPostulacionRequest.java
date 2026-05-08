package pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto;

public record IniciarPostulacionRequest(
        Boolean aceptaTerminos,
        String fechaHoraAceptacion
) {
}
