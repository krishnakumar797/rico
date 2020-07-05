/* Licensed under Apache-2.0 */
package com.rico.springdata.repository;

import com.rico.springdata.entity.Address;

import ro.common.springdata.CommonCrudRepository;

/**
 * Sample repository implementation for SpringData
 *
 * @author r.krishnakumar
 */
public interface AddressRepository extends CommonCrudRepository<Address, Long> {

}
