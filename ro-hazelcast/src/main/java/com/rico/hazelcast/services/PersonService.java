/* Licensed under Apache-2.0 */
package com.rico.hazelcast.services;

import com.rico.hazelcast.entity.Person;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
   * Service method to get all Person entity with address entity as pages Use join fetch hql to get
   * the lazy loaded Address entity without causing N+1 problem. Should not use limit query with
   * fetch join as it loads the results to the memory and then apply the limit. Use Native SQL to
   * join tables with limit operations(Paginations).
   *
   * <p>The method result will be stored to the cache named cache1. The data will be stored to the
   * cache using the key name provided. Here, since the method parameter is empty, used the method
   * name as the key name. If you want to use a static value as the key name use the following
   * pattern #root.target.MY_KEY. Next time when the same method is called, the data is fetched from
   * the cache rather than executing the method.
   *
   * <p>public static final String MY_KEY = "mykey";
   *
   * @param indexNum
   * @return
   * @throws GenericServiceException
   */
  @Cacheable(value = "cache1", key = "#root.methodName")
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
   * <p>The method result will be stored to the cache named cache2. The data will be stored to the
   * cache using the key name as the parameter value of the method. We can either store all data in
   * the same cache with different key or use different caches altogether.
   *
   * <p>@Cacheable wont work if the @Cacheable decorated method is called from the same class. It
   * should be called from another class to invoke the proxy object.
   *
   * @param residentStatus
   * @return
   * @throws GenericServiceException
   */
  @Cacheable(value = "cache2")
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
   * Service method to update first name for the Person by id
   *
   * <p>Clearing the cache cache1. @CachePut with key can be used for partial update of an existing
   * cache rather than clearing whole cache using @CacheEvict allEntries = true
   *
   * <p>CacheEvict wont work if the @CacheEvict decorated method is called from the same class. It
   * should be called from another class to invoke the proxy object.
   *
   * @param id
   * @return
   * @throws GenericServiceException
   */
  @CacheEvict(value = "cache1", allEntries = true)
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
   * Service method to delete Person by id
   *
   * @param id
   * @return
   * @throws GenericServiceException
   */
  @CacheEvict(value = "cache1", allEntries = true)
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
