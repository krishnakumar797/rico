/* Licensed under Apache-2.0 */
package com.rico.couchbase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Bootstrap class for Couchbase test microservice
 *
 * @author r.krishnakumar
 */
@SpringBootApplication
public class CBTestApplication {

  public static void main(String[] args) {
    SpringApplication.run(CBTestApplication.class, args);
  }
}
