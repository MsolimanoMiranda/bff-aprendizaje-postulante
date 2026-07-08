package pe.gob.oece.bff.controller.EjecucionExamen;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import pe.gob.oece.bff.application.AprendizajePostulacion.EjecucionExamenService;
import pe.gob.oece.bff.config.ApiPaths;
import pe.gob.oece.bff.infrastructure.client.EjecucionExamen.dto.*;
import pe.gob.oece.bff.shared.ApiWrapper;
import pe.gob.oece.bff.shared.HttpRequestUtils;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(ApiPaths.EJECUCION_EXAMEN)
@Tag(name = "Ejecución Examen", description = "Gestión de ejecución de exámenes del postulante")
@SecurityRequirement(name = "bearer-jwt")
public class EjecucionExamenController {
    private final EjecucionExamenService ejecucionExamenService;

    public EjecucionExamenController(EjecucionExamenService ejecucionExamenService) {
        this.ejecucionExamenService = ejecucionExamenService;
    }

    @PostMapping("/validar-token")
    @Operation(summary = "Validar token de examen")
    public ApiWrapper<JsonNode> validarToken(
            @Valid @RequestBody ValidarTokenExamenRequest request,
            HttpServletRequest httpRequest
    ) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        JsonNode data = ejecucionExamenService.validarToken(request, ipOrigen);
        return ApiWrapper.success(data, "Token de examen validado", httpRequest.getRequestURI());
    }

    @PostMapping("/iniciar")
    @Operation(summary = "Iniciar ejecución de examen")
    public ApiWrapper<JsonNode> iniciar(
            @Valid @RequestBody IniciarEjecucionExamenRequest request,
            HttpServletRequest httpRequest
    ) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        JsonNode data = ejecucionExamenService.iniciar(request, ipOrigen);
        return ApiWrapper.success(data, "Ejecución de examen iniciada", httpRequest.getRequestURI());
    }

    @PostMapping("/heartbeat")
    @Operation(summary = "Registrar heartbeat de ejecución de examen")
    public ApiWrapper<JsonNode> heartbeat(
            @Valid @RequestBody HeartbeatEjecucionExamenRequest request,
            HttpServletRequest httpRequest
    ) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        JsonNode data = ejecucionExamenService.heartbeat(request, ipOrigen);
        return ApiWrapper.success(data, "Heartbeat registrado", httpRequest.getRequestURI());
    }

    @PutMapping("/pregunta-actual")
    @Operation(summary = "Actualizar pregunta actual")
    public ApiWrapper<JsonNode> actualizarPreguntaActual(
            @Valid @RequestBody ActualizarPreguntaActualRequest request,
            HttpServletRequest httpRequest
    ) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        JsonNode data = ejecucionExamenService.actualizarPreguntaActual(request, ipOrigen);
        return ApiWrapper.success(data, "Pregunta actual actualizada", httpRequest.getRequestURI());
    }

    @PutMapping("/estado")
    @Operation(summary = "Cambiar estado de ejecución de examen")
    public ApiWrapper<JsonNode> cambiarEstado(
            @Valid @RequestBody CambiarEstadoEjecucionRequest request,
            HttpServletRequest httpRequest
    ) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        JsonNode data = ejecucionExamenService.cambiarEstado(request, ipOrigen);
        return ApiWrapper.success(data, "Estado de ejecución actualizado", httpRequest.getRequestURI());
    }

    @GetMapping("/{idExamen}")
    @Operation(summary = "Obtener ejecución por examen")
    public ApiWrapper<JsonNode> obtenerPorExamen(
            @PathVariable Integer idExamen,
            HttpServletRequest httpRequest
    ) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        JsonNode data = ejecucionExamenService.obtenerPorExamen(idExamen, ipOrigen);
        return ApiWrapper.success(data, "Ejecución de examen encontrada", httpRequest.getRequestURI());
    }

    @GetMapping("/examen/status")
    @Operation(summary = "Obtener estado individual del examen")
    public ApiWrapper<JsonNode> obtenerEstadoIndividual(
            @RequestParam Long idInscripcion,
            HttpServletRequest httpRequest
    ) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        JsonNode data = ejecucionExamenService.obtenerEstadoIndividual(idInscripcion, ipOrigen);
        return ApiWrapper.success(data, "Estado individual del examen", httpRequest.getRequestURI());
    }

    @GetMapping("/examen/resultado")
    @Operation(summary = "Obtener resultados del examen")
    public ApiWrapper<JsonNode> obtenerResultados(
            @RequestParam Long idExamen,
            HttpServletRequest httpRequest
    ) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        JsonNode data = ejecucionExamenService.obtenerResultados(idExamen, ipOrigen);
        return ApiWrapper.success(data, "Resultados del examen", httpRequest.getRequestURI());
    }

    @GetMapping("/examen/informacion")
    @Operation(summary = "Obtener información del examen")
    public ApiWrapper<JsonNode> obtenerInformacionExamen(
            @RequestParam Long idInscripcion,
            HttpServletRequest httpRequest
    ) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        JsonNode data = ejecucionExamenService.obtenerInformacionExamen(idInscripcion, ipOrigen);
        return ApiWrapper.success(data, "Información del examen", httpRequest.getRequestURI());
    }

    @GetMapping("/examen/finalizar")
    @Operation(summary = "Finalizar examen")
    public ApiWrapper<JsonNode> finalizar(
            @RequestParam Long idExamen,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization,
            HttpServletRequest httpRequest
    ) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        JsonNode data = ejecucionExamenService.finalizar(idExamen, ipOrigen, authorization);
        return ApiWrapper.success(data, "Examen finalizado", httpRequest.getRequestURI());
    }

    @GetMapping("/examen/Obtener")
    @Operation(summary = "Obtener preguntas respondidas del examen")
    public ApiWrapper<JsonNode> obtenerPreguntasRespondidas(
            @RequestParam Long idInscripcion,
            HttpServletRequest httpRequest
    ) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        JsonNode data = ejecucionExamenService.obtenerPreguntasRespondidas(idInscripcion, ipOrigen);
        return ApiWrapper.success(data, "Preguntas respondidas del examen", httpRequest.getRequestURI());
    }

    @PostMapping("/examen/guardar-respuestas")
    @Operation(summary = "Guardar respuestas del examen")
    public ApiWrapper<JsonNode> guardarRespuestas(
            @Valid @RequestBody GuardarRespuestasRequest request,
            HttpServletRequest httpRequest
    ) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        JsonNode data = ejecucionExamenService.guardarRespuestas(request, ipOrigen);
        return ApiWrapper.success(data, "Respuestas del examen guardadas", httpRequest.getRequestURI());
    }

    @PostMapping("/examen/cursos-recomendados")
    @Operation(summary = "Listar cursos recomendados por competencias")
    public ApiWrapper<JsonNode> listarCursosRecomendados(
            @RequestBody List<Long> idsCompetencia,
            HttpServletRequest httpRequest
    ) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        JsonNode data = ejecucionExamenService.listarCursosRecomendados(idsCompetencia, ipOrigen);
        return ApiWrapper.success(data, "Cursos recomendados", httpRequest.getRequestURI());
    }
}
