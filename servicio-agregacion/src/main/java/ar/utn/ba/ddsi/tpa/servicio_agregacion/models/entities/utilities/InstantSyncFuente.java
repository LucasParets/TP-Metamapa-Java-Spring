package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.utilities;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.fuentes.TipoFuente;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class InstantSyncFuente {
    @Id
    private TipoFuente tipoFuente;
    @Column(nullable = false)
    private LocalDateTime ultimoSync = LocalDateTime.of(1970,1,1,0,0);
}
