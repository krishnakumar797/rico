/* Licensed under Apache-2.0 */
package com.healtrip.client.documents;

import org.springframework.data.elasticsearch.annotations.Document;
import ro.common.elasticsearch.ESDoc;

@Document(createIndex = true, indexName = "person", shards = 2, replicas = 1)
public class Person extends ESDoc {

  private String firstname;
  private String lastname;

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }
}
