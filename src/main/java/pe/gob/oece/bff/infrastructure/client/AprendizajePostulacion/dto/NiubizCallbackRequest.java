package pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto;

public record NiubizCallbackRequest(
        String purchaseNumber,
        String status,
        String motivoDenegacion,
        String currency,
        String transactionDate,
        String montoAprobado
) {
}
