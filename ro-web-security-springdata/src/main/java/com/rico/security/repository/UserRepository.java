/* Licensed under Apache-2.0 */
package com.rico.security.repository;

import ro.common.security.User;
import ro.common.springdata.CommonCrudRepository;

/**
 * Sample repository implementation for SpringData
 *
 * @author r.krishnakumar
 */
public interface UserRepository extends CommonCrudRepository<User, Long> {}
