package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.input;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.utilities.Coordenada;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Data
public class FiltroHechosMetamapa {
    private String q;
    private String categoria;
    private String modo_navegacion;
    private LocalDateTime fecha_carga_desde;
    private LocalDateTime fecha_carga_hasta;
    private LocalDateTime fecha_hecho_desde;
    private LocalDateTime fecha_hecho_hasta;
}
