package pe.gob.oece.bff.controller.AprendizajePostulacion;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.oece.bff.application.AprendizajePostulacion.EncuestaSatisfaccion.EncuestaSatisfaccionCommandService;
import pe.gob.oece.bff.application.AprendizajePostulacion.EncuestaSatisfaccion.EncuestaSatisfaccionQueryService;
import pe.gob.oece.bff.config.ApiPaths;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.encuesta.EncuestaRespuestaRequest;
import pe.gob.oece.bff.shared.ApiWrapper;
import pe.gob.oece.bff.shared.HttpRequestUtils;

import static pe.gob.oece.bff.controller.helpers.JwtControllerHelper.obtenerIdUsuarioGu;

@RestController
@RequestMapping(ApiPaths.ENCUESTA_SATISFACCION)
@Tag(name = "Encuesta de satisfaccion", description = "BFF de encuesta de satisfaccion del examen")
@SecurityRequirement(name = "bearer-jwt")
public class EncuestaSatisfaccionController {

    private final EncuestaSatisfaccionQueryService encuestaSatisfaccionQueryService;
    private final EncuestaSatisfaccionCommandService encuestaSatisfaccionCommandService;

    public EncuestaSatisfaccionController(
            EncuestaSatisfaccionQueryService encuestaSatisfaccionQueryService,
            EncuestaSatisfaccionCommandService encuestaSatisfaccionCommandService
    ) {
        this.encuestaSatisfaccionQueryService = encuestaSatisfaccionQueryService;
        this.encuestaSatisfaccionCommandService = encuestaSatisfaccionCommandService;
    }

    @GetMapping("/formulario-activo")
    @Operation(summary = "Obtener formulario activo de satisfaccion del examen")
    public ApiWrapper<JsonNode> obtenerFormularioActivo(HttpServletRequest httpRequest) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        JsonNode data = encuestaSatisfaccionQueryService.obtenerFormularioActivo(ipOrigen);
        return ApiWrapper.success(data, "Formulario activo de encuesta de satisfaccion", httpRequest.getRequestURI());
    }

    @PostMapping("/respuestas")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar respuestas de satisfaccion del examen")
    public ApiWrapper<JsonNode> registrarRespuesta(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody EncuestaRespuestaRequest request,
            HttpServletRequest httpRequest
    ) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        Long idUsuarioGu = obtenerIdUsuarioGu(jwt);
        System.out.println("idUsuarioGu" + idUsuarioGu);
        System.out.println("request" + request.toString());
        JsonNode data = encuestaSatisfaccionCommandService.registrarRespuesta(idUsuarioGu, request, ipOrigen);
        return ApiWrapper.created(data, "Encuesta de satisfaccion registrada", httpRequest.getRequestURI());
    }

}
