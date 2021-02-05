/* Licensed under Apache-2.0 */
package com.rico.rest.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ro.common.utils.EntityDoc;

/**
 * Sample Entity document
 *
 * @author r.krishnakumar
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Person extends EntityDoc {

  @Column(name = "first_name", length = 50, nullable = false)
  private String firstName;

  @Column(name = "last_name", length = 50, nullable = false)
  private String lastName;

  @Column(name = "age", nullable = false)
  private Integer age;

  @Column(name = "email", length = 100, nullable = false, unique = true)
  private String emailAddress;
}
