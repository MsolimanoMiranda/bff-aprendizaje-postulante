package pe.gob.oece.bff.controller.AprendizajePostulacion;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.oece.bff.application.AprendizajePostulacion.ExpedienteService;
import pe.gob.oece.bff.config.ApiPaths;
import pe.gob.oece.bff.shared.ApiWrapper;
import pe.gob.oece.bff.shared.HttpRequestUtils;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiPaths.EXPEDIENTES)
@Tag(name = "Expedientes", description = "BFF para gestion de expedientes")
@SecurityRequirement(name = "bearer-jwt")
public class ExpedienteController {

    private final ExpedienteService expedienteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar expediente")
    public ApiWrapper<JsonNode> registrarExpediente(
            @Valid @RequestBody JsonNode request,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization,
            HttpServletRequest httpRequest
    ) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        JsonNode data = expedienteService.registrarExpediente(request, authorization, ipOrigen);
        return ApiWrapper.created(data, "Expediente registrado", httpRequest.getRequestURI());
    }

    @PostMapping("/cargo/generar")
    @Operation(summary = "Generar numero de cargo")
    public ApiWrapper<JsonNode> generarCargo(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization,
            HttpServletRequest httpRequest
    ) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        JsonNode data = expedienteService.generarCargo(authorization, ipOrigen);
        return ApiWrapper.success(data, "Cargo generado", httpRequest.getRequestURI());
    }
}
