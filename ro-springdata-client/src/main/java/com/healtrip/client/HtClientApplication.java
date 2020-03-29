/* Licensed under Apache-2.0 */
package com.healtrip.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Bootstrap class for ht client microservice */
@SpringBootApplication
public class HtClientApplication {

  public static void main(String[] args) {
    SpringApplication.run(HtClientApplication.class, args);
  }
}
