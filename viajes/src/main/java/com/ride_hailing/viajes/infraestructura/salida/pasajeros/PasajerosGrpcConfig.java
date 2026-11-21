package com.ride_hailing.viajes.infraestructura.salida.pasajeros;

import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.ImportGrpcClients;

import com.ride_hailing.viajes.grpc.PasajeroGrpcGrpc.PasajeroGrpcBlockingStub;

@Configuration(proxyBeanMethods = false)
@ImportGrpcClients(target = "pasajeros", types = PasajeroGrpcBlockingStub.class)
public class PasajerosGrpcConfig {
}
