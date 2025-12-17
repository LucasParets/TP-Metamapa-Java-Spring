package ar.utn.ba.ddsi.tpa.servicio_agregacion.services.impl;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.output.dashboard.ColeccionDashboardDTO;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.output.dashboard.DashboardDTO;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.output.dashboard.HechoDashboardDTO;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Coleccion;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.repositories.IColeccionRepository;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.repositories.IHechosRepository;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.repositories.ISolicitudEliminacionRepository;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.services.ISolicitudEliminacionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {
    private final IColeccionRepository coleccionRepository;
    private final IHechosRepository hechosRepository;
    private final ISolicitudEliminacionRepository solicitudEliminacionRepository;

    public DashboardService(IColeccionRepository coleccionRepository,
                            IHechosRepository hechosRepository,
                            ISolicitudEliminacionRepository solicitudEliminacionRepository) {
        this.coleccionRepository = coleccionRepository;
        this.hechosRepository = hechosRepository;
        this.solicitudEliminacionRepository = solicitudEliminacionRepository;
    }

    public DashboardDTO generarDashboard() {
        DashboardDTO dto = new DashboardDTO();

        dto.setCant_hechos(hechosRepository.count());
        dto.setCant_colecciones(coleccionRepository.count());
        dto.setCant_solicitudes(solicitudEliminacionRepository.count());

        PageRequest pr = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "fechaCarga"));
        dto.setUltimos_5_hechos_agregados(hechosRepository.findAll(pr)
                .stream().map(this::hechoADTO).toList());

        PageRequest prc = PageRequest.of(0, 5);
        dto.setColecciones_top_5_x_cant_hechos(coleccionRepository.top_5_colecciones_x_cant_hechos(prc)
                .stream().map(this::coleccionADTO).toList());

        return dto;
    }

    private HechoDashboardDTO hechoADTO(Hecho h) {
        HechoDashboardDTO dto = new HechoDashboardDTO();
        dto.setId(h.getId());
        dto.setTitulo(h.getTitulo());
        dto.setFecha_carga(h.getFechaCarga());
        return dto;
    }

    private ColeccionDashboardDTO coleccionADTO(Coleccion c) {
        ColeccionDashboardDTO dto = new ColeccionDashboardDTO();
        dto.setHandle(c.getHandle());
        dto.setTitulo(c.getTitulo());
        dto.setCant_hechos(c.getHechos().size());
        return dto;
    }
}
