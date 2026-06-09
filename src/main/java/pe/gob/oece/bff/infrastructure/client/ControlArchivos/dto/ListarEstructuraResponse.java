package pe.gob.oece.bff.infrastructure.client.ControlArchivos.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListarEstructuraResponse {

    private Long idConfiguracion;
    private String nombreConfiguracion;
    private String tipoConfiguracion;
    private String abreviatura;
    private List<CarpetaEstructuraResponse> carpetas;
}
