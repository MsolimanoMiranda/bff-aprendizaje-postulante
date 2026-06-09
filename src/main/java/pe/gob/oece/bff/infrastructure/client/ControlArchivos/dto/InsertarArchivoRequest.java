package pe.gob.oece.bff.infrastructure.client.ControlArchivos.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InsertarArchivoRequest {

    private String codigoConfiguracion;
    private String uuidAlfrescoCarpeta;
    private String codigoDocumento;
    private Boolean confidencial;
    private List<MetadataArchivoRequest> metadatos;
}
