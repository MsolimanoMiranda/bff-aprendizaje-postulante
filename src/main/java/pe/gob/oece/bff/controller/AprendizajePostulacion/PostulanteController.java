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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.oece.bff.application.AprendizajePostulacion.PostulanteService;
import pe.gob.oece.bff.config.ApiPaths;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.RegistroPostulanteRequest;
import pe.gob.oece.bff.shared.ApiWrapper;
import pe.gob.oece.bff.shared.HttpRequestUtils;

import static pe.gob.oece.bff.controller.helpers.JwtControllerHelper.obtenerToken;

@RestController
@RequestMapping(ApiPaths.POSTULANTE)
@Tag(name = "Postulante", description = "Registro y portal del postulante")
public class PostulanteController {

    private static final String HEADER_POSTULANTE_ID = "X-Postulante-Id";

    private final PostulanteService postulanteService;

    public PostulanteController(PostulanteService postulanteService) {
        this.postulanteService = postulanteService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Solicitar usuario y clave (registro de postulante)")
    public ApiWrapper<JsonNode> registrarPostulante(
            @Valid @RequestBody RegistroPostulanteRequest request,
            HttpServletRequest httpRequest
    ) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        JsonNode data = postulanteService.registrarPostulante(request, ipOrigen);
        return ApiWrapper.created(data, "Postulante registrado", httpRequest.getRequestURI());
    }

    @GetMapping("/perfil")
    @SecurityRequirement(name = "bearer-jwt")
    @Operation(summary = "Perfil del postulante para el wizard")
    public ApiWrapper<JsonNode> obtenerPerfilWizard(
            @RequestHeader(HEADER_POSTULANTE_ID) Long postulanteId,
            @RequestParam(required = false) Long idPostulacion,
            @AuthenticationPrincipal Jwt jwt,
            HttpServletRequest httpRequest
    ) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        String token = obtenerToken(jwt);
        JsonNode data = postulanteService.obtenerPerfilWizard(postulanteId, idPostulacion, token, ipOrigen);
        return ApiWrapper.success(data, "Perfil del postulante", httpRequest.getRequestURI());
    }

    @GetMapping("/dashboard")
    @SecurityRequirement(name = "bearer-jwt")
    @Operation(summary = "Dashboard del postulante")
    public ApiWrapper<JsonNode> obtenerDashboard(
            @RequestHeader(HEADER_POSTULANTE_ID) Long postulanteId,
            @AuthenticationPrincipal Jwt jwt,
            HttpServletRequest httpRequest
    ) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        String token = obtenerToken(jwt);
        JsonNode data = postulanteService.obtenerDashboard(postulanteId, token, ipOrigen);
        return ApiWrapper.success(data, "Dashboard del postulante", httpRequest.getRequestURI());
    }

    @GetMapping("/aula-virtual/url-login")
    @SecurityRequirement(name = "bearer-jwt")
    @Operation(summary = "URL de acceso al Aula Virtual del postulante")
    public ApiWrapper<JsonNode> obtenerUrlLoginAulaVirtual(
            @AuthenticationPrincipal Jwt jwt,
            HttpServletRequest httpRequest
    ) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        String token = obtenerToken(jwt);
        JsonNode data = postulanteService.obtenerUrlLoginAulaVirtual(token, ipOrigen);
        return ApiWrapper.success(data, "URL de acceso al Aula Virtual", httpRequest.getRequestURI());
    }

    @GetMapping("/{idPostulante}/historial-certificados")
    @SecurityRequirement(name = "bearer-jwt")
    @Operation(summary = "Historial de certificaciones del postulante")
    public ApiWrapper<JsonNode> obtenerHistorialCertificados(
            @PathVariable Long idPostulante,
            @AuthenticationPrincipal Jwt jwt,
            HttpServletRequest httpRequest
    ) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        String token = obtenerToken(jwt);
        JsonNode data = postulanteService.obtenerHistorialCertificados(idPostulante, token, ipOrigen);
        return ApiWrapper.success(data, "Historial de certificados", httpRequest.getRequestURI());
    }

}
