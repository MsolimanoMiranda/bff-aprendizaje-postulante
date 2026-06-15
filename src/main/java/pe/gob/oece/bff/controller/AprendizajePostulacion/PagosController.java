package pe.gob.oece.bff.controller.AprendizajePostulacion;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.oece.bff.application.AprendizajePostulacion.PagosService;
import pe.gob.oece.bff.config.ApiPaths;
import pe.gob.oece.bff.shared.ApiWrapper;
import pe.gob.oece.bff.shared.HttpRequestUtils;

@RestController
@Tag(name = "Pagos", description = "Tasa SICAN y webhooks de pasarelas de pago (Niubiz)")
public class PagosController {

    private final PagosService pagosService;

    public PagosController(PagosService pagosService) {
        this.pagosService = pagosService;
    }

    // ── Tasa de pago ─────────────────────────────────────────────────────────

    @GetMapping(ApiPaths.PAGOS + "/tasa")
    @Operation(
        summary = "Obtener tasa de pago SICAN",
        description = "Retorna descripcion, monto, codigoCajaTupa y codigoPlan desde Pagos Electrónicos OECE.",
        security = @SecurityRequirement(name = "bearer-jwt")
    )
    public ApiWrapper<JsonNode> obtenerTasa(
            @RequestHeader("Authorization") String authorizationHeader,
            HttpServletRequest httpRequest
    ) {
        String bearerToken = authorizationHeader.startsWith("Bearer ")
                ? authorizationHeader.substring(7)
                : authorizationHeader;

        JsonNode data = pagosService.obtenerTasa(bearerToken, HttpRequestUtils.obtenerIpOrigen(httpRequest));
        return ApiWrapper.success(data, "Tasa de pago SICAN", httpRequest.getRequestURI());
    }

    // ── Niubiz webhook GET ────────────────────────────────────────────────────

    @GetMapping(ApiPaths.NIUBIZ_WEBHOOK)
    @Operation(
        summary = "Webhook callback Niubiz (GET)",
        description = "Niubiz llama a este endpoint al completar el pago. " +
                      "postulacion e idPostulante son obligatorios para identificar la operación.",
        security = @SecurityRequirement(name = "bearer-jwt")
    )
    public ApiWrapper<JsonNode> callbackNiubiz(
            @RequestParam("postulacion")                        Long   idPostulacion,
            @RequestParam("purchaseNumber")                     String purchaseNumber,
            @RequestParam("status")                             String status,
            @RequestParam(value = "motivoDenegacion", required = false) String motivoDenegacion,
            @RequestParam(value = "currency",         required = false) String currency,
            @RequestParam(value = "transactionDate",  required = false) String transactionDate,
            @RequestParam(value = "montoAprobado",    required = false) String montoAprobado,
            @RequestParam(value = "trama",            required = false) String trama,
            HttpServletRequest httpRequest
    ) {
        JsonNode data = pagosService.callbackNiubiz(
                idPostulacion, purchaseNumber, status,
                motivoDenegacion, currency, transactionDate, montoAprobado, trama,
                HttpRequestUtils.obtenerIpOrigen(httpRequest)
        );
        return ApiWrapper.success(data, "Callback Niubiz procesado", httpRequest.getRequestURI());
    }

    // ── Niubiz webhook POST ───────────────────────────────────────────────────

    @PostMapping(ApiPaths.NIUBIZ_WEBHOOK)
    @Operation(
        summary = "Webhook callback Niubiz (POST)",
        security = @SecurityRequirement(name = "bearer-jwt")
    )
    public ApiWrapper<JsonNode> callbackNiubizPost(
            @RequestParam("postulacion")                              Long   idPostulacion,
            @RequestParam(value = "purchaseNumber",   required = false) String purchaseNumber,
            @RequestParam(value = "status",           required = false) String status,
            @RequestParam(value = "motivoDenegacion", required = false) String motivoDenegacion,
            @RequestParam(value = "transactionDate",  required = false) String transactionDate,
            @RequestParam(value = "montoAprobado",    required = false) String montoAprobado,
            @RequestParam(value = "trama",            required = false) String trama,
            HttpServletRequest httpRequest
    ) {
        JsonNode data = pagosService.callbackNiubizPost(
                idPostulacion, purchaseNumber, status,
                motivoDenegacion, transactionDate, montoAprobado, trama,
                HttpRequestUtils.obtenerIpOrigen(httpRequest)
        );
        return ApiWrapper.success(data, "Callback Niubiz procesado", httpRequest.getRequestURI());
    }
}