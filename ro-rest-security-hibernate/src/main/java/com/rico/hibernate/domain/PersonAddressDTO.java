/* Licensed under Apache-2.0 */
package com.rico.hibernate.domain;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Person DTO model
 *
 * @author krishna
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PersonAddressDTO extends PersonDTO {

  private List<AddressDTO> addressCollection;
}
