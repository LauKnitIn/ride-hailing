package com.ride_hailing.calificacion.infraestructure.salida;

import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.ImportGrpcClients;

import com.ride_hailing.viajes.grpc.ViajeGrpcGrpc.ViajeGrpcBlockingStub;

@Configuration(proxyBeanMethods = false)
@ImportGrpcClients(target = "viajes", types = ViajeGrpcBlockingStub.class)
public class ViajesGrpcConfig {
}
