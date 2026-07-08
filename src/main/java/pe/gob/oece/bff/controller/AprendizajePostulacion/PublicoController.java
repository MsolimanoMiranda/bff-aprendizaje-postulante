package pe.gob.oece.bff.controller.AprendizajePostulacion;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.oece.bff.application.AprendizajePostulacion.PublicoService;
import pe.gob.oece.bff.config.ApiPaths;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.BusquedaCertificadoRequest;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.BusquedaProgramacionRequest;
import pe.gob.oece.bff.shared.ApiWrapper;
import pe.gob.oece.bff.shared.HttpRequestUtils;

@RestController
@RequestMapping(ApiPaths.PUBLICO)
@Tag(name = "Público", description = "Endpoints accesibles sin autenticación")
public class PublicoController {

    private final PublicoService publicoService;

    public PublicoController(PublicoService publicoService) {
        this.publicoService = publicoService;
    }

    @GetMapping("/programaciones")
    @Operation(summary = "Consulta pública de programación de exámenes")
    public ApiWrapper<JsonNode> buscarProgramaciones(
            @ParameterObject @ModelAttribute BusquedaProgramacionRequest solicitud,
            HttpServletRequest httpRequest
    ) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        JsonNode data = publicoService.buscarProgramaciones(solicitud, ipOrigen);
        return ApiWrapper.success(data, "Programaciones públicas", httpRequest.getRequestURI());
    }

    @GetMapping("/locales/departamento/{idDepartamento}")
    @Operation(summary = "Listar locales por departamento")
    public ApiWrapper<JsonNode> listarLocalesPorDepartamento(
            @PathVariable Long idDepartamento,
            HttpServletRequest httpRequest
    ) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        JsonNode data = publicoService.listarLocalesPorDepartamento(idDepartamento, ipOrigen);
        return ApiWrapper.success(data, "Locales por departamento", httpRequest.getRequestURI());
    }

    @GetMapping("/certificados")
    @Operation(summary = "Búsqueda pública de profesionales certificados")
    public ApiWrapper<JsonNode> buscarCertificados(
            @ParameterObject @ModelAttribute BusquedaCertificadoRequest solicitud,
            HttpServletRequest httpRequest
    ) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        JsonNode data = publicoService.buscarCertificados(solicitud, ipOrigen);
        return ApiWrapper.success(data, "Certificados públicos", httpRequest.getRequestURI());
    }

    @GetMapping("/certificados/{idCertificado}")
    @Operation(summary = "Detalle de certificado con QR e historial")
    public ApiWrapper<JsonNode> obtenerDetalleCertificado(
            @PathVariable Long idCertificado,
            HttpServletRequest httpRequest
    ) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        JsonNode data = publicoService.obtenerDetalleCertificado(idCertificado, ipOrigen);
        return ApiWrapper.success(data, "Detalle de certificado", httpRequest.getRequestURI());
    }
}
