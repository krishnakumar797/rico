/* Licensed under Apache-2.0 */
package com.rico.grpc.client.services;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import ro.common.generated.HelloWorldProto.HelloRequest;
import ro.common.generated.SimpleGrpc.SimpleBlockingStub;

/**
 * Sample service for testing GrpcClient
 *
 * @author r.krishnakumar
 */
@Service
public class GrpcClientService {

  // Grpc server with the identifier 'server1'. Target IP to the server1 has to configured
  // with the properties file
  @GrpcClient("server1")
  private SimpleBlockingStub simpleStub;

  /**
   * Method which sends the data to the GrpcServer with the name(ro-grpc-server)
   *
   * @param name
   * @return
   */
  public String receiveGreeting(String name) {
    HelloRequest request = HelloRequest.newBuilder().setName(name).build();
    return simpleStub.sayHello(request).getMessage();
  }
}
