package pe.gob.oece.bff.controller.EjecucionExamen;

import com.fasterxml.jackson.databind.JsonNode;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pe.gob.oece.bff.application.AprendizajePostulacion.InvitacionTesteo.InvitacionTesteoService;
import pe.gob.oece.bff.config.ApiPaths;
import pe.gob.oece.bff.shared.ApiWrapper;
import pe.gob.oece.bff.shared.HttpRequestUtils;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPaths.INVITACION_TESTEO)
@Tag(name = "Invitacion testeo", description = "BFF para invitaciones de testeo")
@SecurityRequirement(name = "bearer-jwt")
public class InvitacionTesteoController {

    private final InvitacionTesteoService invitacionTesteoService;

    @GetMapping("/mis-invitaciones")
    @Operation(summary = "Listar mis invitaciones de testeo")
    @Timed(value = "bff.invitacion-testeo.list")
    public ApiWrapper<JsonNode> obtenerMisInvitaciones(
            @AuthenticationPrincipal Jwt jwt,
            HttpServletRequest httpRequest
    ) {
        String token = obtenerToken(jwt);
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        JsonNode data = invitacionTesteoService.obtenerMisInvitaciones(token, ipOrigen);
        return ApiWrapper.success(data, "Invitaciones de testeo", httpRequest.getRequestURI());
    }

    @GetMapping("/bancos-test-disponibles")
    @Operation(summary = "Listar bancos disponibles para testeo")
    @Timed(value = "bff.invitacion-testeo.available-banks")
    public ApiWrapper<JsonNode> listarBancosTestDisponibles(
            @AuthenticationPrincipal Jwt jwt,
            HttpServletRequest httpRequest
    ) {
        String token = obtenerToken(jwt);
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        JsonNode data = invitacionTesteoService.listarBancosTestDisponibles(token, ipOrigen);
        return ApiWrapper.success(data, "Bancos disponibles para testeo", httpRequest.getRequestURI());
    }

    @PostMapping("/{idInvitacion}/aceptar")
    @Operation(summary = "Aceptar invitacion de testeo")
    @Timed(value = "bff.invitacion-testeo.accept")
    public ApiWrapper<JsonNode> aceptarInvitacion(
            @PathVariable Long idInvitacion,
            @AuthenticationPrincipal Jwt jwt,
            HttpServletRequest httpRequest
    ) {
        String token = obtenerToken(jwt);
        String usuario = obtenerUsuario(jwt);
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        JsonNode data = invitacionTesteoService.aceptarInvitacion(
                idInvitacion,
                token,
                usuario,
                ipOrigen
        );
        return ApiWrapper.success(data, "Invitacion aceptada", httpRequest.getRequestURI());
    }

    @PostMapping("/{idInvitacion}/rechazar")
    @Operation(summary = "Rechazar invitacion de testeo")
    @Timed(value = "bff.invitacion-testeo.reject")
    public ApiWrapper<JsonNode> rechazarInvitacion(
            @PathVariable Long idInvitacion,
            @AuthenticationPrincipal Jwt jwt,
            HttpServletRequest httpRequest
    ) {
        String token = obtenerToken(jwt);
        String usuario = obtenerUsuario(jwt);
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        JsonNode data = invitacionTesteoService.rechazarInvitacion(
                idInvitacion,
                token,
                usuario,
                ipOrigen
        );
        return ApiWrapper.success(data, "Invitacion rechazada", httpRequest.getRequestURI());
    }

    @PostMapping("/iniciar-testeo/{idBanco}")
    @Operation(summary = "Iniciar testeo")
    @Timed(value = "bff.invitacion-testeo.start")
    public ApiWrapper<JsonNode> iniciarTesteo(
            @PathVariable Long idBanco,
            @AuthenticationPrincipal Jwt jwt,
            HttpServletRequest httpRequest
    ) {
        String token = obtenerToken(jwt);
        String usuario = obtenerUsuario(jwt);
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        JsonNode data = invitacionTesteoService.iniciarTesteo(
                idBanco,
                token,
                usuario,
                ipOrigen
        );
        return ApiWrapper.success(data, "Testeo iniciado", httpRequest.getRequestURI());
    }

    @PostMapping("/completar-testeo/{idBanco}")
    @Operation(summary = "Completar testeo")
    @Timed(value = "bff.invitacion-testeo.complete")
    public ApiWrapper<JsonNode> completarTesteo(
            @PathVariable Long idBanco,
            @AuthenticationPrincipal Jwt jwt,
            HttpServletRequest httpRequest
    ) {
        String token = obtenerToken(jwt);
        String usuario = obtenerUsuario(jwt);
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        JsonNode data = invitacionTesteoService.completarTesteo(
                idBanco,
                token,
                usuario,
                ipOrigen
        );
        return ApiWrapper.success(data, "Testeo completado", httpRequest.getRequestURI());
    }

    private static String obtenerToken(Jwt jwt) {
        if (jwt == null || jwt.getTokenValue() == null || jwt.getTokenValue().isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token requerido");
        }
        return jwt.getTokenValue();
    }

    private static String obtenerUsuario(Jwt jwt) {
        String usuario = jwt != null ? jwt.getClaimAsString("idUsuario") : null;
        if (usuario == null || usuario.isBlank()) {
            usuario = jwt != null ? jwt.getSubject() : null;
        }
        if (usuario == null || usuario.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token sin usuario");
        }
        return usuario;
    }
}
