/* Licensed under Apache-2.0 */
package com.rico.hazelcast;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Bootstrap class for Hibernate with caching test microservice */
@SpringBootApplication
public class HazelcastCacheApplication {

  public static void main(String[] args) {
    SpringApplication.run(HazelcastCacheApplication.class, args);
  }
}
