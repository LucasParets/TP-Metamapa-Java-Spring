package ar.utn.ba.ddsi.tpa.fuente_proxy.servicios.impl;

import ar.utn.ba.ddsi.tpa.fuente_proxy.models.dtos.HechoDTO;
import ar.utn.ba.ddsi.tpa.fuente_proxy.models.entities.apis.API;
import ar.utn.ba.ddsi.tpa.fuente_proxy.models.entities.apis.OrigenHecho;
import ar.utn.ba.ddsi.tpa.fuente_proxy.models.entities.hecho.EstadoHecho;
import ar.utn.ba.ddsi.tpa.fuente_proxy.models.entities.hecho.Hecho;
import ar.utn.ba.ddsi.tpa.fuente_proxy.models.entities.repositories.ICategoriaRepository;
import ar.utn.ba.ddsi.tpa.fuente_proxy.models.entities.repositories.IHechosRepository;
import ar.utn.ba.ddsi.tpa.fuente_proxy.models.entities.utils.FechaParser;
import ar.utn.ba.ddsi.tpa.fuente_proxy.servicios.IHechosService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.List;

@Service
public class HechosService implements IHechosService {
    private final List<API> apis;
    private final ICategoriaRepository categoriaRepository;
    private final IHechosRepository hechosRepository;

    public HechosService (List<API> apis,
                          IHechosRepository hechosRepository,
                          ICategoriaRepository categoriaRepository){
        this.apis = apis;
        this.hechosRepository = hechosRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<HechoDTO> getHechos(String last_update){
        if (last_update == null) {
            return hechosRepository.findAllByEstado(EstadoHecho.ACTIVO).stream().map(this::crearHechoDTO).toList();
        }
        return hechosRepository.findSinceLastUpdate(FechaParser.of(last_update), EstadoHecho.ACTIVO)
                .stream().map(this::crearHechoDTO).toList();
    }

    public void importarHechos(){
        System.out.println("Importando hechos...");
        for (API api : apis) {
            System.out.println("Importando hechos de " + api.getNombre());
            List<Hecho> hechos = api.importarHechos()
                    .collectList()
                    .block();

            if (hechos == null) continue;

            OrigenHecho origen = api.getNombre();

            for (Hecho hecho : hechos) {
                hechosRepository.findHechoByOrigenAndIdExterno(origen, hecho.getIdExterno())
                        .ifPresentOrElse(
                                existente -> {
                                    if (existente.getFecha_modificacion().isBefore(hecho.getFecha_modificacion()) &&
                                        existente.getEstado() == EstadoHecho.ACTIVO) {
                                        hecho.setId(existente.getId());

                                        String nombreCategoria = hecho.getCategoria().getNombre().toLowerCase().trim();
                                        hecho.setCategoria(categoriaRepository.findByNombre(nombreCategoria)
                                                .orElseGet(() -> categoriaRepository.findByNombreAlterno(nombreCategoria)
                                                        .orElseGet(() -> categoriaRepository.save(hecho.getCategoria()))
                                                )
                                        );

                                        if (hecho.getDescripcion() != null && hecho.getDescripcion().length() > 1000) {
                                            hecho.setDescripcion(hecho.getDescripcion().substring(0, 997) + "...");
                                        }

                                        hechosRepository.save(hecho);
                                    }
                                },
                                () -> {
                                    String nombreCategoria = hecho.getCategoria().getNombre().toLowerCase().trim();
                                    hecho.setCategoria(categoriaRepository.findByNombre(nombreCategoria)
                                            .orElseGet(() -> categoriaRepository.findByNombreAlterno(nombreCategoria)
                                                    .orElseGet(() -> categoriaRepository.save(hecho.getCategoria()))
                                            )
                                    );

                                    hecho.setOrigen(origen);

                                    hecho.setEstado(EstadoHecho.ACTIVO);

                                    if (hecho.getDescripcion() != null && hecho.getDescripcion().length() > 1000) {
                                        hecho.setDescripcion(hecho.getDescripcion().substring(0, 997) + "...");
                                    }

                                    hechosRepository.save(hecho);
                                }
                        );
            }
        }

    }

    private HechoDTO crearHechoDTO(Hecho h) {
        HechoDTO dto = new HechoDTO();
        dto.setId(h.getId());
        dto.setTitulo(h.getTitulo());
        dto.setDescripcion(h.getDescripcion());
        dto.setCategoria(h.getCategoria().getNombre());
        dto.setFechaHecho(h.getFecha_hecho());
        dto.setFechaCarga(h.getFecha_creacion());
        dto.setFechaModificacion(h.getFecha_modificacion());
        dto.setLatitud(h.getCoordenada().getLatitud());
        dto.setLongitud(h.getCoordenada().getLongitud());
        dto.setOrigenHecho(h.getOrigen().toString());
        dto.setEstadoDelHecho(h.getEstado());
        return dto;
    }

    public void desactivarHecho(Long id) {
        hechosRepository.findById(id).ifPresent(h -> {
            h.setEstado(EstadoHecho.INACTIVO);
            hechosRepository.save(h);
        });
    }
}
