package pe.gob.oece.bff.controller.AprendizajePostulacion;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pe.gob.oece.bff.application.AprendizajePostulacion.PostulacionesService;
import pe.gob.oece.bff.config.ApiPaths;
import pe.gob.oece.bff.shared.ApiWrapper;
import pe.gob.oece.bff.shared.HttpRequestUtils;

import static pe.gob.oece.bff.controller.helpers.JwtControllerHelper.obtenerToken;

@RestController
@RequestMapping(ApiPaths.FORMACION_ACADEMICA)
@Tag(name = "Formacion Academica", description = "Proxy de formacion academica de aprendizaje")
@SecurityRequirement(name = "bearer-jwt")
public class FormacionAcademicaController {

    private final PostulacionesService postulacionesService;

    public FormacionAcademicaController(PostulacionesService postulacionesService) {
        this.postulacionesService = postulacionesService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Agregar formacion academica")
    public ApiWrapper<JsonNode> agregarFormacion(
            @RequestParam MultiValueMap<String, String> request,
            @RequestParam(required = false) MultiValueMap<String, MultipartFile> archivos,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization,
            @AuthenticationPrincipal Jwt jwt,
            HttpServletRequest httpRequest
    ) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        String token = obtenerToken(jwt, authorization);
        JsonNode data = postulacionesService.agregarFormacion(request, archivos, token, ipOrigen);
        return ApiWrapper.created(data, "Formacion academica agregada", httpRequest.getRequestURI());
    }

    @PutMapping(value = "/{idFormacion}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Editar formacion academica")
    public ApiWrapper<JsonNode> editarFormacion(
            @PathVariable Long idFormacion,
            @RequestParam MultiValueMap<String, String> request,
            @RequestParam(required = false) MultiValueMap<String, MultipartFile> archivos,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization,
            @AuthenticationPrincipal Jwt jwt,
            HttpServletRequest httpRequest
    ) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        String token = obtenerToken(jwt, authorization);
        JsonNode data = postulacionesService.editarFormacion(idFormacion, request, archivos, token, ipOrigen);
        return ApiWrapper.success(data, "Formacion academica actualizada", httpRequest.getRequestURI());
    }

    @DeleteMapping("/{idFormacion}")
    @Operation(summary = "Eliminar formacion academica")
    public ApiWrapper<JsonNode> eliminarFormacion(
            @PathVariable Long idFormacion,
            HttpServletRequest httpRequest
    ) {
        String ipOrigen = HttpRequestUtils.obtenerIpOrigen(httpRequest);
        JsonNode data = postulacionesService.eliminarFormacion(idFormacion, ipOrigen);
        return ApiWrapper.success(data, "Formacion academica eliminada", httpRequest.getRequestURI());
    }
}
