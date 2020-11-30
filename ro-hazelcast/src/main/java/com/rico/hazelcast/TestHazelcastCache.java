/* Licensed under Apache-2.0 */
package com.rico.hazelcast;

import com.rico.hazelcast.entity.Address;
import com.rico.hazelcast.entity.Person;
import com.rico.hazelcast.services.PersonService;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ro.common.exception.GenericServiceException;

/**
 * Testing Hazelcast cache
 *
 * @author r.krishnakumar
 */
@Component
@Log4j2
public class TestHazelcastCache {

  @Autowired private PersonService personService;

  /**
   * Sample method showing saving and retrieval of a Person entity document. Caching wont work with
   * post construct method as it requires AOP proxy bean invocations. Inorder to solve it, here used
   * ApplicationReadyEvent for initialization operation
   */
  @EventListener(ApplicationReadyEvent.class)
  @SuppressWarnings({"squid:S1192", "squid:S3776"})
  public void savePerson() {

    // Saving Person objects
    try {
      Person[] personArray = new Person[4];
      personArray[0] = new Person("Julien", "XIII", 25, "kjulien@dreamworks.com");
      personArray[1] = new Person("Mort", "mort", 23, "mort@dreamworks.com");
      personArray[2] = new Person("Melmen", "rt", 27, "melmen@dreamworks.com");
      personArray[3] = new Person("Alex", "xrt", 26, "alex@dreamworks.com");
      personArray[0]
          .getAddressCollection()
          .add(
              new Address(
                  "Studio 103",
                  "61 Wellfield Road",
                  "Roath",
                  "Cardiff",
                  "England",
                  "Resident",
                  "CF24 3DG",
                  personArray[0]));
      personArray[1]
          .getAddressCollection()
          .add(
              new Address(
                  "Unit 14",
                  "3 Edgar Buildings",
                  "George Street",
                  "Bath",
                  "Scotland",
                  "Resident",
                  "BA1 2FJ",
                  personArray[1]));
      personArray[2]
          .getAddressCollection()
          .add(
              new Address(
                  "Unit 14",
                  "3 Edgar Buildings",
                  "George Street",
                  "Bath",
                  "Scotland",
                  "Resident",
                  "BA1 2FJ",
                  personArray[2]));
      personArray[3]
          .getAddressCollection()
          .add(
              new Address(
                  "Box 777",
                  "91 Western Road",
                  "Brighton",
                  "East Sussex",
                  "England",
                  "Non Resident",
                  "BN1 2NW",
                  personArray[3]));
      for (Person p : personArray) {
        personService.save(p);
      }
    } catch (GenericServiceException e) {
      log.error("Error in saving Person");
      return;
    }

    List<Person> personList = new ArrayList<>();

    // Retrieving All Persons with their addresses. Adding the result data to the cache
    try {
      personList = personService.getAllPersonWithAddress();
      personList.forEach(
          p -> {
            p.getAddressCollection()
                .forEach(
                    a ->
                        log.info(
                            "SUCCESSFULLY RETRIEVED PERSON {} WITH ADDRESS {}",
                            p.getFirstName(),
                            a.getHouseName()));
          });
    } catch (GenericServiceException e) {
      log.error("Error in retrieving Person by address");
    }

    // Retrieving All Persons with their resident status. Adding the result data to the cache
    try {
      personList = personService.getAllPersonByResidentStatus("Non Resident");
      personList.forEach(
          p -> {
            p.getAddressCollection()
                .forEach(
                    a ->
                        log.info(
                            "SUCCESSFULLY RETRIEVED PERSON {} BY RESIDENT STATUS {}",
                            p.getFirstName(),
                            a.getResidentStatus()));
          });
    } catch (GenericServiceException e) {
      log.error("Error in retrieving Person by resident status");
    }

    // Retrieving All Persons with their addresses from cache. No SQL query will be fired for this
    // operation
    try {
      personList = personService.getAllPersonWithAddress();
      personList.forEach(
          p -> {
            p.getAddressCollection()
                .forEach(
                    a ->
                        log.info(
                            "SUCCESSFULLY RETRIEVED PERSON FROM CACHE {} WITH ADDRESS {}",
                            p.getFirstName(),
                            a.getHouseName()));
          });
    } catch (GenericServiceException e) {
      log.error("Error in retrieving Person by address");
    }
    // Updating Person first name by id and evicting the cache data
    try {
      int result =
          personService.updatePersonFirstNameById("King Julien", personList.get(0).getId());
      if (result != 0) {
        log.info("SUCCESSFULLY UPDATED PERSON OBJECT FIRSTNAME");
      } else {
        log.error("NOT ABLE TO UPDATE PERSON OBJECT FIRSTNAME");
      }
    } catch (GenericServiceException e) {
      log.error("Error in updating Person first name with id");
    }

    // Retrieving All Persons with their addresses since cache is evicted in the pervious step. This
    // will be a cache miss and hit the db for the data using sql quries.
    try {
      personList = personService.getAllPersonWithAddress();
      personList.forEach(
          p -> {
            p.getAddressCollection()
                .forEach(
                    a ->
                        log.info(
                            "SUCCESSFULLY RETRIEVED PERSON FROM DB {} WITH ADDRESS {}",
                            p.getFirstName(),
                            a.getHouseName()));
          });
    } catch (GenericServiceException e) {
      log.error("Error in retrieving Person by address");
    }

    // Deleting records
    personList.forEach(
        p -> {
          try {
            personService.deletePersonById(p);
            log.info("DELETING RECORD " + p.getFirstName());
          } catch (GenericServiceException e) {
            log.error("Error in deleting Person by id");
          }
        });
  }
}
