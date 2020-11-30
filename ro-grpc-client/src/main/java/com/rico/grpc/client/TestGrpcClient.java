/* Licensed under Apache-2.0 */
package com.rico.grpc.client;

import com.rico.grpc.client.services.GrpcClientService;
import javax.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Testing Grpc client service
 *
 * @author r.krishnakumar
 */
@Component
@Log4j2
public class TestGrpcClient {

  @Autowired private GrpcClientService clientService;

  @PostConstruct
  public void sendGreeting() {
    String greeting = clientService.receiveGreeting("Rico..!!");
    log.info("GREETINGS " + greeting);
  }
}
