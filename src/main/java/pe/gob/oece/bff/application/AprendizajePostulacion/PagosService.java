package pe.gob.oece.bff.application.AprendizajePostulacion;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.PagosClient;

@Service
public class PagosService {

    private static final Logger logger = LoggerFactory.getLogger(PagosService.class);

    private final PagosClient pagosClient;

    public PagosService(PagosClient pagosClient) {
        this.pagosClient = pagosClient;
    }

    public JsonNode obtenerTasa(String bearerToken, String ipOrigen) {
        logger.info("AUDIT op=obtenerTasa ipOrigen={}", ipOrigen);
        return pagosClient.obtenerTasa(bearerToken);
    }

    public JsonNode callbackNiubiz(
            Long   idPostulacion,
            String purchaseNumber,
            String status,
            String motivoDenegacion,
            String currency,
            String transactionDate,
            String montoAprobado,
            String trama,
            String ipOrigen
    ) {
        logger.info("AUDIT op=callbackNiubizGet idPostulacion={} purchaseNumber={} status={} ipOrigen={}",
                idPostulacion, purchaseNumber, status, ipOrigen);
        return pagosClient.callbackNiubiz(
                idPostulacion, purchaseNumber, status,
                motivoDenegacion, currency, transactionDate, montoAprobado, trama
        );
    }

    public JsonNode callbackNiubizPost(
            Long   idPostulacion,
            String purchaseNumber,
            String status,
            String motivoDenegacion,
            String transactionDate,
            String montoAprobado,
            String trama,
            String ipOrigen
    ) {
        logger.info("AUDIT op=callbackNiubizPost idPostulacion={} purchaseNumber={} status={} ipOrigen={}",
                idPostulacion, purchaseNumber, status, ipOrigen);
        return pagosClient.callbackNiubizPost(
                idPostulacion, purchaseNumber, status,
                motivoDenegacion, transactionDate, montoAprobado, trama
        );
    }
}