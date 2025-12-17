package ar.utn.ba.ddsi.tpa.fuente_dinamica.models.dtos.input;

import lombok.Data;

@Data
public class MultimediaInputDTO {
    private String url;
    private String tipo;
    private String publicId;
}
