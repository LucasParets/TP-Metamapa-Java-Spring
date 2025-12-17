package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.input;

import lombok.Data;

@Data
public class SolicitudEliminacionInputDTO {
    private Long idHechoAEliminar;
    private String descripcion;
    private String nombreUsuario;
}
