package pe.gob.oece.bff.application.AprendizajePostulacion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.PagosClient;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.NiubizCallbackRequest;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.dto.RespuestaSimple;

@Service
public class PagosService {

    private static final Logger logger = LoggerFactory.getLogger(PagosService.class);

    private final PagosClient pagosClient;

    public PagosService(PagosClient pagosClient) {
        this.pagosClient = pagosClient;
    }

    public RespuestaSimple callbackNiubiz(
            String purchaseNumber,
            String status,
            String motivoDenegacion,
            String currency,
            String transactionDate,
            String montoAprobado,
            String ipOrigen
    ) {
        logger.info("AUDIT op=callbackNiubizGet purchaseNumber={} status={} ipOrigen={}",
                purchaseNumber, status, ipOrigen);
        return pagosClient.callbackNiubiz(
                purchaseNumber, status, motivoDenegacion, currency, transactionDate, montoAprobado
        );
    }

    public RespuestaSimple callbackNiubizPost(
            NiubizCallbackRequest body,
            String purchaseNumber,
            String status,
            String motivoDenegacion,
            String transactionDate,
            String montoAprobado,
            String ipOrigen
    ) {
        logger.info("AUDIT op=callbackNiubizPost purchaseNumber={} status={} ipOrigen={}",
                purchaseNumber, status, ipOrigen);
        return pagosClient.callbackNiubizPost(
                body, purchaseNumber, status, motivoDenegacion, transactionDate, montoAprobado
        );
    }
}
