package ar.utn.ba.ddsi.tpa.interfaz_web.services.admin;

import ar.utn.ba.ddsi.tpa.interfaz_web.services.WebApiCallerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EtiquetasService {
    private final String agregadorServiceUrl;
    private final WebApiCallerService api;

    public EtiquetasService(@Value("${api-gateway.url}") String apiGatewayUrl,
                            WebApiCallerService api) {
        this.agregadorServiceUrl = apiGatewayUrl + "/agregador/admin";
        this.api = api;
    }

    public void eliminarEtiqueta(Long idHecho, String etiqueta) {
        api.delete(agregadorServiceUrl + "/hechos/" + idHecho + "/etiquetas/" + etiqueta);
    }

    public void agregarEtiqueta(Long idHecho, String etiqueta) {
        api.post(agregadorServiceUrl + "/hechos/" + idHecho + "/etiquetas", etiqueta, void.class);
    }

    public List<String> getEtiquetasDeHecho(Long idHecho) {
        return api.getList(agregadorServiceUrl + "/hechos/" + idHecho + "/etiquetas", String.class);
    }

}
