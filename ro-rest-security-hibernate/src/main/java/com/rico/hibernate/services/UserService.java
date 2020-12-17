/* Licensed under Apache-2.0 */
package com.rico.hibernate.services;

import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.common.exception.GenericServiceException;
import ro.common.hibernate.GenericDAO;
import ro.common.rest.CommonErrorCodes;
import ro.common.security.User;
import ro.common.utils.HttpStatusCode;

/**
 * Sample Service for Hibernate user entity document
 *
 * @author r.krishnakumar
 */
@Service
@Transactional(rollbackFor = {GenericServiceException.class})
@Log4j2
public class UserService {

  @Autowired private GenericDAO userRepository;

  /**
   * Service method to get User entity by primary key
   *
   * @param id
   * @return
   * @throws GenericServiceException
   */
  public Optional<User> getUserById(Long id) throws GenericServiceException {
    try {
      return Optional.ofNullable(userRepository.getEntity(User.class, id));
    } catch (Exception e) {
      log.error("Error in retrieving the User ", e);
      throw new GenericServiceException(
          "Error", CommonErrorCodes.E_HTTP_BAD_REQUEST, HttpStatusCode.INTERNAL_SERVER_ERROR, e);
    }
  }
}
