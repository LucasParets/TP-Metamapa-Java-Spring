package ar.utn.ba.ddsi.tpa.interfaz_web.dtos.inicio;

import lombok.Data;

import java.util.List;

@Data
public class DestacadosInicioDTO {
    List<HechoReducidoDTO> hechosDestacados;
    List<ColeccionReducidaDTO> coleccionesDestacadas;
}
