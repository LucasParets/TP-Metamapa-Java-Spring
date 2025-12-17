package ar.utn.ba.ddsi.tpa.servicio_discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ServicioDiscoveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicioDiscoveryApplication.class, args);
	}

}
