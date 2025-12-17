package ar.utn.ba.ddsi.tpa.servicio_agregacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableDiscoveryClient
public class ServicioAgregacionApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicioAgregacionApplication.class, args);
	}

}
