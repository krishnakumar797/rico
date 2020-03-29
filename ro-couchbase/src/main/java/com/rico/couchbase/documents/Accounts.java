/* Licensed under Apache-2.0 */
package com.rico.couchbase.documents;

import com.couchbase.client.java.repository.annotation.Field;
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
public class Accounts extends CBDoc {

  @Field private String bankName;

  @Field private String accountType;

  @Field private Integer accountBalance;
}
