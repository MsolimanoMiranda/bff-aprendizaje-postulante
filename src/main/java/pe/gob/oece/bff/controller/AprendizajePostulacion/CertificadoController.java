package pe.gob.oece.bff.controller.AprendizajePostulacion;

import com.fasterxml.jackson.databind.JsonNode;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.gob.oece.bff.application.AprendizajePostulacion.Certificado.CertificadoQueryService;
import pe.gob.oece.bff.config.ApiPaths;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.ActualizarCertificadoRequest;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.AlfrescoArchivoDownloadResponse;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.BusquedaCertificadoPostulanteRequest;
import pe.gob.oece.bff.shared.ApiWrapper;

import java.util.List;

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
            @RequestHeader(HEADER_POSTULANTE_ID) Long postulanteId,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization
    ) {
        return ResponseEntity.ok(certificadoQueryService.misCertificados(postulanteId,authorization));
    }

    @GetMapping("/{idCertificado}")
    @Operation(summary = "obtiene mis certificados")
    @Timed(value = "certificado.mis-certificados")
    public ResponseEntity<byte[]> descargarArchivoAlfresco(
            @PathVariable Long idCertificado,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization
    ) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.inline()
                                .filename("certificado-" + idCertificado + ".pdf")
                                .build()
                                .toString())
                .body(certificadoQueryService.descargarArchivoAlfresco(idCertificado,authorization));

    }

    @GetMapping("/descargar-todos")
    @Operation(summary = "obtiene mis certificados")
    @Timed(value = "certificado.mis-certificados")
    public ResponseEntity<byte[]> descargarTodos(
            @RequestHeader(HEADER_POSTULANTE_ID) Long postulanteId,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization
    ) {
        byte[] zip = certificadoQueryService.descargarTodos(postulanteId, authorization);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename("certificados.zip")
                                .build()
                                .toString())
                .contentLength(zip.length)
                .body(zip);
    }
    @GetMapping("/buscar")
    @Operation(summary = "Buscar certificados con datos del postulante")
    @Timed(value = "certificado.buscar")
    public ResponseEntity<JsonNode> buscar(
            @ModelAttribute BusquedaCertificadoPostulanteRequest solicitud
    ) {
        return ResponseEntity.ok(certificadoQueryService.buscar(solicitud));
    }

    @PutMapping("/{idCertificado}")
    @Operation(summary = "Actualizar certificado")
    @Timed(value = "certificado.actualizar")
    public ResponseEntity<JsonNode> actualizar(
            @PathVariable Long idCertificado,
            @RequestBody ActualizarCertificadoRequest solicitud,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization
    ) {
        return ResponseEntity.ok(certificadoQueryService.actualizar(idCertificado, solicitud, authorization));
    }

    @GetMapping("/examen/{idExamen}/datos-descarga")
    @Operation(summary = "Obtiene o crea datos de certificado por examen")
    @Timed(value = "certificado.examen.datos-descarga")
    public ResponseEntity<JsonNode> obtenerDatosDescargaPorExamen(
            @PathVariable Long idExamen,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization
    ) {
        return ResponseEntity.ok(certificadoQueryService.obtenerDatosDescargaPorExamen(idExamen, authorization));
    }

    @GetMapping("/examen/{idExamen}/archivo")
    @Operation(summary = "Descarga el certificado del examen")
    @Timed(value = "certificado.examen.archivo")
    public ResponseEntity<byte[]> descargarArchivoPorExamen(
            @PathVariable Long idExamen,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization
    ) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename("certificado-" + idExamen + ".pdf")
                                .build()
                                .toString())
                .body(certificadoQueryService.descargarArchivoPorExamen(idExamen, authorization));
    }
}
