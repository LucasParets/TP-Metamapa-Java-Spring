package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.fuentes;

import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.origen.OrigenDelHecho;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.origen.OrigenEstatica;
import ar.utn.ba.ddsi.tpa.servicio_agregacion.models.entities.origen.OrigenProxy;

public enum TipoFuente {
    PROXY {
        @Override
        public String toString() {
            return "Proxy";
        }
    },
    ESTATICA {
        @Override
        public String toString() {
            return "Estatica";
        }
    },
    DINAMICA {
        @Override
        public String toString() {
            return "Dinamica";
        }
    };

}
