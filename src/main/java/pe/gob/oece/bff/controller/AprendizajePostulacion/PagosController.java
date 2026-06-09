package pe.gob.oece.bff.controller.AprendizajePostulacion;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.oece.bff.application.AprendizajePostulacion.PagosService;
import pe.gob.oece.bff.config.ApiPaths;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.NiubizCallbackRequest;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.RespuestaSimple;
import pe.gob.oece.bff.shared.ApiWrapper;
import pe.gob.oece.bff.shared.HttpRequestUtils;

@RestController
@RequestMapping(ApiPaths.NIUBIZ_WEBHOOK)
@Tag(name = "Pagos", description = "Webhooks de pasarelas de pago (Niubiz)")
public class PagosController {

    private final PagosService pagosService;

    public PagosController(PagosService pagosService) {
        this.pagosService = pagosService;
    }

    @GetMapping
    @Operation(summary = "Webhook callback Niubiz (GET)")
    public ApiWrapper<RespuestaSimple> callbackNiubiz(
            @RequestParam String purchaseNumber,
            @RequestParam String status,
            @RequestParam(required = false) String motivoDenegacion,
            @RequestParam(required = false) String currency,
            @RequestParam(required = false) String transactionDate,
            @RequestParam(required = false) String montoAprobado,
            HttpServletRequest httpRequest
    ) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        RespuestaSimple data = pagosService.callbackNiubiz(
                purchaseNumber,
                status,
                motivoDenegacion,
                currency,
                transactionDate,
                montoAprobado,
                ipOrigen
        );
        return ApiWrapper.success(data, "Callback Niubiz procesado", httpRequest.getRequestURI());
    }

    @PostMapping
    @Operation(summary = "Webhook callback Niubiz (POST)")
    public ApiWrapper<RespuestaSimple> callbackNiubizPost(
            @RequestBody(required = false) NiubizCallbackRequest body,
            @RequestParam(required = false) String purchaseNumber,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String motivoDenegacion,
            @RequestParam(required = false) String transactionDate,
            @RequestParam(required = false) String montoAprobado,
            HttpServletRequest httpRequest
    ) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        RespuestaSimple data = pagosService.callbackNiubizPost(
                body,
                purchaseNumber,
                status,
                motivoDenegacion,
                transactionDate,
                montoAprobado,
                ipOrigen
        );
        return ApiWrapper.success(data, "Callback Niubiz procesado", httpRequest.getRequestURI());
    }
}
