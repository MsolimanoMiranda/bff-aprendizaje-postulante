package pe.gob.oece.bff.application.AprendizajePostulacion;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.PublicoClient;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.BusquedaCertificadoRequest;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.BusquedaProgramacionRequest;

@Service
public class PublicoService {

    private static final Logger logger = LoggerFactory.getLogger(PublicoService.class);

    private final PublicoClient publicoClient;

    public PublicoService(PublicoClient publicoClient) {
        this.publicoClient = publicoClient;
    }

    public JsonNode buscarProgramaciones(BusquedaProgramacionRequest solicitud, String ipOrigen) {
        logger.info("AUDIT op=buscarProgramaciones ipOrigen={}", ipOrigen);
        return publicoClient.buscarProgramaciones(solicitud);
    }

    public JsonNode buscarCertificados(BusquedaCertificadoRequest solicitud, String ipOrigen) {
        logger.info("AUDIT op=buscarCertificados ipOrigen={}", ipOrigen);
        return publicoClient.buscarCertificados(solicitud);
    }

    public JsonNode obtenerDetalleCertificado(Long idCertificado, String ipOrigen) {
        logger.info("AUDIT op=obtenerDetalleCertificado idCertificado={} ipOrigen={}",
                idCertificado, ipOrigen);
        return publicoClient.obtenerDetalleCertificado(idCertificado);
    }
}
