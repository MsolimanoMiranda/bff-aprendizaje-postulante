package pe.gob.oece.bff.infrastructure.client.ControlArchivos.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidarUuidResponse {

    private String uuid;
    private String nombre;
    private Boolean esCarpeta;
    private Boolean esArchivo;
    private String mimeType;
}
