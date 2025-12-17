package ar.utn.ba.ddsi.tpa.interfaz_web.dtos.input;

import java.util.List;
import lombok.Data;

@Data
public class ColeccionInputDTO {
    private String titulo;
    private String descripcion;
    private List<String> fuentes;
    private String consenso;
}
