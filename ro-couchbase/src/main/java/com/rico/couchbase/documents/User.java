/* Licensed under Apache-2.0 */
package com.rico.couchbase.documents;

import com.couchbase.client.java.repository.annotation.Field;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.couchbase.core.mapping.Document;
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
public class User extends CBDoc {

  @Field private String firstName;

  @Field private String lastName;

  @Field private Integer age;

  @Field private Address address;

  @Field private List<Accounts> accounts;

  @Field private Business business;
}
