package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.output.destacados;

import lombok.Data;

import java.util.List;

@Data
public class DestacadosInicioDTO {
    List<HechoReducidoDTO> hechosDestacados;
    List<ColeccionReducidaDTO> coleccionesDestacadas;
}
