package com.ride_hailing.viajes.infraestructura.salida.conductores;

import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.ImportGrpcClients;

import com.ride_hailing.viajes.grpc.ConductorGrpcGrpc.ConductorGrpcBlockingStub;

/** Registra el stub gRPC hacia conductores-service sobre el canal "conductores" del application.yml. */
@Configuration(proxyBeanMethods = false)
@ImportGrpcClients(target = "conductores", types = ConductorGrpcBlockingStub.class)
public class ConductoresGrpcConfig {
}
