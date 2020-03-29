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
public class Accounts extends CBDoc {

  @Field private String bankName;

  @Field private String accountType;

  @Field private Integer accountBalance;
}
