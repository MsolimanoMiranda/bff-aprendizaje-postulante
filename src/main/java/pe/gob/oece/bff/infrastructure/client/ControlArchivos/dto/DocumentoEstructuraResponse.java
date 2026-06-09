package pe.gob.oece.bff.infrastructure.client.ControlArchivos.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentoEstructuraResponse {

    private Long id;
    private Long idCarpeta;
    private String nombre;
    private String codigo;
    private Long pesoMax;
    private Integer cantidadMaxima;
    private String tipoArchivoPermitido;
    private Boolean obligatorio;
    private Boolean metadataObligatoria;
    private String idModeloAlfresco;
}
