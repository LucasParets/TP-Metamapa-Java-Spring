package ar.utn.ba.ddsi.tpa.fuente_proxy.controladores;

import ar.utn.ba.ddsi.tpa.fuente_proxy.models.dtos.HechoDTO;
import ar.utn.ba.ddsi.tpa.fuente_proxy.models.entities.hecho.Hecho;
import ar.utn.ba.ddsi.tpa.fuente_proxy.servicios.IHechosService;
import ar.utn.ba.ddsi.tpa.fuente_proxy.servicios.impl.HechosService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/api/proxy/hechos")
public class HechosApisController {
    private final IHechosService hechosService;

    public HechosApisController(IHechosService hechosService) {
        this.hechosService = hechosService;
    }

    @GetMapping
    public List<HechoDTO> importarHechos(@RequestParam(required = false) String last_update){
        return hechosService.getHechos(last_update);
    }

    @DeleteMapping("/{id}")
    public void desactivarHecho(@PathVariable Long id){
        hechosService.desactivarHecho(id);
    }
}
