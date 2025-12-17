package ar.utn.ba.ddsi.tpa.interfaz_web.services;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.AuthResponseDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.HechoDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.PageResponse;
import ar.utn.ba.ddsi.tpa.interfaz_web.dtos.RefreshTokenDTO;
import ar.utn.ba.ddsi.tpa.interfaz_web.exceptions.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Objects;

/**
 * Servicio genérico para hacer llamadas HTTP con manejo automático de tokens
 */
@Service
public class WebApiCallerService {
    private final String authServiceUrl;
    private final WebClient webClient;

    public WebApiCallerService(@Value("${api-gateway.url}") String apiGatewayUrl) {
        this.webClient = WebClient.builder().build();
        this.authServiceUrl = apiGatewayUrl + "/auth";
    }

    /**
     * Ejecuta una llamada al API con manejo automático de refresh token
     * @param apiCall función que ejecuta la llamada al API
     * @return resultado de la llamada al API
     */
    public <T> T executeWithTokenRetry(ApiCall<T> apiCall) {
        String accessToken = getAccessTokenFromSession();
        String refreshToken = getRefreshTokenFromSession();

        try {
            return apiCall.execute(Objects.requireNonNullElse(accessToken, ""));
        }
        catch (WebClientResponseException e) {
            if ((e.getStatusCode() == HttpStatus.UNAUTHORIZED || e.getStatusCode() == HttpStatus.FORBIDDEN) && refreshToken != null) {
                try {
                    // Token expirado, intentar refresh
                    AuthResponseDTO newTokens = refreshToken(refreshToken);

                    // Segundo intento con el nuevo token
                    return apiCall.execute(newTokens.getAccessToken());
                } catch (Exception refreshError) {
                    throw new RuntimeException("Error al refrescar token y reintentar: " + refreshError.getMessage(), refreshError);
                }
            }
            if(e.getStatusCode() == HttpStatus.NOT_FOUND){
                throw new NotFoundException("", "");
            }
            throw new RuntimeException("Error en llamada al API: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error de conexión con el servicio: " + e.getMessage(), e);
        }
    }

    /**
     * Ejecuta una llamada HTTP GET con un token específico (sin usar sesión)
     */
    public <T> T getWithAuth(String url, String accessToken, Class<T> responseType) {
        try {
            return webClient
                    .get()
                    .uri(url)
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(responseType)
                    .block();
        } catch (Exception e) {
            throw new RuntimeException("Error en llamada al API: " + e.getMessage(), e);
        }
    }

    /**
     * Refresca el access token usando el refresh token
     */
    private AuthResponseDTO refreshToken(String refreshToken) {
        try {
            RefreshTokenDTO refreshRequest = RefreshTokenDTO.builder()
                    .refreshToken(refreshToken)
                    .build();

            AuthResponseDTO response = webClient
                    .post()
                    .uri(authServiceUrl + "/refresh")
                    .bodyValue(refreshRequest)
                    .retrieve()
                    .bodyToMono(AuthResponseDTO.class)
                    .block();

            // Actualizar tokens en sesión
            updateTokensInSession(response.getAccessToken(), response.getRefreshToken());
            return response;
        } catch (Exception e) {
            throw new RuntimeException("Error al refrescar token: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene el access token de la sesión
     */
    private String getAccessTokenFromSession() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        return (String) request.getSession().getAttribute("accessToken");
    }

    /**
     * Obtiene el refresh token de la sesión
     */
    private String getRefreshTokenFromSession() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        return (String) request.getSession().getAttribute("refreshToken");
    }

    /**
     * Actualiza los tokens en la sesión
     */
    private void updateTokensInSession(String accessToken, String refreshToken) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        request.getSession().setAttribute("accessToken", accessToken);
        request.getSession().setAttribute("refreshToken", refreshToken);
    }

    /**
     * Interfaz funcional para ejecutar llamadas al API con token
     */
    @FunctionalInterface
    public interface ApiCall<T> {
        T execute(String accessToken) throws Exception;
    }

    public <T> T get(String url, Class<T> responseType) {
        if (getAccessTokenFromSession() == null) {
            return webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(responseType)
                    .block();
        }
        return executeWithTokenRetry(accessToken -> webClient.get()
            .uri(url)
            .header("Authorization", "Bearer " + accessToken)
            .retrieve()
            .bodyToMono(responseType)
            .block());
    }

    public <T> java.util.List<T> getList(String url, Class<T> responseType) {
        if (getAccessTokenFromSession() == null) {
            return webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToFlux(responseType)
                    .collectList()
                    .block();
        }
        return executeWithTokenRetry(accessToken -> webClient.get()
            .uri(url)
            .header("Authorization", "Bearer " + accessToken)
            .retrieve()
            .bodyToFlux(responseType)
            .collectList()
            .block());
    }


    public <T> T post(String url, Object body, Class<T> responseType) {
        if (getAccessTokenFromSession() == null) {
            return webClient.post()
                    .uri(url)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(responseType)
                    .block();
        }
        return executeWithTokenRetry(accessToken -> webClient.post()
            .uri(url)
            .header("Authorization", "Bearer " + accessToken)
            .bodyValue(body)
            .retrieve()
            .bodyToMono(responseType)
            .block());
    }

    public <T> T put(String url, Object body, Class<T> responseType) {
        if (getAccessTokenFromSession() == null) {
            webClient.put()
                    .uri(url)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(responseType)
                    .block();
        }
        return executeWithTokenRetry(accessToken -> webClient.put()
            .uri(url)
            .header("Authorization", "Bearer " + accessToken)
            .bodyValue(body)
            .retrieve()
            .bodyToMono(responseType)
            .block());
    }

    public void delete(String url) {
        if (getAccessTokenFromSession() == null) {
            webClient.delete()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        }
        else {
            executeWithTokenRetry(accessToken -> webClient.delete()
                    .uri(url)
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block());
        }

    }

    public <T> T patch(String url, Object body, Class<T> responseType) {
        if (getAccessTokenFromSession() == null) {
            webClient.patch()
                    .uri(url)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(responseType)
                    .block();
        }
        return executeWithTokenRetry(accessToken -> webClient.patch()
                .uri(url)
                .header("Authorization", "Bearer " + accessToken)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(responseType)
                .block());
    }
}