package com.ride_hailing.gateway;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

/** Verifica que el gateway enruta cada prefijo /api a su servicio registrado en Eureka. */
class GatewayRutasTest {

	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> rutas() throws Exception {
		try (InputStream in = getClass().getResourceAsStream("/application.yml")) {
			Map<String, Object> raiz = new Yaml().load(in);
			Map<String, Object> spring = (Map<String, Object>) raiz.get("spring");
			Map<String, Object> cloud = (Map<String, Object>) spring.get("cloud");
			Map<String, Object> gateway = (Map<String, Object>) cloud.get("gateway");
			Map<String, Object> server = (Map<String, Object>) gateway.get("server");
			Map<String, Object> webflux = (Map<String, Object>) server.get("webflux");
			return (List<Map<String, Object>>) webflux.get("routes");
		}
	}

	@SuppressWarnings("unchecked")
	private boolean existe(String uri, String path) throws Exception {
		return rutas().stream().anyMatch(r -> uri.equals(r.get("uri"))
				&& ((List<String>) r.get("predicates")).contains("Path=" + path));
	}

	@Test
	void declaraSeisRutas() throws Exception {
		assertEquals(6, rutas().size());
	}

	@Test
	void enrutaPasajerosViajesYConductores() throws Exception {
		assertTrue(existe("lb://pasajeros-service", "/api/pasajeros/**"));
		assertTrue(existe("lb://viajes-service", "/api/viajes/**"));
		assertTrue(existe("lb://conductores-service", "/api/conductores/**"));
	}

	@Test
	void enrutaOfertasTarifasYCalificaciones() throws Exception {
		assertTrue(existe("lb://emparejamiento-service", "/api/ofertas/**"));
		assertTrue(existe("lb://tarifas-service", "/api/tarifas/**"));
		assertTrue(existe("lb://calificaciones-service", "/api/calificaciones/**"));
	}
}
