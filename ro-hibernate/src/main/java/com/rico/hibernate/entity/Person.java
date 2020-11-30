/* Licensed under Apache-2.0 */
package com.rico.hibernate.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
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

  @OneToMany(
      mappedBy = "person",
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true)
  private List<Address> addressCollection = new ArrayList<>();

  public Person(String firstName, String lastName, Integer age, String emailAddress) {
    super();
    this.firstName = firstName;
    this.lastName = lastName;
    this.age = age;
    this.emailAddress = emailAddress;
  }
}
