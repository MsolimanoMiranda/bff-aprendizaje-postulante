package pe.gob.oece.bff.controller.AprendizajePostulacion;

import com.fasterxml.jackson.databind.JsonNode;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.gob.oece.bff.application.AprendizajePostulacion.Certificado.CertificadoQueryService;
import pe.gob.oece.bff.application.AprendizajePostulacion.CursoAsignado.CursoAsignadoQueryService;
import pe.gob.oece.bff.config.ApiPaths;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiPaths.CURSO_ASIGNADO)
@Tag(name = "CursoAsignado", description = "BFF para gestion de cursos asignados")
@SecurityRequirement(name = "bearer-jwt")
public class CursoAsignadoController {

    private static final String HEADER_POSTULANTE_ID = "X-Postulante-Id";

    private final CursoAsignadoQueryService cursoAsignadoQueryService;

    @GetMapping("mis-cursos")
    @Operation(summary = "obtiene mis cursos")
    @Timed(value = "cursos.asignado.mis-cursos")
    public ResponseEntity<JsonNode> obtenerMisCursos(
            @RequestHeader(HEADER_POSTULANTE_ID) Long postulanteId
    ) {
        return ResponseEntity.ok(cursoAsignadoQueryService.obtenerMisCursos(postulanteId));
    }
}
