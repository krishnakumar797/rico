/* Licensed under Apache-2.0 */
package com.rico.rest.domain;

import lombok.Value;

/**
 * Sample domain model
 *
 * @author r.krishnakumar
 */
@Value
public class NamesOnly {

  private String firstName;

  private String lastName;

  public String getFullName() {
    return this.firstName.concat(" ").concat(this.lastName);
  }
}
