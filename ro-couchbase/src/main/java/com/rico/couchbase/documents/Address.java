/* Licensed under Apache-2.0 */
package com.rico.couchbase.documents;

import org.springframework.data.couchbase.core.mapping.Document;

import com.couchbase.client.java.repository.annotation.Field;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ro.common.couchbase.CBDoc;

/**
 * Sample Couchbase document
 *
 * @author r.krishnakumar
 */
@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address extends CBDoc {

  @Field private String houseName;

  @Field private String streetName;

  @Field private String city;

  @Field private String state;

  @Field private String country;

  @Field private String residentStatus;

  @Field private String pinCode;
}
