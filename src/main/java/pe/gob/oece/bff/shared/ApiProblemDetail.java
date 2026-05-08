package pe.gob.oece.bff.shared;

public record ApiProblemDetail(
    String type,
    String title,
    int status,
    String detail,
    String instance,
    String correlationId
) {
}