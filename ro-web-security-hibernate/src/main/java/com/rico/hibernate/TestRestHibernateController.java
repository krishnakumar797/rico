/* Licensed under Apache-2.0 */
package com.rico.hibernate;

import com.rico.hibernate.domain.NamesOnly;
import com.rico.hibernate.domain.PersonDTO;
import com.rico.hibernate.entity.Person;
import com.rico.hibernate.services.PersonService;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ro.common.exception.CommonRestException;
import ro.common.exception.GenericServiceException;
import ro.common.rest.CommonDTOConverter;
import ro.common.rest.CommonErrorCodes;
import ro.common.security.UserLoginService;
import ro.common.utils.HttpStatusCode;

/** Testing Rest controller service */
@RestController
@Validated
@Log4j2
public class TestRestHibernateController {

  @Autowired private PersonService service;

  @Autowired private CommonDTOConverter dtoConverter;

  @Autowired private UserLoginService loginService;

  /**
   * Get method to retrieve names only from the person table
   *
   * @param lastName
   * @param headers
   * @return
   * @throws CommonRestException
   */
  @GetMapping(
      value = "/person/{lastName}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public List<NamesOnly> getPersonNames(
      @NotBlank(message = CommonErrorCodes.E_HTTP_BAD_REQUEST) @PathVariable final String lastName,
      @RequestHeader HttpHeaders headers)
      throws CommonRestException {
    try {
      return service.getPersonNamesOnlyByLastName(lastName);
    } catch (GenericServiceException e) {
      log.error("Test Rest error ", e);
      throw new CommonRestException(
          CommonErrorCodes.E_GEN_INTERNAL_ERR,
          headers,
          HttpStatusCode.SERVICE_UNAVAILABLE,
          e.getMessage(),
          e);
    }
  }

  /**
   * Get method to retrieve person using id
   *
   * @param id
   * @param headers
   * @return
   * @throws CommonRestException
   */
  @GetMapping(
      value = "/person",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public PersonDTO getPerson(
      @NotBlank(message = CommonErrorCodes.E_HTTP_BAD_REQUEST)
          @RequestParam(value = "id", required = true)
          final String id,
      @RequestHeader HttpHeaders headers)
      throws CommonRestException {
    try {
      return dtoConverter.convertToDto(
          service.getPersonById(Long.parseLong(id)).orElse(new Person()), PersonDTO.class);
    } catch (GenericServiceException e) {
      log.error("Test Rest error ", e);
      throw new CommonRestException(
          CommonErrorCodes.E_GEN_INTERNAL_ERR,
          headers,
          HttpStatusCode.SERVICE_UNAVAILABLE,
          e.getMessage(),
          e);
    }
  }

  /**
   * POST Method to store a new person entity. Use only DTO with validation properties objects with
   * RequestBody annotations. Use common dto converter to convert to entity from DTO class.
   *
   * @param personDTO
   * @param headers
   * @return
   * @throws CommonRestException
   */
  @PostMapping(
      value = "/person",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Person savePerson(
      @Valid @RequestBody PersonDTO personDTO, @RequestHeader HttpHeaders headers)
      throws CommonRestException {
    try {
      Person person = dtoConverter.convertToEntity(personDTO, Person.class);
      return service.save(person);
    } catch (GenericServiceException e) {
      log.error("Test Rest error ", e);
      throw new CommonRestException(
          CommonErrorCodes.E_GEN_INTERNAL_ERR,
          headers,
          HttpStatusCode.SERVICE_UNAVAILABLE,
          e.getMessage(),
          e);
    }
  }
}
