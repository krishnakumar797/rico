/* Licensed under Apache-2.0 */
package com.rico.couchbase.services;

import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.kv.LookupInResult;
import com.couchbase.client.java.kv.MutateInResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rico.couchbase.documents.Address;
import com.rico.couchbase.documents.User;
import com.rico.couchbase.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ro.common.couchbase.CommonCBService;

/**
 * Sample Service for Couchbase user document
 *
 * @author r.krishnakumar
 */
@Service
public class UserService extends CommonCBService<User> {

  @Autowired private UserRepository userRepository;

  @Autowired private String bucketName;

  @Autowired private ObjectMapper objectMapper;

  public UserService(@Autowired CouchbaseTemplate couchbaseTemplate, @Autowired Cluster cluster) {
    super(couchbaseTemplate, cluster);
  }

  /**
   * Sample service method to retrieve user by first name
   *
   * @param firstName
   * @return
   */
  public List<User> getUserByFirstName(String firstName) {
    return userRepository.findByFirstName(firstName);
  }

  /**
   * Sample service method to retrieve users by their lastName as pages
   *
   * @param lastName
   * @param pageable
   * @return
   */
  public Page<User> getUserByLastName(String lastName, Pageable pageable) {
    return userRepository.findByLastName(lastName, pageable);
  }

  /**
   * Sample service method to retrieve all users as pages
   *
   * @param pageable
   * @return
   */
  public Page<User> getAllUsers(Pageable pageable) {
    return userRepository.findAll(pageable);
  }

  /**
   * Sample service method to retrieve users by firstName and country
   *
   * @param firstName
   * @param country
   * @return
   */
  public List<User> getUsersByFirstNameAndContry(String firstName, String country) {
    return userRepository.findByFirstNameAndCountry(firstName, country);
  }

  /**
   * Sample service method to retrieve users by accountBalance
   *
   * @param accountBalance
   * @return
   */
  public List<User> getUsersByAccountBalance(Integer accountBalance) {
    return userRepository.findByAccountBalance(accountBalance);
  }

  /**
   * Sample service method to retrieve users by accountBalance
   *
   * @param accountBalance
   * @return
   */
  public List<User> getUsersByAccountBalanceInAllBanks(Integer accountBalance) {
    return userRepository.findByAccountBalanceInAllBanks(accountBalance);
  }

  /**
   * Sample service method to retrieve users by location either by home address or business
   *
   * @return
   */
  public List<User> getUsersByLocation(String location) {
    return userRepository.findByValue(location);
  }

  /**
   * Sample service method to retrieve firstName of all users To-Do : Need rework
   *
   * @return
   */
  //  public Optional<List<JsonNode>> getUserNamesByCountry(String country) {
  //    String statement =
  //        "SELECT firstName, lastName FROM "
  //            + bucketName
  //            + " WHERE "
  //            + userRepository.getDocumentFilter(User.class)
  //            + " and address.country like $name";
  //    JsonObject namedParams = JsonObject.create().put("name", country + "%");
  //    N1qlQuery query = N1qlQuery.parameterized(statement, namedParams);
  //    N1qlQueryResult result = userRepository.execute(query);
  //    List<JsonNode> jsonNode =
  //        result.allRows().stream()
  //            .map(
  //                row -> {
  //                  try {
  //                    return objectMapper.readTree(row.value().toString());
  //                  } catch (IOException e) {
  //                    return null;
  //                  }
  //                })
  //            .filter(Objects::nonNull)
  //            .collect(Collectors.toList());
  //    return Optional.ofNullable(jsonNode);
  //  }

  /**
   * Sample service method to update the pincode by docId
   *
   * @param docId
   * @param value
   * @return
   */
  public String updatePinCodeById(String docId, String value) {
    MutateInResult fragment = updateSubDocument(User.class, docId, "address.pinCode", value);
    return fragment.contentAs(0, String.class);
  }

  /**
   * Sample service method to update the pincode by docId
   *
   * @param docId
   * @return
   * @throws JsonProcessingException
   * @throws JsonMappingException
   */
  public Address getAddressByUserId(String docId) throws JsonProcessingException {
    LookupInResult fragment = retrieveSubDocument(User.class, docId, "address");
    return fragment.contentAs(0, Address.class);
  }

  /**
   * Sample service method to store user document
   *
   * @param u
   * @return
   */
  public User saveUser(User u) {
    return userRepository.save(u);
  }

  /**
   * Sample service method to delete user document
   *
   * @param u
   * @return
   */
  public void deleteUser(User u) {
    userRepository.delete(u);
  }
}
