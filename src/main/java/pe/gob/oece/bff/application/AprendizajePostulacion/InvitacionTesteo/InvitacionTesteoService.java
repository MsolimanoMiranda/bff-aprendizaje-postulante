package pe.gob.oece.bff.application.AprendizajePostulacion.InvitacionTesteo;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pe.gob.oece.bff.infrastructure.client.AprendizajePostulacion.InvitacionTesteoClient;

@Service
@RequiredArgsConstructor
public class InvitacionTesteoService {

    private static final Logger logger = LoggerFactory.getLogger(InvitacionTesteoService.class);

    private final InvitacionTesteoClient invitacionTesteoClient;

    public JsonNode obtenerMisInvitaciones(String token, String ipOrigen) {
        logger.info("AUDIT op=obtenerMisInvitacionesTesteo ipOrigen={}", ipOrigen);
        return extraerData(invitacionTesteoClient.obtenerMisInvitaciones(token));
    }

    public JsonNode listarBancosTestDisponibles(String token, String ipOrigen) {
        logger.info("AUDIT op=listarBancosTestDisponibles ipOrigen={}", ipOrigen);
        return extraerData(invitacionTesteoClient.listarBancosTestDisponibles(token));
    }

    public JsonNode aceptarInvitacion(
            Long idInvitacion,
            String token,
            String usuario,
            String ipOrigen
    ) {
        logger.info(
                "AUDIT op=aceptarInvitacionTesteo idInvitacion={} usuario={} ipOrigen={}",
                idInvitacion,
                usuario,
                ipOrigen
        );
        return extraerData(invitacionTesteoClient.aceptarInvitacion(idInvitacion, token, usuario, ipOrigen));
    }

    public JsonNode rechazarInvitacion(
            Long idInvitacion,
            String token,
            String usuario,
            String ipOrigen
    ) {
        logger.info(
                "AUDIT op=rechazarInvitacionTesteo idInvitacion={} usuario={} ipOrigen={}",
                idInvitacion,
                usuario,
                ipOrigen
        );
        return extraerData(invitacionTesteoClient.rechazarInvitacion(idInvitacion, token, usuario, ipOrigen));
    }

    public JsonNode iniciarTesteo(
            Long idBanco,
            String token,
            String usuario,
            String ipOrigen
    ) {
        logger.info(
                "AUDIT op=iniciarTesteo idBanco={} usuario={} ipOrigen={}",
                idBanco,
                usuario,
                ipOrigen
        );
        return extraerData(invitacionTesteoClient.iniciarTesteo(idBanco, token, usuario, ipOrigen));
    }

    public JsonNode completarTesteo(
            Long idBanco,
            String token,
            String usuario,
            String ipOrigen
    ) {
        logger.info(
                "AUDIT op=completarTesteo idBanco={} usuario={} ipOrigen={}",
                idBanco,
                usuario,
                ipOrigen
        );
        return extraerData(invitacionTesteoClient.completarTesteo(idBanco, token, usuario, ipOrigen));
    }

    private JsonNode extraerData(JsonNode response) {
        if (response != null && response.has("data")) {
            return response.get("data");
        }
        return response;
    }
}
