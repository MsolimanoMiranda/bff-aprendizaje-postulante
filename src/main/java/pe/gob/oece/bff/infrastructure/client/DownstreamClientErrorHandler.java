package pe.gob.oece.bff.infrastructure.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import pe.gob.oece.bff.domain.DomainException;
import pe.gob.oece.bff.domain.NotFoundException;
import reactor.core.publisher.Mono;

import java.util.List;

public final class DownstreamClientErrorHandler {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final List<String> DETAIL_CANDIDATE_FIELDS = List.of(
            "detail",
            "detalle",
            "message",
            "mensaje",
            "error",
            "title",
            "titulo"
    );

    private final String serviceName;
    private final String errorCodePrefix;

    public DownstreamClientErrorHandler(String serviceName, String errorCodePrefix) {
        this.serviceName = serviceName;
        this.errorCodePrefix = errorCodePrefix;
    }

    public Mono<? extends Throwable> toError(ClientResponse response) {
        HttpStatusCode status = response.statusCode();
        return response.bodyToMono(String.class)
                .defaultIfEmpty("")
                .map(body -> mapError(status, body));
    }

    public RuntimeException mapError(HttpStatusCode status, String body) {
        if (status.value() == 404) {
            return new NotFoundException(resolveErrorDetail(
                    body,
                    "Recurso no encontrado en el servicio " + serviceName
            ));
        }
        if (status.value() == 422 || status.value() == 409 || status.value() == 400) {
            return new DomainException(errorCodePrefix + status.value(), body);
        }
        return new WebClientResponseException(
                status.value(),
                "Error en servicio " + serviceName,
                null,
                body == null ? new byte[0] : body.getBytes(),
                null
        );
    }

    private String resolveErrorDetail(String body, String fallback) {
        if (body == null || body.isBlank()) {
            return fallback;
        }

        try {
            JsonNode node = OBJECT_MAPPER.readTree(body);
            for (String field : DETAIL_CANDIDATE_FIELDS) {
                if (node.hasNonNull(field) && node.get(field).isTextual()) {
                    String value = node.get(field).asText();
                    if (!value.isBlank()) {
                        return value;
                    }
                }
            }
        } catch (Exception ignored) {

        }

        return body;
    }
}
