package ar.utn.ba.ddsi.tpa.fuente_estatica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FuenteEstaticaApplication {

	public static void main(String[] args) {
		SpringApplication.run(FuenteEstaticaApplication.class, args);
	}

}
