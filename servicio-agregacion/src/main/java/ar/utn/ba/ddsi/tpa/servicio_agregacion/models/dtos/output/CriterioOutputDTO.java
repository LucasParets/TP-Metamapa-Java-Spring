package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.output;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class CriterioOutputDTO {
    private Long id;
    private String nombre_criterio;
    private Map<String, Object> parametros;

    public CriterioOutputDTO() {
        this.parametros = new HashMap<>();
    }

    public void addParametro(String k, Object v) {
        this.parametros.put(k, v);
    }
}
