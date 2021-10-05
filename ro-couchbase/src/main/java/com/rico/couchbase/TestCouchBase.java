/* Licensed under Apache-2.0 */
package com.rico.couchbase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rico.couchbase.documents.Accounts;
import com.rico.couchbase.documents.Address;
import com.rico.couchbase.documents.Business;
import com.rico.couchbase.documents.User;
import com.rico.couchbase.services.UserService;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

/**
 * Testing Couchbase service
 *
 * @author r.krishnakumar
 */
@Component
@Log4j2
public class TestCouchBase {

  @Autowired private UserService userService;

  /** Sample method showing saving and retrieval of a couchbase document */
  @PostConstruct
  @SuppressWarnings({"squid:S1192"})
  public void saveUser() {

    Address address =
        new Address(
            "Studio 103",
            "61 Wellfield Road",
            "Roath",
            "Cardiff",
            "England",
            "Resident",
            "CF24 3DG");
    Business business = new Business("Mark Twain", "Partnership", "Australia");
    User user = new User("private", "t", 25, address, getAccounts(), business);
    userService.saveUser(user);

    address =
        new Address(
            "Unit 14",
            "3 Edgar Buildings",
            "George Street",
            "Bath",
            "Scotland",
            "Resident",
            "BA1 2FJ");
    business = new Business("Lovell", "Manufacturing", "Ireland");
    user = new User("rico", "ro", 24, address, getAccounts(), business);
    userService.saveUser(user);

    address =
        new Address(
            "Box 777",
            "91 Western Road",
            "Brighton",
            "East Sussex",
            "England",
            "Resident",
            "BN1 2NW");
    business = new Business("ACME", "Construction", "Scotland");
    user = new User("kowalski", "k", 26, address, getAccounts(), business);
    userService.saveUser(user);

    // Retrieving users by firstName
    List<User> userList = userService.getUserByFirstName("private");
    userList.forEach(u -> log.info("USERS RETRIEVED BY FIRST NAME " + u.getFirstName()));

    // Retrieving users by lastName as pages
    Page<User> pages =
        userService.getUserByLastName(
            "ro", PageRequest.of(0, 10, Sort.by(Direction.DESC, "lastName")));
    pages.forEach(u -> log.info("USERS RETRIEVED BY LAST NAME AS PAGES " + u.getLastName()));

    // Retrieving all users as pages
    pages = userService.getAllUsers(PageRequest.of(0, 10, Sort.by(Direction.DESC, "firstName")));
    pages.forEach(u -> log.info("USERS RETRIEVED AS PAGES " + u.getFirstName()));

    // Retrieving users by firstName and country
    userList = userService.getUsersByFirstNameAndContry("rico", "Scotland");
    userList.forEach(
        u ->
            log.info(
                "USERS RETRIEVED BY FIRSTNAME AND COUNTRY "
                    + u.getFirstName()
                    + " "
                    + u.getAddress().getCountry()));

    // Retrieving users by Account Balance in any bank
    userList = userService.getUsersByAccountBalance(500000);
    userList.forEach(u -> log.info("USERS RETRIEVED BY ACCOUNT BALANCE " + u.getFirstName()));

    // Retrieving users by Account Balance in all bank
    userList = userService.getUsersByAccountBalanceInAllBanks(500000);
    userList.forEach(u -> log.info("USERS RETRIEVED BY ACCOUNT BALANCE " + u.getFirstName()));
    if (userList.isEmpty()) {
      log.info("NO USERS HAVE THE REQUIRED ACCOUNT BALANCE IN ALL THEIR BANK ACCOUNTS");
    }

    // Retrieving users by location either with their home address or with their business
    userList = userService.getUsersByLocation("Scotland");
    userList.forEach(u -> log.info("USERS RETRIEVED BY LOCATION " + u.getFirstName()));

    // Retrieving users names by country
    //    Optional<List<JsonNode>> optional = userService.getUserNamesByCountry("England");
    //    if (optional.isPresent()) {
    //      optional
    //          .get()
    //          .forEach(node -> log.info("USERNAMES RETRIEVED BY COUNTRY " +
    // node.toPrettyString()));
    //    } else {
    //      log.warn("NO USERNAMES FOUND FOR THE SPECIFIED COUNTRY");
    //    }

    user = !userList.isEmpty() ? userList.get(0) : null;
    if (user != null) {
      // Updating a subdocument Address pincode by documentID
      log.info("UPDATING THE PINCODE FOR THE USER " + user.getFirstName());
      String status = userService.updatePinCodeById(user.getId(), "774-XYT");
      log.info("USER PINCODE UPDATED WITH STATUS " + status);
      // Retrieving a subdocument Address by documentID
      try {
        address = userService.getAddressByUserId(user.getId());
        log.info("SUCCESSFULLY RETRIEVED ADDRESS " + address.getCity());
      } catch (JsonProcessingException e) {
        log.error("JSON MAPPING ERROR " + e.getMessage());
      }
    }

    // Deleting all user documents
    pages = userService.getAllUsers(PageRequest.of(0, 10, Sort.by(Direction.DESC, "firstName")));
    pages.forEach(userService::deleteUser);
    log.info("SUCCESSFULLY DELETED ALL DOCUMENTS");
  }

  /**
   * Util method to create accounts
   *
   * @return
   */
  @SuppressWarnings({"squid:S1192"})
  private List<Accounts> getAccounts() {
    return Arrays.asList(
        new Accounts("Horizon First Bank", "Savings", 589400),
        new Accounts("Advantage Credit Union", "Savings", 452000),
        new Accounts("Service Secure Credit Union", "Savings", 326000));
  }
}
