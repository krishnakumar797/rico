/* Licensed under Apache-2.0 */
package com.rico.springdata.repository;

import com.rico.springdata.domain.NamesOnly;
import com.rico.springdata.entity.Person;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.common.springdata.CommonCrudRepository;

/**
 * Sample repository implementation for SpringData
 *
 * @author r.krishnakumar
 */
public interface PersonRepository extends CommonCrudRepository<Person, Long> {

  List<Person> findByFirstName(String firstName);

  List<Person> findByAgeGreaterThanEqual(Integer age);

  /**
   * Method to return only names(partial view) instead of whole entity object
   *
   * @param lastName
   * @return
   */
  List<NamesOnly> findByLastName(String lastName);

  /**
   * Method to find Person by lastName as pages(used for pagination request)
   *
   * @param lastName
   * @param pageable
   * @return
   */
  Page<Person> findByLastName(String lastName, Pageable pageable);

  /**
   * Method to find Person with addresses as pages(used for pagination request)
   *
   * <p>#{#entityName} is to denote the domain type associated with the given repository here its
   * same as 'Person'
   *
   * @param pageable
   * @return
   */
  @Query("select p from #{#entityName} p join fetch p.addressCollection a")
  List<Person> findAllWithAddresses();

  /**
   * Method to find Person by firstName ends with using JPAQL
   *
   * <p>#{#entityName} is to denote the domain type associated with the given repository here its
   * same as 'Person'
   *
   * @param firstName
   * @return
   */
  @Query("select p from #{#entityName} p where p.firstName like %:firstName")
  List<Person> findByFirstNameEndsWith(@Param("firstName") String firstName);

  /**
   * Method to find Person by resident status using JPAQL
   *
   * <p>#{#entityName} is to denote the domain type associated with the given repository here its
   * same as 'Person'
   *
   * @param residentStatus
   * @return
   */
  @Query(
      "select p from #{#entityName} p join fetch p.addressCollection a where a.residentStatus = :residentStatus")
  List<Person> findByResidentStatus(@Param("residentStatus") String residentStatus);

  /**
   * Method to find Person by resident status using JPAQL
   *
   * <p>#{#entityName} is to denote the domain type associated with the given repository here its
   * same as 'Person'
   *
   * @param age
   * @param country
   * @return
   */
  @Query(
      "select p from #{#entityName} p join fetch p.addressCollection a where p.age >= :age and a.country = :country")
  List<Person> findByAgeAndCountry(@Param("age") int age, @Param("country") String country);

  /**
   * Method to find Person by email using SQL query
   *
   * @param emailAddress
   * @return
   */
  @Query(value = "select * from person where email = :email", nativeQuery = true)
  Person findByEmailAddress(@Param("email") String emailAddress);

  /**
   * Method to update properties rather than entire Entity doc using JPAQL
   *
   * @param firstName
   * @param id
   * @return
   */
  @Modifying
  @Query("update #{#entityName} p set p.firstName = :firstName where p.id = :id")
  int updateFirstNameById(@Param("firstName") String firstName, @Param("id") Long id);
}
