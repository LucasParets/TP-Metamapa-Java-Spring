package ar.utn.ba.ddsi.tpa.fuente_proxy.schedulers;

import ar.utn.ba.ddsi.tpa.fuente_proxy.servicios.IHechosService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class FuentesScheduler {
    private final IHechosService hechosService;

    public FuentesScheduler(IHechosService hechosService) {
        this.hechosService = hechosService;
    }

    @Scheduled(cron = "0 0 2 * * *")
    public void importarHechos() {
        this.hechosService.importarHechos();
    }
}
