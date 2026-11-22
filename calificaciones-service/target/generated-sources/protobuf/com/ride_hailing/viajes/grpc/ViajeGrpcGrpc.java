package com.ride_hailing.viajes.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@io.grpc.stub.annotations.GrpcGenerated
public final class ViajeGrpcGrpc {

  private ViajeGrpcGrpc() {}

  public static final java.lang.String SERVICE_NAME = "ViajeGrpc";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.ride_hailing.viajes.grpc.ViajeIdRequest,
      com.ride_hailing.viajes.grpc.ViajeDetalle> getConsultarViajeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ConsultarViaje",
      requestType = com.ride_hailing.viajes.grpc.ViajeIdRequest.class,
      responseType = com.ride_hailing.viajes.grpc.ViajeDetalle.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.ride_hailing.viajes.grpc.ViajeIdRequest,
      com.ride_hailing.viajes.grpc.ViajeDetalle> getConsultarViajeMethod() {
    io.grpc.MethodDescriptor<com.ride_hailing.viajes.grpc.ViajeIdRequest, com.ride_hailing.viajes.grpc.ViajeDetalle> getConsultarViajeMethod;
    if ((getConsultarViajeMethod = ViajeGrpcGrpc.getConsultarViajeMethod) == null) {
      synchronized (ViajeGrpcGrpc.class) {
        if ((getConsultarViajeMethod = ViajeGrpcGrpc.getConsultarViajeMethod) == null) {
          ViajeGrpcGrpc.getConsultarViajeMethod = getConsultarViajeMethod =
              io.grpc.MethodDescriptor.<com.ride_hailing.viajes.grpc.ViajeIdRequest, com.ride_hailing.viajes.grpc.ViajeDetalle>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ConsultarViaje"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.ride_hailing.viajes.grpc.ViajeIdRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.ride_hailing.viajes.grpc.ViajeDetalle.getDefaultInstance()))
              .setSchemaDescriptor(new ViajeGrpcMethodDescriptorSupplier("ConsultarViaje"))
              .build();
        }
      }
    }
    return getConsultarViajeMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ViajeGrpcStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ViajeGrpcStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ViajeGrpcStub>() {
        @java.lang.Override
        public ViajeGrpcStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ViajeGrpcStub(channel, callOptions);
        }
      };
    return ViajeGrpcStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static ViajeGrpcBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ViajeGrpcBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ViajeGrpcBlockingV2Stub>() {
        @java.lang.Override
        public ViajeGrpcBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ViajeGrpcBlockingV2Stub(channel, callOptions);
        }
      };
    return ViajeGrpcBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ViajeGrpcBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ViajeGrpcBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ViajeGrpcBlockingStub>() {
        @java.lang.Override
        public ViajeGrpcBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ViajeGrpcBlockingStub(channel, callOptions);
        }
      };
    return ViajeGrpcBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ViajeGrpcFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ViajeGrpcFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ViajeGrpcFutureStub>() {
        @java.lang.Override
        public ViajeGrpcFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ViajeGrpcFutureStub(channel, callOptions);
        }
      };
    return ViajeGrpcFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void consultarViaje(com.ride_hailing.viajes.grpc.ViajeIdRequest request,
        io.grpc.stub.StreamObserver<com.ride_hailing.viajes.grpc.ViajeDetalle> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getConsultarViajeMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service ViajeGrpc.
   */
  public static abstract class ViajeGrpcImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return ViajeGrpcGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service ViajeGrpc.
   */
  public static final class ViajeGrpcStub
      extends io.grpc.stub.AbstractAsyncStub<ViajeGrpcStub> {
    private ViajeGrpcStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ViajeGrpcStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ViajeGrpcStub(channel, callOptions);
    }

    /**
     */
    public void consultarViaje(com.ride_hailing.viajes.grpc.ViajeIdRequest request,
        io.grpc.stub.StreamObserver<com.ride_hailing.viajes.grpc.ViajeDetalle> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getConsultarViajeMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service ViajeGrpc.
   */
  public static final class ViajeGrpcBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<ViajeGrpcBlockingV2Stub> {
    private ViajeGrpcBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ViajeGrpcBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ViajeGrpcBlockingV2Stub(channel, callOptions);
    }

    /**
     */
    public com.ride_hailing.viajes.grpc.ViajeDetalle consultarViaje(com.ride_hailing.viajes.grpc.ViajeIdRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getConsultarViajeMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service ViajeGrpc.
   */
  public static final class ViajeGrpcBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<ViajeGrpcBlockingStub> {
    private ViajeGrpcBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ViajeGrpcBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ViajeGrpcBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.ride_hailing.viajes.grpc.ViajeDetalle consultarViaje(com.ride_hailing.viajes.grpc.ViajeIdRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getConsultarViajeMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service ViajeGrpc.
   */
  public static final class ViajeGrpcFutureStub
      extends io.grpc.stub.AbstractFutureStub<ViajeGrpcFutureStub> {
    private ViajeGrpcFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ViajeGrpcFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ViajeGrpcFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.ride_hailing.viajes.grpc.ViajeDetalle> consultarViaje(
        com.ride_hailing.viajes.grpc.ViajeIdRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getConsultarViajeMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CONSULTAR_VIAJE = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CONSULTAR_VIAJE:
          serviceImpl.consultarViaje((com.ride_hailing.viajes.grpc.ViajeIdRequest) request,
              (io.grpc.stub.StreamObserver<com.ride_hailing.viajes.grpc.ViajeDetalle>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getConsultarViajeMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.ride_hailing.viajes.grpc.ViajeIdRequest,
              com.ride_hailing.viajes.grpc.ViajeDetalle>(
                service, METHODID_CONSULTAR_VIAJE)))
        .build();
  }

  private static abstract class ViajeGrpcBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ViajeGrpcBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.ride_hailing.viajes.grpc.ViajeProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ViajeGrpc");
    }
  }

  private static final class ViajeGrpcFileDescriptorSupplier
      extends ViajeGrpcBaseDescriptorSupplier {
    ViajeGrpcFileDescriptorSupplier() {}
  }

  private static final class ViajeGrpcMethodDescriptorSupplier
      extends ViajeGrpcBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    ViajeGrpcMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (ViajeGrpcGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ViajeGrpcFileDescriptorSupplier())
              .addMethod(getConsultarViajeMethod())
              .build();
        }
      }
    }
    return result;
  }
}
