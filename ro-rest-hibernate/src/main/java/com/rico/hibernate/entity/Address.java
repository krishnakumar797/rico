/* Licensed under Apache-2.0 */
package com.rico.hibernate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
public class Address extends EntityDoc {

  @Column(name = "house_name", length = 50, nullable = false)
  private String houseName;

  @Column(name = "street_name", length = 50, nullable = false)
  private String streetName;

  @Column(name = "city_name", length = 50, nullable = false)
  private String city;

  @Column(name = "state_name", length = 50, nullable = false)
  private String stateName;

  @Column(name = "country", length = 50, nullable = false)
  private String country;

  @Column(name = "resident_status", length = 50, nullable = false)
  private String residentStatus;

  @Column(name = "pin_code", length = 50, nullable = false)
  private String pinCode;

  @ManyToOne
  @JoinColumn(name = "person_id", nullable = false)
  private Person person;
}
