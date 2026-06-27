package pe.gob.oece.bff.application.AprendizajePostulacion;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.PostulacionClient;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.ConfirmarPostulacionRequest;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.ExperienciaLaboralRequest;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.GenerarOrdenPagoRequest;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.IniciarPostulacionRequest;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.SeleccionarProgramacionRequest;

@Service
public class PostulacionesService {

    private static final Logger logger = LoggerFactory.getLogger(PostulacionesService.class);

    private final PostulacionClient postulacionClient;

    public PostulacionesService(PostulacionClient postulacionClient) {
        this.postulacionClient = postulacionClient;
    }

    public JsonNode iniciarPostulacion(Long postulanteId, IniciarPostulacionRequest request, String ipOrigen) {
        audit("iniciarPostulacion", postulanteId, ipOrigen);
        return postulacionClient.iniciarPostulacion(postulanteId, request);
    }

    public JsonNode listarMisPostulaciones(Long postulanteId, String token, String ipOrigen) {
        audit("listarMisPostulaciones", postulanteId, ipOrigen);
        return postulacionClient.listarMisPostulaciones(postulanteId, token);
    }

    public JsonNode obtenerDetalle(Long idPostulacion, Long postulanteId, String ipOrigen) {
        audit("obtenerDetalle", postulanteId, ipOrigen, "idPostulacion=" + idPostulacion);
        return postulacionClient.obtenerDetalle(idPostulacion, postulanteId);
    }

    public JsonNode agregarExperiencia(
            Long idPostulacion,
            Long postulanteId,
            ExperienciaLaboralRequest request,
            String ipOrigen
    ) {
        audit("agregarExperiencia", postulanteId, ipOrigen, "idPostulacion=" + idPostulacion);
        return postulacionClient.agregarExperiencia(idPostulacion, postulanteId, request);
    }

    public JsonNode editarExperiencia(
            Long idPostulacion,
            Long idExperiencia,
            Long postulanteId,
            ExperienciaLaboralRequest request,
            String ipOrigen
    ) {
        audit("editarExperiencia", postulanteId, ipOrigen,
                "idPostulacion=" + idPostulacion, "idExperiencia=" + idExperiencia);
        return postulacionClient.editarExperiencia(idPostulacion, idExperiencia, postulanteId, request);
    }

    public JsonNode eliminarExperiencia(
            Long idPostulacion,
            Long idExperiencia,
            Long postulanteId,
            String ipOrigen
    ) {
        audit("eliminarExperiencia", postulanteId, ipOrigen,
                "idPostulacion=" + idPostulacion, "idExperiencia=" + idExperiencia);
        return postulacionClient.eliminarExperiencia(idPostulacion, idExperiencia, postulanteId);
    }

    public JsonNode seleccionarProgramacion(
            Long idPostulacion,
            Long postulanteId,
            SeleccionarProgramacionRequest request,
            String ipOrigen
    ) {
        audit("seleccionarProgramacion", postulanteId, ipOrigen, "idPostulacion=" + idPostulacion);
        return postulacionClient.seleccionarProgramacion(idPostulacion, postulanteId, request);
    }

    public JsonNode generarOrdenPago(
            Long idPostulacion,
            Long postulanteId,
            GenerarOrdenPagoRequest request,
            String ipOrigen
    ) {
        audit("generarOrdenPago", postulanteId, ipOrigen, "idPostulacion=" + idPostulacion);
        return postulacionClient.generarOrdenPago(idPostulacion, postulanteId, request);
    }

    public JsonNode confirmarPostulacion(
            Long idPostulacion,
            Long postulanteId,
            ConfirmarPostulacionRequest request,
            String ipOrigen
    ) {
        audit("confirmarPostulacion", postulanteId, ipOrigen, "idPostulacion=" + idPostulacion);
        return postulacionClient.confirmarPostulacion(idPostulacion, postulanteId, request);
    }

    private static void audit(String operacion, Long postulanteId, String ipOrigen, String... extras) {
        if (extras.length == 0) {
            logger.info("AUDIT op={} postulanteId={} ipOrigen={}", operacion, postulanteId, ipOrigen);
        } else {
            logger.info("AUDIT op={} postulanteId={} ipOrigen={} {}",
                    operacion, postulanteId, ipOrigen, String.join(" ", extras));
        }
    }
}
