package pe.gob.oece.bff.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.server.ResponseStatusException;
import pe.gob.oece.bff.domain.DomainException;
import pe.gob.oece.bff.domain.NotFoundException;
import pe.gob.oece.bff.shared.ApiProblemDetail;
import pe.gob.oece.bff.shared.CorrelationIdContext;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiProblemDetail> handleNotFound(NotFoundException ex, WebRequest request) {
        return buildProblem(HttpStatus.NOT_FOUND, "NOT-FOUND", ex.getMessage(), request);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiProblemDetail> handleNoResource(NoResourceFoundException ex, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        return buildProblem(HttpStatus.NOT_FOUND, "NOT-FOUND", "Servicio no encontrado: " + path, request);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiProblemDetail> handleDomain(DomainException ex, WebRequest request) {
        return buildProblem(HttpStatus.BAD_REQUEST, ex.getCode(), resolveDomainDetail(ex.getMessage()), request);
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<ApiProblemDetail> handleWebClientResponse(WebClientResponseException ex, WebRequest request) {
        HttpStatus mapped = mapUpstreamStatus(ex.getStatusCode().value());
        String code = mapped.is5xxServerError() ? "UPSTREAM-ERROR" : "UPSTREAM-CLIENT-ERROR";
        String detail = resolveDomainDetail(ex.getResponseBodyAsString());
        if (detail == null || detail.isBlank()) {
            detail = "Error en el servicio invocado (" + ex.getStatusCode().value() + ").";
        }
        logger.warn("Upstream {} → {} : {}", ex.getRequest() != null ? ex.getRequest().getURI() : "?",
                ex.getStatusCode().value(), detail);
        return buildProblem(mapped, code, detail, request);
    }

    @ExceptionHandler(WebClientRequestException.class)
    public ResponseEntity<ApiProblemDetail> handleWebClientRequest(WebClientRequestException ex, WebRequest request) {
        Throwable root = ex.getMostSpecificCause();
        if (root != null && root.getClass().getSimpleName().contains("ReadTimeoutException")) {
            return buildProblem(
                    HttpStatus.GATEWAY_TIMEOUT,
                    "UPSTREAM-TIMEOUT",
                    "El servicio tardó demasiado en responder. Intente nuevamente.",
                    request
            );
        }
        return buildProblem(
                HttpStatus.BAD_GATEWAY,
                "UPSTREAM-ERROR",
                "No se pudo completar la comunicación con el servicio.",
                request
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiProblemDetail> handleValidation(MethodArgumentNotValidException ex, WebRequest request) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return buildProblem(HttpStatus.BAD_REQUEST, "VALIDATION-ERROR", errors, request);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ApiProblemDetail> handleMissingHeader(MissingRequestHeaderException ex, WebRequest request) {
        return buildProblem(
                HttpStatus.BAD_REQUEST,
                "MISSING-HEADER",
                "Falta el encabezado requerido: " + ex.getHeaderName(),
                request
        );
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiProblemDetail> handleMissingParam(MissingServletRequestParameterException ex, WebRequest request) {
        return buildProblem(
                HttpStatus.BAD_REQUEST,
                "MISSING-PARAMETER",
                "Falta el parámetro requerido: " + ex.getParameterName(),
                request
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiProblemDetail> handleTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        return buildProblem(
                HttpStatus.BAD_REQUEST,
                "INVALID-PARAMETER",
                "Parámetro inválido: " + ex.getName(),
                request
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiProblemDetail> handleNotReadable(HttpMessageNotReadableException ex, WebRequest request) {
        return buildProblem(
                HttpStatus.BAD_REQUEST,
                "MALFORMED-REQUEST",
                "El cuerpo de la solicitud no es válido o está malformado.",
                request
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiProblemDetail> handleAccessDenied(AccessDeniedException ex, WebRequest request) {
        return buildProblem(HttpStatus.FORBIDDEN, "FORBIDDEN", "No tiene permisos para acceder a este recurso", request);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiProblemDetail> handleResponseStatus(ResponseStatusException ex, WebRequest request) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        String code = switch (status) {
            case UNAUTHORIZED -> "UNAUTHORIZED";
            case FORBIDDEN -> "FORBIDDEN";
            case NOT_FOUND -> "NOT-FOUND";
            default -> "REQUEST-ERROR";
        };
        return buildProblem(status, code, ex.getReason(), request);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiProblemDetail> handleMaxUploadSize(MaxUploadSizeExceededException ex, WebRequest request) {
        logger.warn("Multipart rechazado por tamaño: {}", ex.getMessage());
        return buildProblem(
                HttpStatus.PAYLOAD_TOO_LARGE,
                "FILE-TOO-LARGE",
                "El archivo supera el tamaño máximo permitido.",
                request
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiProblemDetail> handleGeneric(Exception ex, WebRequest request) {
        logger.error("Error inesperado: {}", ex.getMessage(), ex);
        return buildProblem(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL-ERROR", "Ha ocurrido un error inesperado", request);
    }

    private ResponseEntity<ApiProblemDetail> buildProblem(
            HttpStatus status,
            String code,
            String detail,
            WebRequest request
    ) {
        return ResponseEntity.status(status).body(
                new ApiProblemDetail(
                        "https://oece.gob.pe/errors/" + code,
                        resolveTitle(status),
                        status.value(),
                        detail,
                        request.getDescription(false).replace("uri=", ""),
                        CorrelationIdContext.getCorrelationId()
                )
        );
    }

    private HttpStatus mapUpstreamStatus(int upstreamStatus) {
        if (upstreamStatus == HttpStatus.UNAUTHORIZED.value()) {
            return HttpStatus.UNAUTHORIZED;
        }
        if (upstreamStatus == HttpStatus.FORBIDDEN.value()) {
            return HttpStatus.FORBIDDEN;
        }
        if (upstreamStatus == HttpStatus.REQUEST_TIMEOUT.value()) {
            return HttpStatus.REQUEST_TIMEOUT;
        }
        if (upstreamStatus >= 500) {
            return HttpStatus.BAD_GATEWAY;
        }
        return HttpStatus.BAD_GATEWAY;
    }

    private String resolveTitle(HttpStatus status) {
        return switch (status) {
            case NOT_FOUND -> "Recurso no encontrado";
            case BAD_REQUEST -> "Solicitud inválida";
            case UNAUTHORIZED -> "No autenticado";
            case FORBIDDEN -> "Acceso denegado";
            case PAYLOAD_TOO_LARGE -> "Archivo demasiado grande";
            case GATEWAY_TIMEOUT -> "Tiempo de espera agotado";
            case BAD_GATEWAY -> "Error de servicio intermedio";
            case INTERNAL_SERVER_ERROR -> "Error interno del servidor";
            default -> "Error de aplicación";
        };
    }

    private static final List<String> DETAIL_CANDIDATE_FIELDS = List.of(
            "detail",
            "detalle",
            "message",
            "mensaje",
            "errors",
            "error",
            "title",
            "titulo"
    );

    private String resolveDomainDetail(String rawMessage) {
        if (rawMessage == null || rawMessage.isBlank()) {
            return rawMessage;
        }
        try {
            JsonNode node = objectMapper.readTree(rawMessage);
            for (String field : DETAIL_CANDIDATE_FIELDS) {
                if (node.hasNonNull(field) && node.get(field).isTextual()) {
                    return node.get(field).asText();
                }
            }
        } catch (Exception ignored) {

        }
        return rawMessage;
    }
}
