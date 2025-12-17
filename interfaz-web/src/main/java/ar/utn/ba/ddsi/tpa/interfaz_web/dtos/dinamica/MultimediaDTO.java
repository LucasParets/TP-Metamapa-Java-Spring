package ar.utn.ba.ddsi.tpa.interfaz_web.dtos.dinamica;

import lombok.Data;

@Data
public class MultimediaDTO {
    private Long id;
    private String url;
    private String tipo;
    private String publicId;
}
