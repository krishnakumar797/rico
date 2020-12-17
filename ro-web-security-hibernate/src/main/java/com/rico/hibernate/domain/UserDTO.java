/* Licensed under Apache-2.0 */
package com.rico.hibernate.domain;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ro.common.rest.CommonDTO;
import ro.common.rest.CommonErrorCodes;

/**
 * Person DTO model
 *
 * @author krishna
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends CommonDTO {

  @NotBlank(message = CommonErrorCodes.E_HTTP_BAD_REQUEST)
  private String userName;

  @NotBlank(message = CommonErrorCodes.E_HTTP_BAD_REQUEST)
  private String password;
}
