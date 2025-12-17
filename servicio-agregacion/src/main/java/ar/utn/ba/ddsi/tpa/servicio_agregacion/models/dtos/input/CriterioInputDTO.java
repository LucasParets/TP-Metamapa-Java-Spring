package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.input;

import lombok.Data;

import java.util.Map;

@Data
public class CriterioInputDTO {
    private String nombre_criterio;
    private Map<String, Object> parametros;
}
