package ar.utn.ba.ddsi.tpa.servicio_agregacion.services.impl;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.fuentes.TipoFuente;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.utilities.InstantSyncFuente;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.repositories.IInstantSyncFuenteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;

@Service
public class SyncService {
    private final IInstantSyncFuenteRepository repo;

    public SyncService(IInstantSyncFuenteRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public LocalDateTime getSince(TipoFuente fuente) {
        return repo.findByTipoFuente(fuente)
                .map(InstantSyncFuente::getUltimoSync)
                .orElse(LocalDateTime.of(1970,1,1,0,0));
    }

    @Transactional
    public void bump(TipoFuente fuente, LocalDateTime maxUpdated) {
        InstantSyncFuente sync = repo.findByTipoFuente(fuente)
                .orElseGet(() -> { var x = new InstantSyncFuente(); x.setTipoFuente(fuente); return x; });
        if (maxUpdated != null && (sync.getUltimoSync() == null || maxUpdated.isAfter(sync.getUltimoSync()))) {
            sync.setUltimoSync(maxUpdated.plusSeconds(1));
            repo.save(sync);
        }
    }
}
