/* Licensed under Apache-2.0 */
package com.rico.hibernate;

import com.rico.hibernate.domain.NamesOnly;
import com.rico.hibernate.entity.Address;
import com.rico.hibernate.entity.Person;
import com.rico.hibernate.services.PersonService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.common.exception.GenericServiceException;

/**
 * Testing Hibernate service
 *
 * @author r.krishnakumar
 */
@Component
@Log4j2
public class TestHibernate {

  @Autowired private PersonService personService;

  /** Sample method showing saving and retrieval of a Person entity document */
  @SuppressWarnings({"squid:S1192", "squid:S3776"})
  public void savePerson() {

    // Saving Person object
    Person person = new Person("Julien", "XIII", 25, "kjulien@dreamworks.com");
    person
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
                person));
    try {
      person = personService.save(person);
      log.info("SUCCESSFULLY SAVED PERSON " + person.getId());
    } catch (GenericServiceException e) {
      log.error("Error in saving Person");
      return;
    }
    // Retrieving Person by id
    try {
      Optional<Person> optional = personService.getPersonById(person.getId());
      if (optional.isPresent()) {
        person = optional.get();
        log.info("SUCCESSFULLY RETRIEVED PERSON " + person.getFirstName());
      }
    } catch (GenericServiceException e) {
      log.error("Error in retrieving Person by Id");
    }
    // Retrieving Person by email id
    try {
      Optional<Person> optional = personService.getPersonByEmail(person.getEmailAddress());
      if (optional.isPresent()) {
        person = optional.get();
        log.info("SUCCESSFULLY RETRIEVED PERSON " + person.getEmailAddress());
      }
    } catch (GenericServiceException e) {
      log.error("Error in retrieving Person by email");
    }

    // Adding more data for pagination and sorting
    try {

      Person[] personArray = new Person[3];
      personArray[0] = new Person("Mort", "mort", 23, "mort@dreamworks.com");
      personArray[1] = new Person("Melmen", "rt", 27, "melmen@dreamworks.com");
      personArray[2] = new Person("Alex", "xrt", 26, "alex@dreamworks.com");
      personArray[0]
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
                  "Box 777",
                  "91 Western Road",
                  "Brighton",
                  "East Sussex",
                  "England",
                  "Non Resident",
                  "BN1 2NW",
                  personArray[2]));
      for (Person p : personArray) {
        personService.save(p);
      }
    } catch (GenericServiceException e) {
      log.error("Error in saving Person");
      return;
    }

    List<Person> personList = new ArrayList<>();

    // Retrieving All Persons with their addresses
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

    // Retrieving All Persons with their resident status
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

    // Retrieving All Persons with their country
    try {
      personList = personService.getAllPersonByAgeAndCountry(25, "England");
      personList.forEach(
          p -> {
            p.getAddressCollection()
                .forEach(
                    a ->
                        log.info(
                            "SUCCESSFULLY RETRIEVED PERSON {} BY COUNTRY AND AGE {} AND {}",
                            p.getFirstName(),
                            a.getCountry(),
                            p.getAge()));
          });
    } catch (GenericServiceException e) {
      log.error("Error in retrieving Person by country");
    }

    // Retrieving Person by age
    try {
      personList = personService.getAllPersonByAge(25);
      personList.forEach(
          p -> log.info("SUCCESSFULLY RETRIEVED PERSON BY AGE '>=25' " + p.getFirstName()));
    } catch (GenericServiceException e) {
      log.error("Error in retrieving Person by age");
    }

    // Retrieving Person by first name ends with 'en'
    try {
      personList = personService.getPersonByFirstNameEndsWith("en");
      personList.forEach(
          p ->
              log.info(
                  "SUCCESSFULLY RETRIEVED PERSON BY FIRSTNAME ENDS WITH 'en' " + p.getFirstName()));
    } catch (GenericServiceException e) {
      log.error("Error in retrieving Person by first name");
    }

    // Retrieving Person by last name as pages
    try {
      List<Person> pages = personService.getPersonByLastNameAsPages("rt", 0);
      pages.forEach(
          p ->
              log.info(
                  "SUCCESSFULLY RETRIEVED PERSON BY LASTNAME 'rt' "
                      + p.getFirstName()
                      + " "
                      + p.getLastName()));
    } catch (GenericServiceException e) {
      log.error("Error in retrieving Person by last name as pages");
    }

    // Retrieving all Person sorted by first name
    try {
      personList = personService.getAllPersonSortedByFirstName();
      personList.forEach(
          p ->
              log.info(
                  "SUCCESSFULLY RETRIEVED ALL PERSON SORTED BY FIRSTNAME " + p.getFirstName()));
    } catch (GenericServiceException e) {
      log.error("Error in retrieving Person sorted by first name");
    }

    // Retrieving all Person by created date from start of the day(mid night)
    try {
      personList =
          personService.getAllPersonByCreatedDate(LocalDate.now().atStartOfDay().toLocalDate());
      personList.forEach(
          p -> log.info("SUCCESSFULLY RETRIEVED ALL PERSON BY CREATED DATE " + p.getFirstName()));
    } catch (GenericServiceException e) {
      log.error("Error in retrieving Person by created date");
    }

    // Retrieving all Person as pages
    try {
      List<Person> pages = personService.getAllPersonAsPages(0);
      pages.forEach(
          p -> log.info("SUCCESSFULLY RETRIEVED ALL PERSON AS PAGES " + p.getFirstName()));
    } catch (GenericServiceException e) {
      log.error("Error in retrieving all Person as pages");
    }

    // Updating Person first name by id
    try {
      int result = personService.updatePersonFirstNameById("King Julien", person.getId());
      if (result != 0) {
        log.info("SUCCESSFULLY UPDATED PERSON OBJECT FIRSTNAME");
      } else {
        log.error("NOT ABLE TO UPDATE PERSON OBJECT FIRSTNAME");
      }
    } catch (GenericServiceException e) {
      log.error("Error in updating Person first name with id");
    }

    // Retrieving NamesOnly for Person object by last name
    try {
      List<NamesOnly> namesOnlyList = personService.getPersonNamesOnlyByLastName("XIII");
      namesOnlyList.forEach(
          n ->
              log.info("SUCCESSFULLY RETRIEVED NAMES ONLY BY LAST NAME 'XIII' " + n.getFullName()));
    } catch (GenericServiceException e) {
      log.error("Error in retrieving NamesOnly for Person by last name");
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
