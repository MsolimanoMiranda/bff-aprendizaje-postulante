package pe.gob.oece.bff.application.AprendizajePostulacion.Certificado;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.CertificacionClient;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.ActualizarCertificadoRequest;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.BusquedaCertificadoPostulanteRequest;

@RequiredArgsConstructor
@Service
public class CertificadoQueryService {
    private final CertificacionClient certificacionClient;
    public JsonNode misCertificados(Long idPostulante){
        return certificacionClient.misCertificados(idPostulante);
    }

    public JsonNode buscar(BusquedaCertificadoPostulanteRequest solicitud) {
        return certificacionClient.buscar(solicitud);
    }

    public JsonNode actualizar(Long idCertificado, ActualizarCertificadoRequest solicitud, String authorization) {
        return certificacionClient.actualizar(idCertificado, solicitud, authorization);
    }

    public JsonNode obtenerDatosDescargaPorExamen(Long idExamen, String authorization) {
        return certificacionClient.obtenerDatosDescargaPorExamen(idExamen, authorization);
    }
}
