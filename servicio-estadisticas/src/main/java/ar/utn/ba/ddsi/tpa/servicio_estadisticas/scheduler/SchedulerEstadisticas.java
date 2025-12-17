package ar.utn.ba.ddsi.tpa.servicio_estadisticas.scheduler;

import ar.utn.ba.ddsi.tpa.servicio_estadisticas.services.IAgregadorService;
import ar.utn.ba.ddsi.tpa.servicio_estadisticas.services.IEstadisticasService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SchedulerEstadisticas {
    private IAgregadorService agregadorService;

    private SchedulerEstadisticas(IAgregadorService agregadorService) {
        this.agregadorService = agregadorService;
    }

    @Scheduled(cron = "0 */3 * * * *")
    public void traerDatosYGuardarEnBD() {
        agregadorService.traerHechosDelAgregador();
        agregadorService.traerColeccionesDelAgregador();
        agregadorService.traerSolicitudesDelAgregador();
    }
}
