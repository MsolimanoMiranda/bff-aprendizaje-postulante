package pe.gob.oece.bff.infrastructure.client.ControlArchivos.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CarpetaEstructuraResponse {

    private Long id;
    private Long idPadre;
    private String codigo;
    private String nombre;
    private String tipoNombre;
    private Boolean aplicaCantMax;
    private List<CarpetaEstructuraResponse> carpetas;
    private List<DocumentoEstructuraResponse> documentos;
}
