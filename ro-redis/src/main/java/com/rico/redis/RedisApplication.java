/* Licensed under Apache-2.0 */
package com.rico.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Bootstrap class for Redis test microservice */
@SpringBootApplication
public class RedisApplication {

  public static void main(String[] args) {
    SpringApplication.run(RedisApplication.class, args);
  }
}
