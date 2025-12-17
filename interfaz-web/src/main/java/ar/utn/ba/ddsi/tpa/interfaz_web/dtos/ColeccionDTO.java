package ar.utn.ba.ddsi.tpa.interfaz_web.dtos;

import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.input.CriterioDTO;
import lombok.Data;

import java.util.List;

@Data
public class ColeccionDTO {
    private String handle;
    private String titulo;
    private String descripcion;
    private List<String> fuentes;
    private String consenso;
    private Boolean esDestacada;
}
