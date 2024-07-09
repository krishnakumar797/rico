/* Licensed under Apache-2.0 */
package com.rico.grpc.client;

import com.ecwid.consul.v1.health.model.Check;
import com.rico.grpc.client.services.GrpcClientService;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.consul.discovery.ConsulServiceInstance;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ro.common.exception.CommonRestException;

/** Testing Rest controller service */
@RestController
@Validated
@Log4j2
public class TestRestController {

  @Autowired private DiscoveryClient discoveryClient;

  @Autowired private GrpcClientService service;

  /**
   * Get method to retrieve names only from the person table
   *
   * @param headers
   * @return
   * @throws CommonRestException
   */
  @GetMapping(value = "/hello", produces = MediaType.ALL_VALUE)
  public String getGreetings(@RequestHeader HttpHeaders headers) throws CommonRestException {
    return service.receiveGreeting("Rico");
  }

  @GetMapping(value = "/hai", produces = MediaType.ALL_VALUE)
  public String getHai(@RequestHeader HttpHeaders headers) throws CommonRestException {
    return "hai";
  }

  @RequestMapping("/service-instances/{applicationName}")
  public Object serviceInstancesByApplicationName(@PathVariable String applicationName) {
    List<ServiceInstance> instances = discoveryClient.getInstances(applicationName);
    for (ServiceInstance instance : instances) {
      if (isHealthy(instance, applicationName)) {
        return instance;
      }
    }
    return "No healthy instances found";
  }

  private boolean isHealthy(ServiceInstance instance, String applicationName) {
    boolean isHealthy = true;
    ConsulServiceInstance consulServiceInstance = (ConsulServiceInstance) instance;
    List<Check> healthStatus = consulServiceInstance.getHealthService().getChecks();
    for (Check check : healthStatus) {
      if (!check.getServiceName().equalsIgnoreCase(applicationName)) {
        continue;
      }
      if (!Check.CheckStatus.PASSING.equals(check.getStatus())) {
        isHealthy = false;
        break;
      }
    }
    return isHealthy;
  }
}
