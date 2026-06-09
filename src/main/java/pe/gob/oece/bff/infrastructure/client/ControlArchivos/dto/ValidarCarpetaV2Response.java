package pe.gob.oece.bff.infrastructure.client.ControlArchivos.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidarCarpetaV2Response {
    private Boolean existe;
    private Long idCarpeta;
    private String codigoConfiguracion;
    private String codigoCarpeta;
    private String nombreCarpeta;
    private String uuidAlfresco;
}
