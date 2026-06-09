package pe.gob.oece.bff.infrastructure.client.ControlArchivos.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArchivoRegistradoResponse {

    private Long id;
    private String uuidAlfresco;
    private String nombreArchivo;
    private String extensionArchivo;
    private Long pesoArchivo;
}
