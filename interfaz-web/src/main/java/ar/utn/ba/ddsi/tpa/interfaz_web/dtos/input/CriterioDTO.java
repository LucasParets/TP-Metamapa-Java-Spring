package ar.utn.ba.ddsi.tpa.interfaz_web.dtos.input;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class CriterioDTO {
    private Long id;
    private String nombre_criterio;
    private Map<String, Object> parametros;

    public CriterioDTO() {
        this.parametros = new HashMap<>();
    }
}
