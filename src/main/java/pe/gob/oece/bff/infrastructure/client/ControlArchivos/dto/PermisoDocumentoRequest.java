package pe.gob.oece.bff.infrastructure.client.ControlArchivos.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermisoDocumentoRequest {

    private Long idDocumento;
    private Long idUsuario;
}
