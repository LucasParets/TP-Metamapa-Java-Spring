package ar.utn.ba.ddsi.tpa.interfaz_web.services.admin;

import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.dashboard.DashboardDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.services.WebApiCallerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {
    private final String agregadorServiceUrl;
    private final WebApiCallerService api;

    public DashboardService(@Value("${api-gateway.url}") String apiGatewayUrl,
                                    WebApiCallerService api) {
        this.agregadorServiceUrl = apiGatewayUrl + "/agregador";
        this.api = api;
    }

    public DashboardDTO getDashboard() {
        return api.get(agregadorServiceUrl + "/admin/dashboard", DashboardDTO.class);
    }
}
