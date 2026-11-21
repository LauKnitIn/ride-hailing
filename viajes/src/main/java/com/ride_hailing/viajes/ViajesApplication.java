package com.ride_hailing.viajes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ViajesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ViajesApplication.class, args);
	}

}
