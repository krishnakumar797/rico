package com.rico.grpc.server.service;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import ro.common.generated.HelloWorldProto.HelloReply;
import ro.common.generated.HelloWorldProto.HelloRequest;
import ro.common.generated.SimpleGrpc;

/**
 * Test Grpc Service
 *
 */
@GrpcService
public class GrpcServerService extends SimpleGrpc.SimpleImplBase {

	@Override
	public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
		HelloReply reply = HelloReply.newBuilder().setMessage("Hello ==> " + request.getName()).build();
		responseObserver.onNext(reply);
		responseObserver.onCompleted();
	}

}