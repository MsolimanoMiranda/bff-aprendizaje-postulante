package pe.gob.oece.bff.infrastructure.client.ControlArchivos.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearCarpetaV2Request {

    private String codigoConfiguracion;
    private String codigoCarpeta;
    private String nombreCarpeta;
    private String uuidCarpetaPadre;
}
