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
import pe.gob.oece.bff.config.ApiPaths;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiPaths.CERTIFICADO)
@Tag(name = "Certificado", description = "BFF para gestion de certificados")
@SecurityRequirement(name = "bearer-jwt")
public class CertificadoController {
    private static final String HEADER_POSTULANTE_ID = "X-Postulante-Id";

    private final CertificadoQueryService certificadoQueryService;

    @GetMapping("mis-certificados")
    @Operation(summary = "obtiene mis certificados")
    @Timed(value = "certificado.mis-certificados")
    public ResponseEntity<JsonNode> misCertificados(
            @RequestHeader(HEADER_POSTULANTE_ID) Long postulanteId
    ) {
        return ResponseEntity.ok(certificadoQueryService.misCertificados(postulanteId));
    }

    @GetMapping("/examen/{idExamen}/datos-descarga")
    @Operation(summary = "Obtiene o crea datos de certificado por examen")
    @Timed(value = "certificado.examen.datos-descarga")
    public ResponseEntity<JsonNode> obtenerDatosDescargaPorExamen(
            @PathVariable Long idExamen
    ) {
        return ResponseEntity.ok(certificadoQueryService.obtenerDatosDescargaPorExamen(idExamen));
    }
}
