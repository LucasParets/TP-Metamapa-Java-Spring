package ar.utn.ba.ddsi.tpa.fuente_proxy.servicios.impl;

import ar.utn.ba.ddsi.tpa.fuente_proxy.models.dtos.metamapa.*;
import ar.utn.ba.ddsi.tpa.fuente_proxy.models.entities.metamapa.MetaMapa;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class MetaMapaService {
    private final List<MetaMapa> instanciasMetamapa = new ArrayList<>();

    public MetaMapaService(@Value("${metamapa.api.urls}") List<String> urls){
        for (String url : urls) {
            this.instanciasMetamapa.add(new MetaMapa(url));
        }
    }

    public Page<ColeccionDTO> importarColecciones(Pageable pg){
        List<ColeccionDTO> colecciones = new ArrayList<>(instanciasMetamapa.stream()
                .map(MetaMapa::importarColecciones)
                .flatMap(List::stream)
                .toList());

        colecciones.sort(Comparator.comparing(ColeccionDTO::getTitulo));

        int start = (int) pg.getOffset();
        int end = Math.min(start + pg.getPageSize(), colecciones.size());

        List<ColeccionDTO> content = start >= end
                ? List.of()
                : colecciones.subList(start, end);

        return new PageImpl<>(content, pg, colecciones.size());
    }

    public ColeccionDTO importarColeccion(String handle, String metamapa){
        MetaMapa mm = getInstanciaMetamapa(metamapa);
        ColeccionDTO c = mm.importarColeccion(handle);
        c.setInstanciaMetamapa(metamapa);
        return c;
    }

    public Page<HechoMetamapa> importarHechosDeColeccion(String h, String metamapa,
                                                         Pageable pg, FiltrosDTO f){
        MetaMapa mm = getInstanciaMetamapa(metamapa);
        PageResponse<HechoMetamapa> hechos = mm.importarHechosDeColeccion(h, f, pg);
        List<HechoMetamapa> content = hechos.getContent().stream().map(hecho -> {
            hecho.setFuente("METAMAPA");
            hecho.setOrigenHecho(metamapa);
            return hecho;
        }).toList();
        return new PageImpl<>(content, pg, hechos.getTotalElements());
    }

    public HechoMetamapa getHecho(Long id, String metamapa) {
        MetaMapa mm = getInstanciaMetamapa(metamapa);
        return mm.importarHecho(id);
    }

    private MetaMapa getInstanciaMetamapa(String metamapa){
        for (MetaMapa mm : instanciasMetamapa) {
            if (mm.getUrl().equals(metamapa)) return mm;
        }
        throw new IllegalArgumentException("No existe una instancia de MetaMapa con la URL " + metamapa);
    }

    public void crearSolicitud(SolicitudDTO s, String metamapa) {
        MetaMapa mm = getInstanciaMetamapa(metamapa);
        mm.enviarSolicitudDeEliminacion(s);
    }
}