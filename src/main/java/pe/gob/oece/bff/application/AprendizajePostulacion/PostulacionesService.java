package pe.gob.oece.bff.application.AprendizajePostulacion;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.PostulacionClient;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.ConfirmarPostulacionRequest;
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
        return extraerData(postulacionClient.iniciarPostulacion(postulanteId, request));
    }

    public JsonNode listarMisPostulaciones(Long postulanteId, String token, String ipOrigen) {
        audit("listarMisPostulaciones", postulanteId, ipOrigen);
        return extraerData(postulacionClient.listarMisPostulaciones(postulanteId, token));
    }

    public JsonNode listarNivelesCertificacion(String token, String ipOrigen) {
        logger.info("AUDIT op=listarNivelesCertificacion ipOrigen={}", ipOrigen);
        return extraerData(postulacionClient.listarNivelesCertificacion(token));
    }

    public JsonNode obtenerDetalle(Long idPostulacion, Long postulanteId, String ipOrigen) {
        audit("obtenerDetalle", postulanteId, ipOrigen, "idPostulacion=" + idPostulacion);
        return extraerData(postulacionClient.obtenerDetalle(idPostulacion, postulanteId));
    }

    public JsonNode agregarExperiencia(
            Long idPostulacion,
            Long postulanteId,
            MultiValueMap<String, String> request,
            MultiValueMap<String, MultipartFile> archivos,
            String token,
            String ipOrigen
    ) {
        audit("agregarExperiencia", postulanteId, ipOrigen, "idPostulacion=" + idPostulacion);
        return extraerData(postulacionClient.agregarExperiencia(idPostulacion, postulanteId, request, archivos, token));
    }

    public JsonNode editarExperiencia(
            Long idPostulacion,
            Long idExperiencia,
            Long postulanteId,
            MultiValueMap<String, String> request,
            MultiValueMap<String, MultipartFile> archivos,
            String token,
            String ipOrigen
    ) {
        audit("editarExperiencia", postulanteId, ipOrigen,
                "idPostulacion=" + idPostulacion, "idExperiencia=" + idExperiencia);
        return extraerData(postulacionClient.editarExperiencia(idPostulacion, idExperiencia, postulanteId, request, archivos, token));
    }

    public JsonNode eliminarExperiencia(
            Long idPostulacion,
            Long idExperiencia,
            Long postulanteId,
            String ipOrigen
    ) {
        audit("eliminarExperiencia", postulanteId, ipOrigen,
                "idPostulacion=" + idPostulacion, "idExperiencia=" + idExperiencia);
        return extraerData(postulacionClient.eliminarExperiencia(idPostulacion, idExperiencia, postulanteId));
    }

    public JsonNode agregarFormacion(
            MultiValueMap<String, String> request,
            MultiValueMap<String, MultipartFile> archivos,
            String token,
            String ipOrigen
    ) {
        logger.info("AUDIT op=agregarFormacion ipOrigen={}", ipOrigen);
        return extraerData(postulacionClient.agregarFormacion(request, archivos, token));
    }

    public JsonNode editarFormacion(
            Long idFormacion,
            MultiValueMap<String, String> request,
            MultiValueMap<String, MultipartFile> archivos,
            String token,
            String ipOrigen
    ) {
        logger.info("AUDIT op=editarFormacion idFormacion={} ipOrigen={}", idFormacion, ipOrigen);
        return extraerData(postulacionClient.editarFormacion(idFormacion, request, archivos, token));
    }

    public JsonNode eliminarFormacion(Long idFormacion, String ipOrigen) {
        logger.info("AUDIT op=eliminarFormacion idFormacion={} ipOrigen={}", idFormacion, ipOrigen);
        return extraerData(postulacionClient.eliminarFormacion(idFormacion));
    }

    public JsonNode seleccionarProgramacion(
            Long idPostulacion,
            Long postulanteId,
            SeleccionarProgramacionRequest request,
            String ipOrigen
    ) {
        audit("seleccionarProgramacion", postulanteId, ipOrigen, "idPostulacion=" + idPostulacion);
        return extraerData(postulacionClient.seleccionarProgramacion(idPostulacion, postulanteId, request));
    }

    public JsonNode generarOrdenPago(
            Long idPostulacion,
            Long postulanteId,
            GenerarOrdenPagoRequest request,
            String ipOrigen
    ) {
        audit("generarOrdenPago", postulanteId, ipOrigen, "idPostulacion=" + idPostulacion);
        return extraerData(postulacionClient.generarOrdenPago(idPostulacion, postulanteId, request));
    }

    public JsonNode confirmarPostulacion(
            Long idPostulacion,
            Long postulanteId,
            ConfirmarPostulacionRequest request,
            String ipOrigen
    ) {
        audit("confirmarPostulacion", postulanteId, ipOrigen, "idPostulacion=" + idPostulacion);
        return extraerData(postulacionClient.confirmarPostulacion(idPostulacion, postulanteId, request));
    }

    public JsonNode enviarSubsanacion(
            Long idPostulacion,
            MultiValueMap<String, String> request,
            MultiValueMap<String, MultipartFile> archivos,
            String token,
            String ipOrigen) {
        logger.info("AUDIT op=enviarSubsanacion idPostulacion={} ipOrigen={}", idPostulacion, ipOrigen);
        return extraerData(postulacionClient.enviarSubsanacion(idPostulacion, request, archivos, token));
    }

    public JsonNode cancelarPostulacion(JsonNode request, String ipOrigen) {
        logger.info("AUDIT op=cancelarPostulacion ipOrigen={}", ipOrigen);
        return extraerData(postulacionClient.cancelarPostulacion(request));
    }

    public JsonNode listarPorTipoLista(String tipo, String ipOrigen) {
        logger.info("AUDIT op=listarPorTipoLista tipo={} ipOrigen={}", tipo, ipOrigen);
        return extraerData(postulacionClient.listarPorTipoLista(tipo));
    }

    private JsonNode extraerData(JsonNode response) {
        if (response != null && response.has("data")) {
            return response.get("data");
        }
        return response;
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
