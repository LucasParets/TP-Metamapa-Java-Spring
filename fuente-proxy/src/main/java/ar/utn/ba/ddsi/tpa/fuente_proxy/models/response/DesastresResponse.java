package ar.utn.ba.ddsi.tpa.fuente_proxy.models.response;

import ar.utn.ba.ddsi.tpa.fuente_proxy.models.entities.hecho.Hecho;
import lombok.Data;
import java.util.List;

@Data
public class DesastresResponse {
    private Integer current_page;
    private String first_page_url;
    private Integer from;
    private Integer last_page;
    private String last_page_url;
    private String next_page_url;
    private String path;
    private Integer per_page;
    private String prev_page_url;
    private Integer to;
    private Integer total;
    private List<Desastre> data;
}
