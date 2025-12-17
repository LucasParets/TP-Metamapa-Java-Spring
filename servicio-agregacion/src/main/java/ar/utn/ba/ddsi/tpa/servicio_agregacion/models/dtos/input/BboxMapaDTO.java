package ar.utn.ba.ddsi.tpa.servicio_agregacion.models.dtos.input;

import lombok.Data;

@Data
public class BboxMapaDTO {
    double s;
    double w;
    double n;
    double e;
}
