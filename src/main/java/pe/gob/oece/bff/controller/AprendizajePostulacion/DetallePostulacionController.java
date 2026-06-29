package pe.gob.oece.bff.controller.AprendizajePostulacion;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.oece.bff.application.AprendizajePostulacion.DetallePostulacion.DetallePostulacionQueryService;
import pe.gob.oece.bff.config.ApiPaths;
import pe.gob.oece.bff.shared.ApiWrapper;
import pe.gob.oece.bff.shared.HttpRequestUtils;

import static pe.gob.oece.bff.controller.helpers.JwtControllerHelper.obtenerToken;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(ApiPaths.DETALLE_POSTULACION)
@Tag(name = "Detalle postulacion", description = "BFF HU-POS-013 - Ver detalle de postulacion")
@SecurityRequirement(name = "bearer-jwt")
public class DetallePostulacionController {

    private final DetallePostulacionQueryService detallePostulacionQueryService;

    public DetallePostulacionController(DetallePostulacionQueryService detallePostulacionQueryService) {
        this.detallePostulacionQueryService = detallePostulacionQueryService;
    }

    @GetMapping("/{idPostulacion}/detalle")
    @Operation(summary = "Obtener detalle de postulacion")
    public ApiWrapper<JsonNode> obtenerDetalle(
            @PathVariable Long idPostulacion,
            @AuthenticationPrincipal Jwt jwt,
            HttpServletRequest httpRequest
    ) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        String token = obtenerToken(jwt);
        JsonNode data = detallePostulacionQueryService.obtenerDetalle(idPostulacion, token, ipOrigen);
        return ApiWrapper.success(data, "Detalle de postulacion", httpRequest.getRequestURI());
    }

}
