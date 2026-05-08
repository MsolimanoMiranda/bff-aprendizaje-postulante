package pe.gob.oece.bff.shared;

import org.slf4j.MDC;

public final class CorrelationIdContext {

    private static final String CORRELATION_ID_KEY = "correlationId";

    private CorrelationIdContext() {
    }

    public static String getCorrelationId() {
        return MDC.get(CORRELATION_ID_KEY);
    }
}