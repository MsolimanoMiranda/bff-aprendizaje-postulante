package pe.gob.oece.bff.infrastructure.client.ControlArchivos.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class LeerExpedienteResponse {

    private ControlArchivosStatus status;
    private Map<String, Object> data;
}
