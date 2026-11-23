package com.ride_hailing.eureka_server;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

class EurekaServerConfiguracionTest {

	@Test
	void laAplicacionHabilitaElServidorEureka() {
		assertNotNull(EurekaServerApplication.class.getAnnotation(EnableEurekaServer.class));
	}

	@Test
	void laAplicacionEsUnaAplicacionSpringBoot() {
		assertTrue(EurekaServerApplication.class.isAnnotationPresent(SpringBootApplication.class));
	}
}
