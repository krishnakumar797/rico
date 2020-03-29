/* Licensed under Apache-2.0 */
package com.rico.grpc.client;

import com.rico.grpc.client.services.GrpcClientService;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Testing Grpc client service
 *
 * @author r.krishnakumar
 */
@Component
public class TestGrpcClient {

  @Autowired private GrpcClientService clientService;

  @PostConstruct
  public void sendGreeting() {
    String greeting = clientService.receiveGreeting("Rico..!!");
    System.out.println("GREETINGS " + greeting);
  }
}
