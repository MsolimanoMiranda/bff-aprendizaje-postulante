package pe.gob.oece.bff.infrastructure.client.ControlArchivos.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearCarpetaResponse {

    private Long id;
    private String uuidCarpeta;
    private String fechaReg;
}
