package pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto;

public record BusquedaProgramacionRequest(
        Long idDepartamento,
        Long idLocal,
        String direccion,
        String columnaOrden,
        String direccionOrden,
        Integer pagina,
        Integer tamano
) {
}
