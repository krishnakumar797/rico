/* Licensed under Apache-2.0 */
package com.rico.springdata.services;

import com.rico.springdata.entity.Address;
import com.rico.springdata.repository.AddressRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.common.exception.GenericServiceException;
import ro.common.rest.CommonErrorCodes;
import ro.common.utils.HttpStatusCode;

/**
 * Sample Service for SpringData address entity document
 *
 * @author r.krishnakumar
 */
@Service
@Transactional(rollbackFor = {GenericServiceException.class})
@Log4j2
@SuppressWarnings({"squid:S1192"})
public class AddressService {

  @Autowired private AddressRepository addressRepository;

  /**
   * Service method to save Address entity
   *
   * @param person
   * @return
   * @throws GenericServiceException
   */
  public Address save(Address address) throws GenericServiceException {
    try {
      return addressRepository.save(address);
    } catch (Exception e) {
      log.error("Error in saving the Person ", e);
      throw new GenericServiceException(
          "Error", CommonErrorCodes.E_HTTP_BAD_REQUEST, HttpStatusCode.INTERNAL_SERVER_ERROR, e);
    }
  }
}
