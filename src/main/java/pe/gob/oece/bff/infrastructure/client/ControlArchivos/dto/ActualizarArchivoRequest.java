package pe.gob.oece.bff.infrastructure.client.ControlArchivos.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ActualizarArchivoRequest {

    private String codigoConfiguracion;
    private String uuidAlfrescoCarpeta;
    private String codigoDocumento;
    private String uuidAlfrescoDocumento;
    private Boolean confidencial;
    private List<MetadataArchivoRequest> metadatos;
}
