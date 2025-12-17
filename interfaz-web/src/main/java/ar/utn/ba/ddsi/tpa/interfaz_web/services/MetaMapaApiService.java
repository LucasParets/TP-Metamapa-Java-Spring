package ar.utn.ba.ddsi.tpa.interfaz_web.services;

import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.*;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.inicio.DestacadosInicioDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.input.SolicitudInputDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Map;

@Service
public class MetaMapaApiService {
    private final String agregadorServiceUrl;
    private final WebClient webClient;
    private final WebApiCallerService api;
    private final String authServiceUrl;
    private static final Logger log = LoggerFactory.getLogger(MetaMapaApiService.class);

    public MetaMapaApiService(@Value("${api-gateway.url}") String apiGatewayUrl,
                              WebApiCallerService api) {
        this.agregadorServiceUrl = apiGatewayUrl + "/agregador";
        this.authServiceUrl = apiGatewayUrl + "/auth";
        this.webClient = WebClient.builder().build();
        this.api = api;
    }

    public AuthResponseDTO login(String username, String password) {
        try {
            AuthResponseDTO response = webClient
                    .post()
                    .uri(authServiceUrl + "/login")
                    .bodyValue(Map.of(
                            "username", username,
                            "password", password
                    ))
                    .retrieve()
                    .bodyToMono(AuthResponseDTO.class)
                    .block();
            return response;
        } catch (WebClientResponseException e) {
            log.error(e.getMessage());
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                // Login fallido - credenciales incorrectas
                return null;
            }
            // Otros errores HTTP
            throw new RuntimeException("Error en el servicio de autenticación: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error de conexión con el servicio de autenticación: " + e.getMessage(), e);
        }
    }

    public RolesDTO getRolesPermisos(String accessToken) {
        try {
            RolesDTO response = api.getWithAuth(
                    authServiceUrl + "/user/roles",
                    accessToken,
                    RolesDTO.class
            );
            return response;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Error al obtener roles y permisos: " + e.getMessage(), e);
        }
    }

    public AuthResponseDTO registrarUsuario(UsuarioDTO u) {
        try {
            return webClient.post()
                    .uri(authServiceUrl + "/register")
                    .bodyValue(Map.of(
                            "nombre", u.getNombre(),
                            "apellido", u.getApellido(),
                            "nombreDeUsuario", u.getUsername(),
                            "email", u.getEmail(),
                            "contrasenia", u.getPassword()
                    ))
                    .retrieve()
                    .bodyToMono(AuthResponseDTO.class)
                    .block();
        } catch (WebClientResponseException.Conflict e) {
            throw new IllegalArgumentException("Usuario o email ya existentes");
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Error al registrar: " + e.getStatusCode());
        }
    }

    public void crearSolicitud(SolicitudInputDTO dto) {
        api.post(agregadorServiceUrl + "/public/solicitudes", dto, void.class);
    }

    public List<CategoriaDTO> getCategorias(String handle) {
        String url = agregadorServiceUrl + "/public/categorias";
        if (handle != null) {
            url += "?handleColeccion=" + handle;
        }
        List<CategoriaDTO> response = api.getList(url, CategoriaDTO.class);
        return response != null ? response : List.of();
    }

    public HechoDTO getHecho(Long idHecho) {
        try {
            return api.get(agregadorServiceUrl + "/public/hechos/" + idHecho, HechoDTO.class);
        }
        catch (Exception e) {
            throw new RuntimeException("Error al obtener hecho: " + e.getMessage(), e);
        }
    }

    public ColeccionDTO getColeccion(String handle) {
        return api.get(agregadorServiceUrl + "/public/colecciones/" + handle, ColeccionDTO.class);
    }

    public PageResponse<HechoDTO> getHechosDeColeccion(String handle, FiltrosHechos f, Pageable pg) {
        WebClient api = WebClient.builder().baseUrl(agregadorServiceUrl).build();
        return api.get()
                .uri(ub -> {
                    var u = ub
                        .path("/public/colecciones/" + handle + "/hechos")
                        .queryParam("page", pg.getPageNumber())
                        .queryParam("size", pg.getPageSize())
                        .queryParam("q", f.getQ())
                        .queryParam("categoria", f.getCategoria())
                        .queryParam("modo_navegacion", f.getModo_navegacion())
                        .queryParam("fecha_hecho_desde", f.getFecha_hecho_desde())
                        .queryParam("fecha_hecho_hasta", f.getFecha_hecho_hasta());
                    pg.getSort().forEach(s -> {
                        u.queryParam("sort", s.getProperty() + "," + s.getDirection().name().toLowerCase());
                    });
                    return u.build();
                })
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PageResponse<HechoDTO>>() {})
                .block();
    }

    public PageResponse<ColeccionDTO> getColecciones(FiltrosColeccion f, Pageable pg) {
        WebClient api = WebClient.builder().baseUrl(agregadorServiceUrl + "/public/colecciones").build();
        return api.get()
                .uri(ub -> {
                    var u = ub
                            .queryParam("page", pg.getPageNumber())
                            .queryParam("size", pg.getPageSize())
                            .queryParam("q", f.getQ());
//                    pg.getSort().forEach(s -> {
//                        u.queryParam("sort", s.getProperty() + "," + s.getDirection().name().toLowerCase());
//                    });
                    return u.build();
                })
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PageResponse<ColeccionDTO>>() {})
                .block();
    }

    public DestacadosInicioDTO getDestacadosInicio() {
        return api.get(agregadorServiceUrl + "/public/inicio/destacados", DestacadosInicioDTO.class);
    }

    public PageResponse<HechoDTO> getHechosDestacados(FiltrosHechos f, Pageable pg) {
        WebClient api = WebClient.builder().baseUrl(agregadorServiceUrl).build();
        return api.get()
                .uri(ub -> {
                    var u = ub
                            .path("/public/hechos/destacados")
                            .queryParam("page", pg.getPageNumber())
                            .queryParam("size", pg.getPageSize())
                            .queryParam("q", f.getQ())
                            .queryParam("categoria", f.getCategoria())
                            .queryParam("fecha_hecho_desde", f.getFecha_hecho_desde())
                            .queryParam("fecha_hecho_hasta", f.getFecha_hecho_hasta());
                    pg.getSort().forEach(s -> {
                        u.queryParam("sort", s.getProperty() + "," + s.getDirection().name().toLowerCase());
                    });
                    return u.build();
                })
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PageResponse<HechoDTO>>() {})
                .block();
    }

    public PageResponse<HechoDTO> getHechos(FiltrosHechos f, Pageable pg) {
        WebClient wc = WebClient.builder().baseUrl(agregadorServiceUrl).build();
        return wc.get()
                .uri(ub -> {
                    var u = ub
                            .path("/public/hechos")
                            .queryParam("page", pg.getPageNumber())
                            .queryParam("size", pg.getPageSize())
                            .queryParam("q", f.getQ())
                            .queryParam("categoria", f.getCategoria())
                            .queryParam("fecha_hecho_desde", f.getFecha_hecho_desde())
                            .queryParam("fecha_hecho_hasta", f.getFecha_hecho_hasta());
                    pg.getSort().forEach(s -> {
                        u.queryParam("sort", s.getProperty() + "," + s.getDirection().name().toLowerCase());
                    });
                    return u.build();
                })
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PageResponse<HechoDTO>>() {})
                .block();
    }
}
