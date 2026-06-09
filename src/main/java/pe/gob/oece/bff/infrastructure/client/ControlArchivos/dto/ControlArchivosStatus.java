package pe.gob.oece.bff.infrastructure.client.ControlArchivos.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ControlArchivosStatus {

    private Integer code;
    private String status;
    private String message;
}
