package pe.gob.oece.bff.infrastructure.client.ControlArchivos.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ValidarCarpetaV2Request {
    private String codigoConfiguracion;
    private String codigoCarpeta;
    private String nombreCarpeta;
    private String uuidCarpetaPadre;
}
