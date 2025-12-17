package ar.utn.ba.ddsi.tpa.interfaz_web.services.admin;

import ar.utn.ba.ddsi.tpa.interfaz_web.services.WebApiCallerService;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
public class ImportarDatasetService {
    private final WebClient webClient;
    private final WebApiCallerService api;
    private final Logger log = LoggerFactory.getLogger(ImportarDatasetService.class);

    public ImportarDatasetService(@Value("${api-gateway.url}") String apiGatewayUrl,
                                  WebApiCallerService api) {
        this.api = api;
        this.webClient = WebClient.builder()
                .baseUrl(apiGatewayUrl + "/estatica")
                .build();
    }

    public void importarDataset(MultipartFile dataset) {
        try {
            log.info("Iniciando envío de dataset: {} - Tamaño: {} bytes",
                    dataset.getOriginalFilename(),
                    dataset.getSize());

            MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
            bodyBuilder.part("dataset", new ByteArrayResource(dataset.getBytes()) {
                @Override
                public String getFilename() {
                    return dataset.getOriginalFilename();
                }
            });

            api.executeWithTokenRetry(accessToken -> webClient.post()
                    .uri("/importar")
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                    .retrieve()
                    .toBodilessEntity()
                    .doOnSuccess(response -> log.info("Dataset enviado exitosamente"))
                    .doOnError(error -> log.error("Error enviando dataset", error))
                    .block());

            log.info("Respuesta recibida del backend");

        } catch (IOException e) {
            throw new RuntimeException("Error al procesar el archivo: " + e.getMessage());
        }
    }


}
