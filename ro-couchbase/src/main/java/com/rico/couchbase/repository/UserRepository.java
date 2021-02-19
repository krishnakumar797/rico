/* Licensed under Apache-2.0 */
package com.rico.couchbase.repository;

import com.rico.couchbase.documents.User;
import java.util.List;
import org.springframework.data.couchbase.core.query.N1qlSecondaryIndexed;
import org.springframework.data.couchbase.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ro.common.couchbase.CommonCBRepository;

/**
 * Sample repository for Couchbase user collection
 *
 * @author r.krishnakumar
 */
@Repository
@N1qlSecondaryIndexed(indexName = "user")
public interface UserRepository extends CommonCBRepository<User> {

  List<User> findByFirstName(String firstName);

  Page<User> findByLastName(String firstName, Pageable pageable);

  /**
   * Sample nickel query to return entities based on firstName and address.country
   *
   * <p>#(#n1ql.bucket): Use this syntax avoids hard-coding your bucket name in the query
   * #{#n1ql.selectEntity}: syntax-sugar to SELECT * FROM #(#n1ql.bucket):
   *
   * <p>#{#n1ql.filter}: syntax-sugar to filter the document by type, technically it means class =
   * �myPackage.MyClassName� (_class is the attribute automatically added in the document to define
   * its type when you are working with Couchbase on Spring Data )
   *
   * <p>#{#n1ql.fields} will be replaced by the list of fields (eg. for a SELECT clause) necessary
   * to reconstruct the entity.
   *
   * @param firstName
   * @param country
   * @return
   */
  @Query("#{#n1ql.selectEntity} where #{#n1ql.filter} and firstName = $1 and address.country = $2")
  List<User> findByFirstNameAndCountry(String firstName, String country);

  /**
   * Find users with account balance in any of their bank accounts
   *
   * @param accountBalance
   * @return
   */
  @Query(
      "#{#n1ql.selectEntity} where #{#n1ql.filter} AND ANY account IN accounts SATISFIES account.accountBalance >= $1 END")
  List<User> findByAccountBalance(Integer accountBalance);

  /**
   * Find users with account balance in every bank accounts
   *
   * @param accountBalance
   * @return
   */
  @Query(
      "#{#n1ql.selectEntity} where #{#n1ql.filter} AND EVERY account IN accounts SATISFIES account.accountBalance >= $1 END")
  List<User> findByAccountBalanceInAllBanks(Integer accountBalance);

  /**
   * Find users by value in direct or any child descendants
   *
   * @param value
   * @return
   */
  @Query("#{#n1ql.selectEntity} where #{#n1ql.filter} and $1 within #{#n1ql.bucket}")
  List<User> findByValue(String value);

  /**
   * Find users for a particular age
   *
   * @param param
   * @return
   */
  @Query("SELECT COUNT(*) AS count FROM #{#n1ql.bucket} WHERE #{#n1ql.filter} and age = $1")
  Long countByAge(Integer param);
}
