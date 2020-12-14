/* Licensed under Apache-2.0 */
package com.rico.security.services;

import com.rico.security.domain.NamesOnly;
import com.rico.security.entity.Person;
import com.rico.security.repository.PersonRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.common.exception.GenericServiceException;
import ro.common.rest.CommonErrorCodes;
import ro.common.utils.HttpStatusCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Sample Service for SpringData person entity document
 *
 * @author r.krishnakumar
 */
@Service
@Transactional(rollbackFor = {GenericServiceException.class})
@Log4j2
@SuppressWarnings({"squid:S1192"})
public class PersonService {

  @Autowired private PersonRepository personRepository;

  /**
   * Service method to save Person entity
   *
   * @param person
   * @return
   * @throws GenericServiceException
   */
  public Person save(Person person) throws GenericServiceException {
    try {
      return personRepository.save(person);
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
      return personRepository.findById(id);
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
  public Page<Person> getAllPersonAsPages(int indexNum) throws GenericServiceException {
    try {
      PageRequest pageRequest = PageRequest.of(indexNum, 10);
      return personRepository.findAll(pageRequest);
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
  public Page<Person> getPersonByLastNameAsPages(String lastName, int indexNum)
      throws GenericServiceException {
    try {
      PageRequest pageRequest = PageRequest.of(indexNum, 10, Sort.by(Direction.DESC, "lastName"));
      return personRepository.findByLastName(lastName, pageRequest);
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
      return personRepository.findByFirstNameEndsWith(firstName);
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
      return Optional.ofNullable(personRepository.findByEmailAddress(email));
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
      return personRepository.updateFirstNameById(firstName, id);
    } catch (Exception e) {
      log.error("Error in updating the Person ", e);
      throw new GenericServiceException(
          "Error", CommonErrorCodes.E_HTTP_BAD_REQUEST, HttpStatusCode.INTERNAL_SERVER_ERROR, e);
    }
  }

  /**
   * Service method to retrieve names only for Person by last name
   *
   * @return
   * @throws GenericServiceException
   */
  public List<NamesOnly> getPersonNamesOnlyByLastName(String lastName)
      throws GenericServiceException {
    try {
      return personRepository.findByLastName(lastName);
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
      return personRepository.findByAgeGreaterThanEqual(age);
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
      return personRepository.findAllBySorted(Sort.by(Direction.ASC, "firstName"));
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
  public List<Person> getAllPersonByCreatedDate(LocalDateTime date) throws GenericServiceException {
    try {
      return personRepository.findAllByCreatedDate(date);
    } catch (Exception e) {
      log.error("Error in retrieving the Person ", e);
      throw new GenericServiceException(
          "Error", CommonErrorCodes.E_HTTP_BAD_REQUEST, HttpStatusCode.INTERNAL_SERVER_ERROR, e);
    }
  }

  /**
   * Service method to delete Person by id
   *
   * @param id
   * @return
   * @throws GenericServiceException
   */
  public void deletePersonById(Long id) throws GenericServiceException {
    try {
      personRepository.deleteById(id);
    } catch (Exception e) {
      log.error("Error in deleting the Person ", e);
      throw new GenericServiceException(
          "Error", CommonErrorCodes.E_HTTP_BAD_REQUEST, HttpStatusCode.INTERNAL_SERVER_ERROR, e);
    }
  }
}
