package ar.utn.ba.ddsi.tpa.fuente_proxy.models.dtos.metamapa;

import lombok.Data;

@Data
public class SolicitudDTO {
    private Long idHechoAEliminar;
    private String descripcion;
    private String nombreUsuario;
}
