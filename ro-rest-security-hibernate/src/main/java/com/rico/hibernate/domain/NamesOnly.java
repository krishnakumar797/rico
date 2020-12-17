/* Licensed under Apache-2.0 */
package com.rico.hibernate.domain;

import lombok.NoArgsConstructor;

/**
 * Sample domain model
 *
 * @author r.krishnakumar
 */
@NoArgsConstructor
public class NamesOnly {

  private String firstName;

  private String lastName;

  public String getFullName() {
    return this.firstName.concat(" ").concat(this.lastName);
  }
}
