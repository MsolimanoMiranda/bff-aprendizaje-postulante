package pe.gob.oece.bff.controller.EjecucionExamen;

import com.fasterxml.jackson.databind.JsonNode;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.oece.bff.application.AprendizajePostulacion.ExamenTesteo.ExamenTesteoService;
import pe.gob.oece.bff.config.ApiPaths;
import pe.gob.oece.bff.shared.ApiWrapper;
import pe.gob.oece.bff.shared.HttpRequestUtils;

import static pe.gob.oece.bff.controller.helpers.JwtControllerHelper.obtenerUsuario;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPaths.EXAMEN_TESTEO)
@Tag(name = "Examen testeo", description = "BFF para preparacion de examenes de testeo")
@SecurityRequirement(name = "bearer-jwt")
@Validated
public class ExamenTesteoController {

    private final ExamenTesteoService examenTesteoService;

    @PostMapping("/antes-creacion")
    @Operation(summary = "Preparar datos antes de crear un examen de testeo")
    @Timed(value = "bff.examen-testeo.before-create")
    public ApiWrapper<JsonNode> antesCreacion(
            @RequestParam @NotNull Long idSesionTest,
            @AuthenticationPrincipal Jwt jwt,
            HttpServletRequest httpRequest
    ) {
        String usuario = obtenerUsuario(jwt);
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        JsonNode data = examenTesteoService.antesCreacion(
                idSesionTest,
                usuario,
                ipOrigen
        );
        return ApiWrapper.success(data, "Preparacion de examen testeo", httpRequest.getRequestURI());
    }

    @PostMapping("/desp-creacion")
    @Operation(summary = "Asociar examen creado a la sesion de testeo")
    @Timed(value = "bff.examen-testeo.after-create")
    public ApiWrapper<JsonNode> despCreacion(
            @RequestParam @NotNull Long idSesionTest,
            @RequestParam("idexamen") @NotNull Long idExamen,
            @AuthenticationPrincipal Jwt jwt,
            HttpServletRequest httpRequest
    ) {
        String usuario = obtenerUsuario(jwt);
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        JsonNode data = examenTesteoService.despCreacion(
                idSesionTest,
                idExamen,
                usuario,
                ipOrigen
        );
        return ApiWrapper.success(data, "Examen testeo asociado", httpRequest.getRequestURI());
    }

    @PostMapping("/obtener-det-sesion")
    @Operation(summary = "Obtener detalle de sesion de testeo")
    @Timed(value = "bff.examen-testeo.session-detail")
    public ApiWrapper<JsonNode> obtenerDetalleSesion(
            @RequestParam @NotNull Long idSesionTest,
            @AuthenticationPrincipal Jwt jwt,
            HttpServletRequest httpRequest
    ) {
        String usuario = obtenerUsuario(jwt);
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        JsonNode data = examenTesteoService.obtenerDetalleSesion(
                idSesionTest,
                usuario,
                ipOrigen
        );
        return ApiWrapper.success(data, "Detalle de sesion testeo", httpRequest.getRequestURI());
    }

}
