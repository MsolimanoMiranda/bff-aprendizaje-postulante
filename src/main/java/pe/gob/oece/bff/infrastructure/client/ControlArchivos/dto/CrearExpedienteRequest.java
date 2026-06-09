package pe.gob.oece.bff.infrastructure.client.ControlArchivos.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearExpedienteRequest {

    private String codigoConfiguracion;
    private String codigoCarpetaPlantilla;
    private String numeroExpediente;
    private String nombreCarpeta;
}
