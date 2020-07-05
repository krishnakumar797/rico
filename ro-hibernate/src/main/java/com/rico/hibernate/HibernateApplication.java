/* Licensed under Apache-2.0 */
package com.rico.hibernate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Bootstrap class for Hibernate test microservice */
@SpringBootApplication
public class HibernateApplication {

  public static void main(String[] args) {
    SpringApplication.run(HibernateApplication.class, args);
  }
}
