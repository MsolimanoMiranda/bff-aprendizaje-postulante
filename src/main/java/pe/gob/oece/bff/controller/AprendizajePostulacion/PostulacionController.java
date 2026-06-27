package pe.gob.oece.bff.controller.AprendizajePostulacion;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pe.gob.oece.bff.application.AprendizajePostulacion.PostulacionesService;
import pe.gob.oece.bff.config.ApiPaths;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.ConfirmarPostulacionRequest;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.ExperienciaLaboralRequest;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.GenerarOrdenPagoRequest;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.IniciarPostulacionRequest;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.SeleccionarProgramacionRequest;
import pe.gob.oece.bff.shared.ApiWrapper;
import pe.gob.oece.bff.shared.HttpRequestUtils;

@RestController
@RequestMapping(ApiPaths.POSTULACIONES)
@Tag(name = "Postulaciones", description = "API de postulación de aprendizaje")
@SecurityRequirement(name = "bearer-jwt")
public class PostulacionController {

    private static final String HEADER_POSTULANTE_ID = "X-Postulante-Id";

    private final PostulacionesService postulacionesService;

    public PostulacionController(PostulacionesService postulacionesService) {
        this.postulacionesService = postulacionesService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Paso 1A: Iniciar postulación")
    public ApiWrapper<JsonNode> iniciarPostulacion(
            @RequestHeader(HEADER_POSTULANTE_ID) Long postulanteId,
            @Valid @RequestBody IniciarPostulacionRequest request,
            HttpServletRequest httpRequest
    ) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        JsonNode data = postulacionesService.iniciarPostulacion(postulanteId, request, ipOrigen);
        return ApiWrapper.created(data, "Postulación iniciada", httpRequest.getRequestURI());
    }

    @GetMapping
    @Operation(summary = "Listar mis postulaciones")
    public ApiWrapper<JsonNode> listarMisPostulaciones(
            @RequestHeader(HEADER_POSTULANTE_ID) Long postulanteId,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization,
            @AuthenticationPrincipal Jwt jwt,
            HttpServletRequest httpRequest
    ) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        String token = obtenerToken(jwt, authorization);
        JsonNode data = postulacionesService.listarMisPostulaciones(postulanteId, token, ipOrigen);
        return ApiWrapper.success(data, "Postulaciones del postulante", httpRequest.getRequestURI());
    }

    @GetMapping("/{idPostulacion}")
    @Operation(summary = "Ver detalle de postulación")
    public ApiWrapper<JsonNode> obtenerDetalle(
            @PathVariable Long idPostulacion,
            @RequestHeader(HEADER_POSTULANTE_ID) Long postulanteId,
            HttpServletRequest httpRequest
    ) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        JsonNode data = postulacionesService.obtenerDetalle(idPostulacion, postulanteId, ipOrigen);
        return ApiWrapper.success(data, "Detalle de postulación", httpRequest.getRequestURI());
    }

    @PostMapping("/{idPostulacion}/experiencias")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Paso 1B: Agregar experiencia laboral")
    public ApiWrapper<JsonNode> agregarExperiencia(
            @PathVariable Long idPostulacion,
            @RequestHeader(HEADER_POSTULANTE_ID) Long postulanteId,
            @Valid @RequestBody ExperienciaLaboralRequest request,
            HttpServletRequest httpRequest
    ) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        JsonNode data = postulacionesService.agregarExperiencia(idPostulacion, postulanteId, request, ipOrigen);
        return ApiWrapper.created(data, "Experiencia laboral agregada", httpRequest.getRequestURI());
    }

    @PutMapping("/{idPostulacion}/experiencias/{idExperiencia}")
    @Operation(summary = "Paso 1B: Editar experiencia laboral")
    public ApiWrapper<JsonNode> editarExperiencia(
            @PathVariable Long idPostulacion,
            @PathVariable Long idExperiencia,
            @RequestHeader(HEADER_POSTULANTE_ID) Long postulanteId,
            @Valid @RequestBody ExperienciaLaboralRequest request,
            HttpServletRequest httpRequest
    ) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        JsonNode data = postulacionesService.editarExperiencia(
                idPostulacion, idExperiencia, postulanteId, request, ipOrigen
        );
        return ApiWrapper.success(data, "Experiencia laboral actualizada", httpRequest.getRequestURI());
    }

    @DeleteMapping("/{idPostulacion}/experiencias/{idExperiencia}")
    @Operation(summary = "Paso 1B: Eliminar experiencia laboral")
    public ApiWrapper<JsonNode> eliminarExperiencia(
            @PathVariable Long idPostulacion,
            @PathVariable Long idExperiencia,
            @RequestHeader(HEADER_POSTULANTE_ID) Long postulanteId,
            HttpServletRequest httpRequest
    ) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        JsonNode data = postulacionesService.eliminarExperiencia(idPostulacion, idExperiencia, postulanteId, ipOrigen);
        return ApiWrapper.success(data, "Experiencia laboral eliminada", httpRequest.getRequestURI());
    }

    @PostMapping("/{idPostulacion}/programacion")
    @Operation(summary = "Paso 2: Seleccionar programación de examen")
    public ApiWrapper<JsonNode> seleccionarProgramacion(
            @PathVariable Long idPostulacion,
            @RequestHeader(HEADER_POSTULANTE_ID) Long postulanteId,
            @Valid @RequestBody SeleccionarProgramacionRequest request,
            HttpServletRequest httpRequest
    ) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        JsonNode data = postulacionesService.seleccionarProgramacion(idPostulacion, postulanteId, request, ipOrigen);
        return ApiWrapper.success(data, "Programación de examen seleccionada", httpRequest.getRequestURI());
    }

    @PostMapping("/{idPostulacion}/pagos")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Paso 3: Generar orden de pago")
    public ApiWrapper<JsonNode> generarOrdenPago(
            @PathVariable Long idPostulacion,
            @RequestHeader(HEADER_POSTULANTE_ID) Long postulanteId,
            @Valid @RequestBody GenerarOrdenPagoRequest request,
            HttpServletRequest httpRequest
    ) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        JsonNode data = postulacionesService.generarOrdenPago(idPostulacion, postulanteId, request, ipOrigen);
        return ApiWrapper.created(data, "Orden de pago generada", httpRequest.getRequestURI());
    }

    @PostMapping("/{idPostulacion}/confirmar")
    @Operation(summary = "Paso 3 → 4: Confirmar postulación")
    public ApiWrapper<JsonNode> confirmarPostulacion(
            @PathVariable Long idPostulacion,
            @RequestHeader(HEADER_POSTULANTE_ID) Long postulanteId,
            @Valid @RequestBody ConfirmarPostulacionRequest request,
            HttpServletRequest httpRequest
    ) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        JsonNode data = postulacionesService.confirmarPostulacion(idPostulacion, postulanteId, request, ipOrigen);
        return ApiWrapper.success(data, "Postulación confirmada", httpRequest.getRequestURI());
    }

    private static String obtenerToken(Jwt jwt, String authorization) {
        if (jwt != null && jwt.getTokenValue() != null && !jwt.getTokenValue().isBlank()) {
            return jwt.getTokenValue();
        }
        if (authorization == null || authorization.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token requerido");
        }
        return authorization.regionMatches(true, 0, "Bearer ", 0, 7)
                ? authorization.substring(7).trim()
                : authorization.trim();
    }
}
