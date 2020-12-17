/* Licensed under Apache-2.0 */
package com.rico.hibernate.services;

import com.rico.hibernate.domain.NamesOnly;
import com.rico.hibernate.entity.Person;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.common.exception.GenericServiceException;
import ro.common.hibernate.GenericDAO;
import ro.common.rest.CommonErrorCodes;
import ro.common.utils.HttpStatusCode;

/**
 * Sample Service for Hibernate person entity document
 *
 * @author r.krishnakumar
 */
@Service
@Transactional(rollbackFor = {GenericServiceException.class})
@Log4j2
@SuppressWarnings({"squid:S1192"})
public class PersonService {

  @Autowired private GenericDAO personRepository;

  /**
   * Service method to save Person entity
   *
   * @param person
   * @return
   * @throws GenericServiceException
   */
  public Person save(Person person) throws GenericServiceException {
    try {
      return personRepository.saveEntity(person);
    } catch (Exception e) {
      log.error("Error in saving the Person ", e);
      throw new GenericServiceException(
          "Error", CommonErrorCodes.E_HTTP_BAD_REQUEST, HttpStatusCode.INTERNAL_SERVER_ERROR, e);
    }
  }

  /**
   * Service method to get Person entity by primary key
   *
   * @param id
   * @return
   * @throws GenericServiceException
   */
  public Optional<Person> getPersonById(Long id) throws GenericServiceException {
    try {
      return Optional.ofNullable(personRepository.getEntity(Person.class, id));
    } catch (Exception e) {
      log.error("Error in retrieving the Person ", e);
      throw new GenericServiceException(
          "Error", CommonErrorCodes.E_HTTP_BAD_REQUEST, HttpStatusCode.INTERNAL_SERVER_ERROR, e);
    }
  }

  /**
   * Service method to get all Person entity as pages
   *
   * @param indexNum
   * @return
   * @throws GenericServiceException
   */
  public List<Person> getAllPersonAsPages(int indexNum) throws GenericServiceException {
    try {
      String sqlQuery =
          "select  p.first_name as firstName, p.last_name as lastName, p.age as age, p.email as emailAddress from person p";
      return personRepository.getRecordsWithLimit(Person.class, indexNum, 10, sqlQuery);
    } catch (Exception e) {
      log.error("Error in retrieving the Person ", e);
      throw new GenericServiceException(
          "Error", CommonErrorCodes.E_HTTP_BAD_REQUEST, HttpStatusCode.INTERNAL_SERVER_ERROR, e);
    }
  }

  /**
   * Service method to get all Person entity with address entity as pages Use join fetch hql to get
   * the lazy loaded Address entity without causing N+1 problem. Should not use limit query with
   * fetch join as it loads the results to the memory and then apply the limit. Use Native SQL to
   * join tables with limit operations(Paginations).
   *
   * @return
   * @throws GenericServiceException
   */
  public List<Person> getAllPersonWithAddress() throws GenericServiceException {
    try {
      String hqlQuery = "from Person p join fetch p.addressCollection";
      return personRepository.getEntitiesNoParam(hqlQuery);
    } catch (Exception e) {
      log.error("Error in retrieving the Person ", e);
      throw new GenericServiceException(
          "Error", CommonErrorCodes.E_HTTP_BAD_REQUEST, HttpStatusCode.INTERNAL_SERVER_ERROR, e);
    }
  }

  /**
   * Service method to get all Person entity by resident status as pages using hql
   *
   * @return
   * @throws GenericServiceException
   */
  public List<Person> getAllPersonByResidentStatus(String residentStatus)
      throws GenericServiceException {
    try {
      String hqlQuery = "from Person p join fetch p.addressCollection a where a.residentStatus = ?";
      return personRepository.getEntitiesByStringParam(hqlQuery, residentStatus);
    } catch (Exception e) {
      log.error("Error in retrieving the Person ", e);
      throw new GenericServiceException(
          "Error", CommonErrorCodes.E_HTTP_BAD_REQUEST, HttpStatusCode.INTERNAL_SERVER_ERROR, e);
    }
  }

  /**
   * Service method to get all Person entity by country as pages using hql
   *
   * @param country
   * @param age, country
   * @return
   * @throws GenericServiceException
   */
  public List<Person> getAllPersonByAgeAndCountry(int age, String country)
      throws GenericServiceException {
    try {
      String hqlQuery =
          "from Person p join fetch p.addressCollection a where p.age >= ? and a.country = ?";
      return personRepository.getEntitiesByIntAndStringParam(hqlQuery, age, country);
    } catch (Exception e) {
      log.error("Error in retrieving the Person ", e);
      throw new GenericServiceException(
          "Error", CommonErrorCodes.E_HTTP_BAD_REQUEST, HttpStatusCode.INTERNAL_SERVER_ERROR, e);
    }
  }

  /**
   * Service method to get all Person entity by LastName as pages
   *
   * @param lastName
   * @param indexNum
   * @return
   * @throws GenericServiceException
   */
  public List<Person> getPersonByLastNameAsPages(String lastName, int indexNum)
      throws GenericServiceException {
    try {
      String hqlQuery = "from Person where lastName=?";
      return personRepository.getEntitiesByStringParamWithLimit(hqlQuery, indexNum, 10, lastName);
    } catch (Exception e) {
      log.error("Error in retrieving the Person ", e);
      throw new GenericServiceException(
          "Error", CommonErrorCodes.E_HTTP_BAD_REQUEST, HttpStatusCode.INTERNAL_SERVER_ERROR, e);
    }
  }

