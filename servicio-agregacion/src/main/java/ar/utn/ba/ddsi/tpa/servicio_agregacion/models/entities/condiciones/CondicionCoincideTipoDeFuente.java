package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.condiciones;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.fuentes.TipoFuente;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.origen.OrigenDelHecho;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.origen.OrigenDinamica;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.origen.OrigenEstatica;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.origen.OrigenProxy;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;

@Getter
@Setter
public class CondicionCoincideTipoDeFuente extends Condicion {
    private TipoFuente tipoFuente;
    private static final Map<TipoFuente, Class<? extends OrigenDelHecho>> tipoOrigen = Map.of(
            TipoFuente.ESTATICA, OrigenEstatica.class,
            TipoFuente.DINAMICA, OrigenDinamica.class,
            TipoFuente.PROXY, OrigenProxy.class
    );

    public CondicionCoincideTipoDeFuente(TipoFuente tipoFuente) {
        super();
        this.tipoFuente = tipoFuente;
    }

    @Override
    public Specification<Hecho> cumple() {
        return (root, cq, cb) -> cb.and(
                cb.equal(root.get("origen").type(), tipoOrigen.get(this.tipoFuente))
        );
    }
}
