package pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.reprogramacion;

public record BusquedaProgramacionDisponibleRequest(
        Long idLocal,
        String fechaDesde,
        String fechaHasta,
        String columnaOrden,
        String direccionOrden,
        Integer pagina,
        Integer tamano
) {
}