  /**
   * Service method to get Person object by FirstName ends with
   *
   * @param firstName
   * @return
   * @throws GenericServiceException
   */
  public List<Person> getPersonByFirstNameEndsWith(String firstName)
      throws GenericServiceException {
    try {
      String sqlQuery =
          "select p.first_name as firstName, p.last_name as lastName, p.age as age, p.email as emailAddress from person p where p.first_name like ?";
      return personRepository.getRecords(Person.class, sqlQuery, "%" + firstName);
    } catch (Exception e) {
      log.error("Error in retrieving the Person ", e);
      throw new GenericServiceException(
          "Error", CommonErrorCodes.E_HTTP_BAD_REQUEST, HttpStatusCode.INTERNAL_SERVER_ERROR, e);
    }
  }

  /**
   * Service method to get Person object by Email
   *
   * @param email
   * @return
   * @throws GenericServiceException
   */
  public Optional<Person> getPersonByEmail(String email) throws GenericServiceException {
    try {
      String hql = "from Person where email=?";
      return Optional.ofNullable(personRepository.getEntityByStringParam(hql, email));
    } catch (Exception e) {
      log.error("Error in retrieving the Person ", e);
      throw new GenericServiceException(
          "Error", CommonErrorCodes.E_HTTP_BAD_REQUEST, HttpStatusCode.INTERNAL_SERVER_ERROR, e);
    }
  }

  /**
   * Service method to update first name for the Person by id
   *
   * @param id
   * @return
   * @throws GenericServiceException
   */
  public int updatePersonFirstNameById(String firstName, Long id) throws GenericServiceException {
    try {
      String sqlQuery = "update person set first_name = ? where id = ?";
      return personRepository.updateRecords(sqlQuery, firstName, id);
    } catch (Exception e) {
      log.error("Error in updating the Person ", e);
      throw new GenericServiceException(
          "Error", CommonErrorCodes.E_HTTP_BAD_REQUEST, HttpStatusCode.INTERNAL_SERVER_ERROR, e);
    }
  }

  /**
   * Service method to retrieve names only for Person by last name
   *
   * @param lastName
   * @return
   * @throws GenericServiceException
   */
  public List<NamesOnly> getPersonNamesOnlyByLastName(String lastName)
      throws GenericServiceException {
    try {
      String sqlQuery =
          "select p.first_name as firstName, p.last_name as lastName from person p where p.last_name = ?";
      return personRepository.getRecords(NamesOnly.class, sqlQuery, lastName);
    } catch (Exception e) {
      log.error("Error in retrieving the Person NamesOnly ", e);
      throw new GenericServiceException(
          "Error", CommonErrorCodes.E_HTTP_BAD_REQUEST, HttpStatusCode.INTERNAL_SERVER_ERROR, e);
    }
  }

  /**
   * Service method to get Person entity by age
   *
   * @param age
   * @return
   * @throws GenericServiceException
   */
  public List<Person> getAllPersonByAge(Integer age) throws GenericServiceException {
    try {
      String hqlQuery = "from Person where age >= ?";
      return personRepository.getEntitiesByIntParam(hqlQuery, age);
    } catch (Exception e) {
      log.error("Error in retrieving the Person ", e);
      throw new GenericServiceException(
          "Error", CommonErrorCodes.E_HTTP_BAD_REQUEST, HttpStatusCode.INTERNAL_SERVER_ERROR, e);
    }
  }

  /**
   * Service method to get all Person sorted by first name
   *
   * @return
   * @throws GenericServiceException
   */
  public List<Person> getAllPersonSortedByFirstName() throws GenericServiceException {
    try {
      String hqlQuery = "from Person order by firstName asc";
      return personRepository.getEntitiesNoParam(hqlQuery);
    } catch (Exception e) {
      log.error("Error in retrieving the Person ", e);
      throw new GenericServiceException(
          "Error", CommonErrorCodes.E_HTTP_BAD_REQUEST, HttpStatusCode.INTERNAL_SERVER_ERROR, e);
    }
  }

  /**
   * Service method to get all Person by create date
   *
   * @param date
   * @return
   * @throws GenericServiceException
   */
  public List<Person> getAllPersonByCreatedDate(LocalDate date) throws GenericServiceException {
    try {
      String hqlQuery = "from Person where createdDate = ?";
      return personRepository.getEntitiesByDateParam(hqlQuery, date);
    } catch (Exception e) {
      log.error("Error in retrieving the Person ", e);
      throw new GenericServiceException(
          "Error", CommonErrorCodes.E_HTTP_BAD_REQUEST, HttpStatusCode.INTERNAL_SERVER_ERROR, e);
    }
  }

  /**
   * Service method to delete Person by id
   *
   * @param p
   * @return
   * @throws GenericServiceException
   */
  public void deletePersonById(Person p) throws GenericServiceException {
    try {
      personRepository.deleteEntity(p);
    } catch (Exception e) {
      log.error("Error in deleting the Person ", e);
      throw new GenericServiceException(
          "Error", CommonErrorCodes.E_HTTP_BAD_REQUEST, HttpStatusCode.INTERNAL_SERVER_ERROR, e);
    }
  }
}
