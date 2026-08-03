package pe.gob.oece.bff.controller.AprendizajePostulacion;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.oece.bff.application.AprendizajePostulacion.PostulacionesService;
import pe.gob.oece.bff.config.ApiPaths;
import pe.gob.oece.bff.shared.ApiWrapper;
import pe.gob.oece.bff.shared.HttpRequestUtils;

@RestController
@RequestMapping(ApiPaths.LISTAS)
@Tag(name = "Listas Generales", description = "Catalogos de referencia del microservicio de postulacion")
public class ListaGeneralController {

    private final PostulacionesService postulacionesService;

    public ListaGeneralController(PostulacionesService postulacionesService) {
        this.postulacionesService = postulacionesService;
    }

    @GetMapping("/{tipo}")
    @Operation(summary = "Listar items por tipo de lista")
    public ApiWrapper<JsonNode> listarPorTipo(
            @PathVariable String tipo,
            HttpServletRequest httpRequest
    ) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        JsonNode data = postulacionesService.listarPorTipoLista(tipo, ipOrigen);
        return ApiWrapper.success(data, "Items de lista general", httpRequest.getRequestURI());
    }
}
