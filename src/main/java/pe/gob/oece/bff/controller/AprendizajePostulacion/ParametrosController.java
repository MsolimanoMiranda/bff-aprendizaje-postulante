package pe.gob.oece.bff.controller.AprendizajePostulacion;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.oece.bff.application.AprendizajePostulacion.ParametrosService;
import pe.gob.oece.bff.config.ApiPaths;
import pe.gob.oece.bff.shared.ApiWrapper;
import pe.gob.oece.bff.shared.HttpRequestUtils;

@RestController
@RequestMapping(ApiPaths.PARAMETROS)
@Tag(name = "Parámetros", description = "Consulta de parámetros de configuración del sistema")
@SecurityRequirement(name = "bearer-jwt")
public class ParametrosController {

    private final ParametrosService parametrosService;

    public ParametrosController(ParametrosService parametrosService) {
        this.parametrosService = parametrosService;
    }

    @GetMapping("/{codigo}")
    @Operation(summary = "Buscar parámetro por código")
    public ApiWrapper<JsonNode> buscarPorCodigo(
            @PathVariable String codigo,
            HttpServletRequest httpRequest
    ) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        JsonNode data = parametrosService.buscarPorCodigo(codigo, ipOrigen);
        return ApiWrapper.success(data, "Parámetro encontrado", httpRequest.getRequestURI());
    }

    @GetMapping("/seccion/{idSeccion}")
    @Operation(summary = "Listar parámetros por sección")
    public ApiWrapper<JsonNode> listarPorSeccion(
            @PathVariable Long idSeccion,
            HttpServletRequest httpRequest
    ) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        JsonNode data = parametrosService.listarPorSeccion(idSeccion, ipOrigen);
        return ApiWrapper.success(data, "Parámetros de la sección", httpRequest.getRequestURI());
    }
}
