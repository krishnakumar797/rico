/* Licensed under Apache-2.0 */
package com.healtrip.client.services;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import ro.common.generated.HelloWorldProto.HelloRequest;
import ro.common.generated.SimpleGrpc.SimpleBlockingStub;

@Service
public class GrpcClientService {

  @GrpcClient("ht-test")
  private SimpleBlockingStub simpleStub;

  public String receiveGreeting(String name) {
    HelloRequest request = HelloRequest.newBuilder().setName(name).build();
    return simpleStub.sayHello(request).getMessage();
  }
}
