package ar.utn.ba.ddsi.tpa.servicio_agregacion.services.impl;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.exceptions.coleccion.ColeccionNoEncontradaException;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.input.ColeccionInputDTO;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.input.CriterioInputDTO;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.input.FiltroColecciones;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.output.ColeccionOutputDTO;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.output.CriterioOutputDTO;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.condiciones.*;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.fuentes.TipoFuente;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.*;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.origen.OrigenDelHecho;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.utilities.Coordenada;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.utilities.FechaParser;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.repositories.*;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.services.IColeccionService;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.services.IHechosService;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.utils.ColeccionesSpecs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ColeccionService implements IColeccionService {
    private final IColeccionRepository coleccionRepository;
    private final IHechosRepository hechosRepository;
    private final ICondicionRepository condicionRepository;
    private final IHechosService hechosService;
    private final ICategoriasRepository categoriasRepository;

    public ColeccionService(IColeccionRepository coleccionRepository,
                            IHechosRepository hechosRepository,
                            ICondicionRepository condicionRepository,
                            IHechosService hechosService,
                            ICategoriasRepository categoriasRepository) {
        this.coleccionRepository = coleccionRepository;
        this.hechosRepository = hechosRepository;
        this.condicionRepository = condicionRepository;
        this.hechosService = hechosService;
        this.categoriasRepository = categoriasRepository;
    }

    @Transactional(readOnly = true)
    public Page<ColeccionOutputDTO> mostrarColecciones(FiltroColecciones f, Pageable pg) {
        log.info("Buscando colecciones para mostrar según filtros y página.");
        Specification<Coleccion> specs = ColeccionesSpecs.filtros(f);
        return coleccionRepository.findAll(specs, pg).map(this::coleccionADTO);
    }

    @Transactional(readOnly = true)
    public ColeccionOutputDTO mostrarColeccion(String handle) {
        log.info("Buscando coleccion con handle: {}", handle);
        Coleccion coleccion = coleccionRepository.findById(handle)
                .orElseThrow(() -> new ColeccionNoEncontradaException("No se ha encontrado la coleccion con handle: " + handle));
        return this.coleccionADTO(coleccion);
    }

    @Transactional
    public String crearColeccion(ColeccionInputDTO dto) {
        log.info("Creando colección nueva...");
        Coleccion coleccion = new Coleccion(dto.getTitulo(), dto.getDescripcion());
        coleccion.agregarConsenso(dto.getConsenso());

        while (coleccionRepository.existsById(coleccion.getHandle())) {
            coleccion.setHandle(coleccion.getHandle() + "_1");
        }

        if (dto.getFuentes() != null) {
            dto.getFuentes().forEach(coleccion::agregarFuentePermitida);
        }

        coleccionRepository.save(coleccion);

        log.info("Se ha creado una colección nueva con handle: {}", coleccion.getHandle());

        return coleccion.getHandle();
    }

    @Transactional
    public void eliminarColeccion(String handle) {
        coleccionRepository.findById(handle)
                .orElseThrow(() -> new ColeccionNoEncontradaException("No se ha encontrado la coleccion con handle: " + handle));
        coleccionRepository.deleteById(handle);
        log.info("Se ha eliminado la colección con handle: {}", handle);
    }

    @Transactional
    public String modificarColeccion(String handle, ColeccionInputDTO dto) {
        log.info("Iniciando modificación de la colección con handle: {}", handle);
        Coleccion coleccion = coleccionRepository.findById(handle)
                .orElseThrow(() -> new ColeccionNoEncontradaException("No se ha encontrado la coleccion con handle: " + handle));
        if (dto.getTitulo() != null) {
            coleccion.setTitulo(dto.getTitulo());
        }
        if (dto.getDescripcion() != null) {
            coleccion.setDescripcion(dto.getDescripcion());
        }
        if (dto.getConsenso() != null) {
            coleccion.agregarConsenso(dto.getConsenso());
        }
        else coleccion.setAlgoritmoConsenso(null);

        if (dto.getFuentes() != null && hanCambiadoLasFuentes(coleccion, dto)) {
            coleccion.getFuentesPermitidas().clear();

            for (String nombreFuente : dto.getFuentes()) {
                try {
                    coleccion.agregarFuentePermitida(nombreFuente);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Tipo de fuente no válido: " + nombreFuente);
                }
            }
            return handle;
        }
        else {
            coleccionRepository.save(coleccion);
            return null;
        }
    }

    private boolean hanCambiadoLasFuentes(Coleccion coleccion, ColeccionInputDTO dto) {
        if (dto.getFuentes() == null || dto.getFuentes().isEmpty()) {
            return !coleccion.getFuentesPermitidas().isEmpty();
        }

        if (coleccion.getFuentesPermitidas().size() != dto.getFuentes().size()) {
            return true;
        }

        Set<String> fuentesActuales = coleccion.getFuentesPermitidas()
                .stream()
                .map(Enum::name)
                .collect(Collectors.toSet());

        return !new HashSet<>(dto.getFuentes())
                .stream()
                .map(String::toUpperCase)
                .map(StringUtils::stripAccents)
                .collect(Collectors.toSet())
                .equals(fuentesActuales);
    }


    private ColeccionOutputDTO coleccionADTO(Coleccion c) {
        ColeccionOutputDTO dto = new ColeccionOutputDTO();
        dto.setHandle(c.getHandle());
        dto.setTitulo(c.getTitulo());
        dto.setDescripcion(c.getDescripcion());
        dto.setEsDestacada(c.isEsDestacado());
        if (c.getAlgoritmoConsenso() != null) {
            dto.setConsenso(c.getAlgoritmoConsenso().getClass().getSimpleName());
        }
        else dto.setConsenso(null);
        dto.setFuentes(c.getFuentesPermitidas().stream().map(TipoFuente::toString).toList());
        return dto;
    }

    @Transactional(readOnly = true)
    public List<HechoOutputDTO> mostrarHechos(String handle) {
        log.info("Buscando los hechos de la colección con handle: {}", handle);
        Coleccion coleccion = coleccionRepository.findById(handle)
                .orElseThrow(() -> new ColeccionNoEncontradaException("No se ha encontrado la coleccion con handle: " + handle));
        return coleccion.getHechos().stream().map(hechosService::hechoADTO).toList();
    }

    @Transactional
    public List<CriterioOutputDTO> mostrarCriterios(String handle) {
        log.info("Buscando criterios de la colección con handle: {}", handle);
        return condicionRepository.criteriosDeUnaColeccion(handle).stream().map(this::criterioADTO).toList();
    }

    private CriterioOutputDTO criterioADTO(Condicion c) {
        CriterioOutputDTO dto = new CriterioOutputDTO();
        dto.setId(c.getId());

        if (c instanceof CondicionDespuesDeXFechaHecho) {
            dto.setNombre_criterio("fecha_hecho_desde");
            dto.addParametro("fecha_hecho", ((CondicionDespuesDeXFechaHecho) c).getFechaLimite());
        } else if (c instanceof CondicionAntesDeXFechaHecho) {
            dto.setNombre_criterio("fecha_hecho_hasta");
            dto.addParametro("fecha_hecho", ((CondicionAntesDeXFechaHecho) c).getFechaLimite());
        } else if (c instanceof CondicionFechaHechoExacta) {
            dto.setNombre_criterio("fecha_hecho_exacta");
            dto.addParametro("fecha_hecho", ((CondicionFechaHechoExacta) c).getFecha());
        } else if (c instanceof CondicionIntervaloDeFechasHecho) {
            dto.setNombre_criterio("fecha_hecho_intervalo");
            dto.addParametro("fecha_inicio", ((CondicionIntervaloDeFechasHecho) c).getFechaInicio());
            dto.addParametro("fecha_fin", ((CondicionIntervaloDeFechasHecho) c).getFechaFin());
        } else if (c instanceof CondicionDespuesDeXFechaCarga) {
            dto.setNombre_criterio("fecha_carga_desde");
            dto.addParametro("fecha_carga", ((CondicionDespuesDeXFechaCarga) c).getFechaLimite());
        } else if (c instanceof CondicionAntesDeXFechaCarga) {
            dto.setNombre_criterio("fecha_carga_hasta");
            dto.addParametro("fecha_carga", ((CondicionAntesDeXFechaCarga) c).getFechaLimite());
        } else if (c instanceof CondicionFechaCargaExacta) {
            dto.setNombre_criterio("fecha_carga_exacta");
            dto.addParametro("fecha_carga", ((CondicionFechaCargaExacta) c).getFecha());
        } else if (c instanceof CondicionIntervaloDeFechasCarga) {
            dto.setNombre_criterio("fecha_carga_intervalo");
            dto.addParametro("fecha_inicio", ((CondicionIntervaloDeFechasCarga) c).getFechaInicio());
            dto.addParametro("fecha_fin", ((CondicionIntervaloDeFechasCarga) c).getFechaFin());
        } else if (c instanceof CondicionEstadoActivo) {
            dto.setNombre_criterio("estado_hecho");
        } else if (c instanceof CondicionCategoriaExacta) {
            dto.setNombre_criterio("coincide_categoria");
            dto.addParametro("categoria", ((CondicionCategoriaExacta) c).getCategoria().getNombre());
        } else if (c instanceof CondicionTituloExacto) {
            dto.setNombre_criterio("coincide_titulo");
            dto.addParametro("titulo", ((CondicionTituloExacto) c).getTitulo());
        } else if (c instanceof CondicionFuenteDiferente) {
            dto.setNombre_criterio("distinta_fuente");
            dto.addParametro("fuente", ((CondicionFuenteDiferente) c).getOrigen());
        } else if (c instanceof CondicionDescripcionContieneCadena) {
            dto.setNombre_criterio("contiene_cadena");
            dto.addParametro("cadena", ((CondicionDescripcionContieneCadena) c).getCadena());
        } else if (c instanceof CondicionContieneEtiqueta) {
            dto.setNombre_criterio("contiene_etiqueta");
            dto.addParametro("etiqueta", ((CondicionContieneEtiqueta) c).getEtiqueta());
        } else if (c instanceof CondicionRangoDistancia) {
            dto.setNombre_criterio("rango_distancia");
            dto.addParametro("latitud", ((CondicionRangoDistancia) c).getLatitud());
            dto.addParametro("longitud", ((CondicionRangoDistancia) c).getLongitud());
            dto.addParametro("rango_distancia", ((CondicionRangoDistancia) c).getRadioEnKm());
        }

        return dto;
    }

    public String agregarCriterio(String handle, CriterioInputDTO dto) {
        Coleccion coleccion = coleccionRepository.findById(handle)
                .orElseThrow(() -> new ColeccionNoEncontradaException(handle));
        Condicion criterio = reconocerCriterioDeUnDTO(dto);
        coleccion.agregarCriterio(criterio);
        condicionRepository.save(criterio);
        log.info("Se ha agregado un criterio a la colección con handle: {}.", handle);
        return handle;
    }

    public String quitarCriterio(String handle, Long idCriterio) {
        Coleccion coleccion = coleccionRepository.findById(handle)
                .orElseThrow(() -> new ColeccionNoEncontradaException(handle));
        Condicion condicion = condicionRepository.findById(idCriterio)
                .orElseThrow(() -> new IllegalArgumentException("No se ha encontrado el criterio con id: " + idCriterio));
        coleccion.quitarCriterio(condicion);
        condicionRepository.deleteById(idCriterio);
        log.info("Se ha quitado el criterio con id {} de la colección con handle {}.", idCriterio, handle);
        return handle;
    }

    public boolean existeElAlgoritmoDeConsenso(String algoritmo) {
        return algoritmo.equals("Absoluta") ||
                algoritmo.equals("MayoriaSimple") ||
                algoritmo.equals("MultiplesMenciones");
    }

    public Condicion reconocerCriterioDeUnDTO(CriterioInputDTO dto) {
        switch (dto.getNombre_criterio()) {
            case "fecha_hecho_desde":
                return new CondicionDespuesDeXFechaHecho(FechaParser.of((String) dto.getParametros().get("fecha_hecho")));
            case "fecha_hecho_hasta":
                return new CondicionAntesDeXFechaHecho(FechaParser.of((String) dto.getParametros().get("fecha_hecho")));
            case "fecha_hecho_exacta":
                return new CondicionFechaHechoExacta(FechaParser.of((String) dto.getParametros().get("fecha_hecho")));
            case "fecha_hecho_intervalo":
                return new CondicionIntervaloDeFechasHecho(FechaParser.of((String) dto.getParametros().get("fecha_inicio")),
                        FechaParser.of((String) dto.getParametros().get("fecha_fin")));
            case "fecha_carga_desde":
                return new CondicionDespuesDeXFechaCarga(FechaParser.of((String) dto.getParametros().get("fecha_carga")));
            case "fecha_carga_hasta":
                return new CondicionAntesDeXFechaCarga(FechaParser.of((String) dto.getParametros().get("fecha_carga")));
            case "fecha_carga_exacta":
                return new CondicionFechaCargaExacta(FechaParser.of((String) dto.getParametros().get("fecha_carga")));
            case "fecha_carga_intervalo":
                return new CondicionIntervaloDeFechasCarga(FechaParser.of((String) dto.getParametros().get("fecha_inicio")),
                        FechaParser.of((String) dto.getParametros().get("fecha_fin")));
            case "estado_hecho":
                return new CondicionEstadoActivo();
            case "coincide_categoria":
                return new CondicionCategoriaExacta(this.categoriasRepository.findByNombre((String) dto.getParametros().get("categoria"))
                        .orElseThrow(() -> new IllegalArgumentException("No se ha encontrado la categoria con id: " + dto.getParametros().get("categoria"))));
            case "coincide_titulo":
                return new CondicionTituloExacto((String) dto.getParametros().get("titulo"));
            case "distinta_fuente":
                return new CondicionFuenteDiferente((OrigenDelHecho) dto.getParametros().get("fuente"));
            case "contiene_cadena":
                return new CondicionDescripcionContieneCadena((String) dto.getParametros().get("cadena"));
            case "contiene_etiqueta":
                return new CondicionContieneEtiqueta((String) dto.getParametros().get("etiqueta"));
            case "rango_distancia":
                return new CondicionRangoDistancia(
                        new Coordenada(
                                Float.parseFloat((String) dto.getParametros().get("latitud")),
                                Float.parseFloat((String) dto.getParametros().get("longitud"))
                        ),
                        Double.parseDouble((String) dto.getParametros().get("rango_distancia"))
                );
            default:
                // Armar una excepción para este caso
                throw new IllegalArgumentException("El criterio " + dto.getNombre_criterio() + " no existe");
        }
    }


    public void destacarColeccion(String handle) {
        coleccionRepository.cambiarEsDestacado(handle, true);
        log.info("Se destacó la colección con handle: {}.", handle);
    }

    public void quitarDestacado(String handle) {
        coleccionRepository.cambiarEsDestacado(handle, false);
        log.info("Se quitó de destacados la colección con handle: {}.", handle);
    }



    private void refrescarColeccionUtil(Coleccion c) {
        log.info("Iniciando refresh de la colección con handle: {}.", c.getHandle());
        CondicionEstadoActivo estadoActivo = new CondicionEstadoActivo();

        Specification<Hecho> spec = Specification.where(estadoActivo.cumple());

        Set<Condicion> criterios = c.getCriteriosDePertenencia();

        Set<TipoFuente> fuentes = c.getFuentesPermitidas();

        if (fuentes != null && !fuentes.isEmpty()) {
            Specification<Hecho> specFuentes = null;
            for (TipoFuente f : fuentes) {
                CondicionCoincideTipoDeFuente fuente = new CondicionCoincideTipoDeFuente(f);
                specFuentes = specFuentes != null ? specFuentes.or(fuente.cumple()) : fuente.cumple();
            }
            if (specFuentes != null)
                spec = spec.and(specFuentes);
        }

        if (criterios != null && !criterios.isEmpty()) {
            Specification<Hecho> catSpecs = null;
            for (Condicion cond : criterios) {
                if (cond instanceof CondicionCategoriaExacta cr) {
                    System.out.println("CondicionCategoriaExacta");
                    catSpecs = catSpecs != null ? catSpecs.or(cr.cumple()) : cr.cumple();
                }
                else {
                    spec = spec.and(cond.cumple());
                }
            }
            if (catSpecs != null) spec = spec.and(catSpecs);
        }

        List<Hecho> hechos = hechosRepository.findAll(spec);
        c.refrescarHechos(hechos);
        this.coleccionRepository.save(c);
    }

    @Async("refrescarColeccion")
    @Transactional
    public void refrescarColeccionesPorFuente(TipoFuente fuente) {
        log.info("Iniciando refresh de las colecciones con fuente: {}.", fuente);
        List<Coleccion> colecciones = this.coleccionRepository.coleccionesSegunFuente(fuente);
        for (Coleccion c : colecciones) {
            refrescarColeccionUtil(c);
        }
    }

    @Async("refrescarColeccion")
    @Transactional
    public void refrescarColeccion(String handle) {
        Coleccion c = this.coleccionRepository.findById(handle)
                .orElseThrow(() -> new ColeccionNoEncontradaException(handle));
        refrescarColeccionUtil(c);
    }


//    @Override
//    @Transactional
//    public void refrescarColecciones() {
//        List<Coleccion> colecciones = this.coleccionRepository.findAll();
//        if (colecciones.isEmpty()) return;
//        colecciones.forEach(c -> {
//            List<Hecho> hechos = this.hechosRepository.findAll()
//                    .stream().filter(c::sePuedeAgregarHecho)
//                    .toList();
//            c.refrescarHechos(hechos);
//            this.coleccionRepository.save(c);
//        });
//    }
}
