package ar.utn.ba.ddsi.tpa.fuente_proxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FuenteProxyApplication {
    public static void main(String[] args) {
        SpringApplication.run(FuenteProxyApplication.class, args);
    }
}