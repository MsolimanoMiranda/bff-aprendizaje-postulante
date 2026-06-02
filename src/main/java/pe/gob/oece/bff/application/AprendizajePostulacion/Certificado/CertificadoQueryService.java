package pe.gob.oece.bff.application.AprendizajePostulacion.Certificado;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.CertificacionClient;

@RequiredArgsConstructor
@Service
public class CertificadoQueryService {
    private final CertificacionClient certificacionClient;
    public JsonNode misCertificados(Long idPostulante){
        return certificacionClient.misCertificados(idPostulante);
    }
}
