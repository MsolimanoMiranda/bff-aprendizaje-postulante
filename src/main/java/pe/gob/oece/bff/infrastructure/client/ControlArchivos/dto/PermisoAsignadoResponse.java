package pe.gob.oece.bff.infrastructure.client.ControlArchivos.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermisoAsignadoResponse {

    private Long idPermiso;
    private Long idDocumento;
    private String nombreDocumento;
    private Long idUsuario;
    private String fechaAsignacion;
}
