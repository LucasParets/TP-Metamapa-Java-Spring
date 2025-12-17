package ar.utn.ba.ddsi.tpa.interfaz_web.dtos.input;

import lombok.*;

@Data
public class SolicitudInputDTO {
    private Long idHechoAEliminar;
    private String nombreUsuario;
    private String descripcion;
}
