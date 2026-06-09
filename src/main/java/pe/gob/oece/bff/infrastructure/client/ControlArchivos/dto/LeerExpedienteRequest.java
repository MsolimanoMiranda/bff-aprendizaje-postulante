package pe.gob.oece.bff.infrastructure.client.ControlArchivos.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LeerExpedienteRequest {

    private Long idExpediente;
    private String codigoConfiguracion;
    private String codigoCarpeta;
}
