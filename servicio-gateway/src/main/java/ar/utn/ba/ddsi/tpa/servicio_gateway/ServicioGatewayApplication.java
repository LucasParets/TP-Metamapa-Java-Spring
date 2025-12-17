package ar.utn.ba.ddsi.tpa.servicio_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ServicioGatewayApplication {
	public static void main(String[] args) {
		SpringApplication.run(ServicioGatewayApplication.class, args);
	}
}
