/* Licensed under Apache-2.0 */
package com.rico.security.services;

import com.rico.security.repository.UserRepository;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.common.exception.GenericServiceException;
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

  @Autowired private UserRepository userRepository;

  /**
   * Service method to get User entity by primary key
   *
   * @param id
   * @return
   * @throws GenericServiceException
   */
  public Optional<User> getUserById(Long id) throws GenericServiceException {
    try {
      return userRepository.findById(id);
    } catch (Exception e) {
      log.error("Error in retrieving the User ", e);
      throw new GenericServiceException(
          "Error", CommonErrorCodes.E_HTTP_BAD_REQUEST, HttpStatusCode.INTERNAL_SERVER_ERROR, e);
    }
  }
}
