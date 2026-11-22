package com.ride_hailing.tarifas.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@io.grpc.stub.annotations.GrpcGenerated
public final class TarifaGrpcGrpc {

  private TarifaGrpcGrpc() {}

  public static final java.lang.String SERVICE_NAME = "TarifaGrpc";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.ride_hailing.tarifas.grpc.CotizarTarifaRequest,
      com.ride_hailing.tarifas.grpc.CotizarTarifaResponse> getCotizarTarifaMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CotizarTarifa",
      requestType = com.ride_hailing.tarifas.grpc.CotizarTarifaRequest.class,
      responseType = com.ride_hailing.tarifas.grpc.CotizarTarifaResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.ride_hailing.tarifas.grpc.CotizarTarifaRequest,
      com.ride_hailing.tarifas.grpc.CotizarTarifaResponse> getCotizarTarifaMethod() {
    io.grpc.MethodDescriptor<com.ride_hailing.tarifas.grpc.CotizarTarifaRequest, com.ride_hailing.tarifas.grpc.CotizarTarifaResponse> getCotizarTarifaMethod;
    if ((getCotizarTarifaMethod = TarifaGrpcGrpc.getCotizarTarifaMethod) == null) {
      synchronized (TarifaGrpcGrpc.class) {
        if ((getCotizarTarifaMethod = TarifaGrpcGrpc.getCotizarTarifaMethod) == null) {
          TarifaGrpcGrpc.getCotizarTarifaMethod = getCotizarTarifaMethod =
              io.grpc.MethodDescriptor.<com.ride_hailing.tarifas.grpc.CotizarTarifaRequest, com.ride_hailing.tarifas.grpc.CotizarTarifaResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CotizarTarifa"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.ride_hailing.tarifas.grpc.CotizarTarifaRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.ride_hailing.tarifas.grpc.CotizarTarifaResponse.getDefaultInstance()))
              .setSchemaDescriptor(new TarifaGrpcMethodDescriptorSupplier("CotizarTarifa"))
              .build();
        }
      }
    }
    return getCotizarTarifaMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static TarifaGrpcStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TarifaGrpcStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TarifaGrpcStub>() {
        @java.lang.Override
        public TarifaGrpcStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TarifaGrpcStub(channel, callOptions);
        }
      };
    return TarifaGrpcStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static TarifaGrpcBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TarifaGrpcBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TarifaGrpcBlockingV2Stub>() {
        @java.lang.Override
        public TarifaGrpcBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TarifaGrpcBlockingV2Stub(channel, callOptions);
        }
      };
    return TarifaGrpcBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static TarifaGrpcBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TarifaGrpcBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TarifaGrpcBlockingStub>() {
        @java.lang.Override
        public TarifaGrpcBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TarifaGrpcBlockingStub(channel, callOptions);
        }
      };
    return TarifaGrpcBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static TarifaGrpcFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TarifaGrpcFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TarifaGrpcFutureStub>() {
        @java.lang.Override
        public TarifaGrpcFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TarifaGrpcFutureStub(channel, callOptions);
        }
      };
    return TarifaGrpcFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void cotizarTarifa(com.ride_hailing.tarifas.grpc.CotizarTarifaRequest request,
        io.grpc.stub.StreamObserver<com.ride_hailing.tarifas.grpc.CotizarTarifaResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCotizarTarifaMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service TarifaGrpc.
   */
  public static abstract class TarifaGrpcImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return TarifaGrpcGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service TarifaGrpc.
   */
  public static final class TarifaGrpcStub
      extends io.grpc.stub.AbstractAsyncStub<TarifaGrpcStub> {
    private TarifaGrpcStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TarifaGrpcStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TarifaGrpcStub(channel, callOptions);
    }

    /**
     */
    public void cotizarTarifa(com.ride_hailing.tarifas.grpc.CotizarTarifaRequest request,
        io.grpc.stub.StreamObserver<com.ride_hailing.tarifas.grpc.CotizarTarifaResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCotizarTarifaMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service TarifaGrpc.
   */
  public static final class TarifaGrpcBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<TarifaGrpcBlockingV2Stub> {
    private TarifaGrpcBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TarifaGrpcBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TarifaGrpcBlockingV2Stub(channel, callOptions);
    }

    /**
     */
    public com.ride_hailing.tarifas.grpc.CotizarTarifaResponse cotizarTarifa(com.ride_hailing.tarifas.grpc.CotizarTarifaRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getCotizarTarifaMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service TarifaGrpc.
   */
  public static final class TarifaGrpcBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<TarifaGrpcBlockingStub> {
    private TarifaGrpcBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TarifaGrpcBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TarifaGrpcBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.ride_hailing.tarifas.grpc.CotizarTarifaResponse cotizarTarifa(com.ride_hailing.tarifas.grpc.CotizarTarifaRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCotizarTarifaMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service TarifaGrpc.
   */
  public static final class TarifaGrpcFutureStub
      extends io.grpc.stub.AbstractFutureStub<TarifaGrpcFutureStub> {
    private TarifaGrpcFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TarifaGrpcFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TarifaGrpcFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.ride_hailing.tarifas.grpc.CotizarTarifaResponse> cotizarTarifa(
        com.ride_hailing.tarifas.grpc.CotizarTarifaRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCotizarTarifaMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_COTIZAR_TARIFA = 0;

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
        case METHODID_COTIZAR_TARIFA:
          serviceImpl.cotizarTarifa((com.ride_hailing.tarifas.grpc.CotizarTarifaRequest) request,
              (io.grpc.stub.StreamObserver<com.ride_hailing.tarifas.grpc.CotizarTarifaResponse>) responseObserver);
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
          getCotizarTarifaMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.ride_hailing.tarifas.grpc.CotizarTarifaRequest,
              com.ride_hailing.tarifas.grpc.CotizarTarifaResponse>(
                service, METHODID_COTIZAR_TARIFA)))
        .build();
  }

  private static abstract class TarifaGrpcBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    TarifaGrpcBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.ride_hailing.tarifas.grpc.TarifaProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("TarifaGrpc");
    }
  }

  private static final class TarifaGrpcFileDescriptorSupplier
      extends TarifaGrpcBaseDescriptorSupplier {
    TarifaGrpcFileDescriptorSupplier() {}
  }

  private static final class TarifaGrpcMethodDescriptorSupplier
      extends TarifaGrpcBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    TarifaGrpcMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (TarifaGrpcGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new TarifaGrpcFileDescriptorSupplier())
              .addMethod(getCotizarTarifaMethod())
              .build();
        }
      }
    }
    return result;
  }
}
