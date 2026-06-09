package pe.gob.oece.bff.controller.AprendizajePostulacion.ReprogramacionExamenes;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.oece.bff.application.AprendizajePostulacion.ReprogramacionExamenes.ReprogramacionExamenesService;
import pe.gob.oece.bff.config.ApiPaths;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.reprogramacion.BusquedaProgramacionDisponibleRequest;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.reprogramacion.RegistrarReprogramacionRequest;
import pe.gob.oece.bff.shared.ApiWrapper;
import pe.gob.oece.bff.shared.HttpRequestUtils;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(ApiPaths.REPROGRAMACION_EXAMENES)
@Tag(name = "Reprogramacion de examenes", description = "API BFF para reprogramacion de examenes del postulante")
public class ReprogramacionExamenesController {

    private final ReprogramacionExamenesService reprogramacionExamenesService;

    public ReprogramacionExamenesController(ReprogramacionExamenesService reprogramacionExamenesService) {
        this.reprogramacionExamenesService = reprogramacionExamenesService;
    }

    @GetMapping("/programaciones-disponibles")
    @Operation(summary = "Lista programaciones disponibles para reprogramacion")
    public ApiWrapper<JsonNode> buscarProgramacionesDisponibles(
            @ParameterObject @ModelAttribute BusquedaProgramacionDisponibleRequest solicitud,
            @RequestHeader(value = "Authorization", required = false) String authorization,
            HttpServletRequest httpRequest
    ) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        JsonNode data = reprogramacionExamenesService.buscarProgramacionesDisponibles(
                solicitud, authorization, ipOrigen);
        return ApiWrapper.success(data, "Programaciones disponibles para reprogramacion", httpRequest.getRequestURI());
    }

    @PostMapping("/registrar")
    @Operation(summary = "Registra la reprogramacion del examen")
    public ApiWrapper<JsonNode> registrarReprogramacion(
            @Valid @RequestBody RegistrarReprogramacionRequest solicitud,
            @RequestHeader(value = "Authorization", required = false) String authorization,
            HttpServletRequest httpRequest
    ) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        JsonNode data = reprogramacionExamenesService.registrarReprogramacion(
                solicitud, authorization, ipOrigen);
        return ApiWrapper.success(data, "Reprogramacion registrada", httpRequest.getRequestURI());
    }
}
