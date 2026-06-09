package pe.gob.oece.bff.infrastructure.client.ControlArchivos.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ControlArchivosResponse<T> {

    private ControlArchivosStatus status;
    private T data;
}
