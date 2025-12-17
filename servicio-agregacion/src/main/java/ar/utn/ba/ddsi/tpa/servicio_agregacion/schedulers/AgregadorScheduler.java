package ar.utn.ba.ddsi.tpa.servicio_agregacion.schedulers;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.georef.GeorefBatchResponse;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.georef.GeorefRequestInfo;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.georef.GeorefResponse;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.georef.GeorefUbicacion;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.fuentes.TipoFuente;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Coleccion;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.utilities.Departamento;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.utilities.Provincia;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.repositories.IColeccionRepository;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.repositories.IDepartamentoRepository;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.repositories.IHechosRepository;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.repositories.IProvinciaRepository;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.services.IColeccionService;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.services.IFuentesService;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.services.ISolicitudEliminacionService;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.services.impl.ConsensoService;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.services.impl.GeorefService;
import datadog.trace.api.Trace;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class AgregadorScheduler {
    private final IFuentesService fuentesService;
    private final IColeccionRepository coleccionRepository;
    private final IColeccionService coleccionService;
    private final IHechosRepository hechosRepository;
    private final IProvinciaRepository provinciaRepository;
    private final IDepartamentoRepository departamentoRepository;
    private final ISolicitudEliminacionService solicitudEliminacionService;
    private final ConsensoService consensoService;
    private final GeorefService georefService;

    @Trace(operationName = "scheduler.importar_hechos_fuente_estatica", resourceName = "importar_hechos_de_fuente_estatica")
    @Scheduled(cron = "0 */3 * * * *")
    public void importarHechosDeFuenteEstatica() {
        try {
            log.info("Iniciando importacion de hechos desde fuente estatica");
            if (fuentesService.traerHechosDeFuenteEstatica())
                coleccionService.refrescarColeccionesPorFuente(TipoFuente.ESTATICA);
        }
        catch (Exception e) {
            log.error("Error durante la importacion de hechos desde fuente estatica: {}", e.getMessage());
        }
    }

    @Trace(operationName = "scheduler.importar_hechos_fuente_dinamica", resourceName = "importar_hechos_de_fuente_dinamica")
    @Scheduled(cron = "0 */2 * * * *")
    public void importarHechosDeFuenteDinamica() {
        try {
            log.info("Iniciando importacion de hechos desde fuente dinamica");
            if (fuentesService.traerHechosDeFuenteDinamica())
                coleccionService.refrescarColeccionesPorFuente(TipoFuente.DINAMICA);
        }
        catch (Exception e) {
            log.error("Error durante la importacion de hechos desde fuente dinamica: {}", e.getMessage());
        }
    }

    @Trace(operationName = "scheduler.importar_hechos_fuente_proxy", resourceName = "importar_hechos_de_fuente_proxy")
    @Scheduled(cron = "0 */5 * * * *")
    public void importarHechosDeFuenteProxy() {
        try {
            log.info("Iniciando importacion de hechos desde fuente proxy");
            if (fuentesService.traerHechosDeFuenteProxy())
                coleccionService.refrescarColeccionesPorFuente(TipoFuente.PROXY);
        }
        catch (Exception e) {
            log.error("Error durante la importacion de hechos desde fuente proxy: {}", e.getMessage());
        }
    }

    @Scheduled(cron = "0 */5 * * * *")
    @Transactional
    public void refrescarHechosConsensuados() {
        try {
            log.info("Iniciando actualizacion de hechos consensuados");

            List<Coleccion> colecciones = coleccionRepository.coleccionConConsenso();
            log.info("Encontradas {} colecciones con consenso para actualizar", colecciones.size());

            for (Coleccion coleccion : colecciones) {
                try {
                    log.info("Aplicando consenso a colección: {}", coleccion.getHandle());
                    consensoService.aplicarConsenso(coleccion);
                } catch (Exception e) {
                    log.error("Error al aplicar consenso a la colección {}: {}",
                            coleccion.getHandle(), e.getMessage());
                }
            }
            log.info("Finalizada la actualización de hechos consensuados");
        } catch (Exception e) {
            log.error("Error durante la actualización de hechos consensuados: {}", e.getMessage());
        }
    }

    @Trace(operationName = "scheduler.pendientes_geocodificacion_inversa", resourceName = "geocodificacion_inversa_pendientes")
    @Scheduled(cron = "0 */5 * * * *")
    @Transactional
    public void pendientesGeocodificacionInversa() {
        List<Hecho> pendientes = hechosRepository.findTop500ByGeocodificadoIsFalse();

        if (pendientes.isEmpty())
            return;

        List<GeorefRequestInfo> ubicaciones = pendientes.stream()
                .map(h -> new GeorefRequestInfo(
                        (double) h.getCoordenada().getLatitud(),
                        (double) h.getCoordenada().getLongitud()))
                .toList();

        GeorefBatchResponse respuesta;
        try {
            log.info("Iniciando geocodificacion inversa de {} hechos pendientes", pendientes.size());
            respuesta = georefService.geocodificacionInversa(ubicaciones);
        }
        catch (Exception e) {
            log.error("Error durante la geocodificacion inversa de los hechos pendientes: {}", e.getMessage());
            return;
        }

        List<GeorefResponse> resultados = respuesta.getResultados();
        if (resultados == null || resultados.isEmpty()) {
            return;
        }

        int size = Math.min(pendientes.size(), resultados.size());

        for (int i = 0; i < size; i++) {
            Hecho h = pendientes.get(i);
            GeorefUbicacion u = resultados.get(i).getUbicacion();

            if (u == null) continue;

            String provinciaId = u.getProvincia_id();
            String provinciaNombre = u.getProvincia_nombre();

            if (provinciaId == null) {
                log.warn("Georef no devolvió provincia para hecho {} (lat={}, lon={})",
                        h.getId(), h.getCoordenada().getLatitud(), h.getCoordenada().getLongitud());
            } else {
                Provincia provincia = provinciaRepository.findById(provinciaId)
                        .orElseGet(() -> provinciaRepository.save(
                                new Provincia(provinciaId, provinciaNombre)));
                h.setProvincia(provincia);
            }

            String deptoId = u.getDepartamento_id();
            String deptoNombre = u.getDepartamento_nombre();

            if (deptoId == null) {
                log.warn("Georef no devolvió departamento para hecho {} (lat={}, lon={})",
                        h.getId(), h.getCoordenada().getLatitud(), h.getCoordenada().getLongitud());
            } else {
                Departamento departamento = departamentoRepository.findById(deptoId)
                        .orElseGet(() -> departamentoRepository.save(
                                    new Departamento(deptoId, deptoNombre)));
                h.setDepartamento(departamento);
            }

            h.setFechaModificacion(LocalDateTime.now());
            h.setGeocodificado(true);
        }
    }

    @Trace(operationName = "scheduler.detectar_spam", resourceName = "deteccion_de_spam")
    @Scheduled(cron = "0 */2 * * * *")
    @Transactional
    public void detectarSpam() {
        solicitudEliminacionService.detectarSpam();
    }
}
