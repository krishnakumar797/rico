/* Licensed under Apache-2.0 */
package com.rico.kafka.documents;

import ro.common.utils.Doc;

/**
 * Sample document to share using the queue service. Place these files in common place to avoid the
 * deserialization errors.
 *
 * @author r.krishnakumar
 */
public class User implements Doc {

  private String firstname;

  private String lastname;

  public User(String firstname, String lastname) {
    this.firstname = firstname;
    this.lastname = lastname;
  }

  public String getFirstname() {
    return firstname;
  }

  public String getLastname() {
    return lastname;
  }
}
