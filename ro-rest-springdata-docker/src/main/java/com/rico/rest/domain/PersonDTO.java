/* Licensed under Apache-2.0 */
package com.rico.rest.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
public class PersonDTO extends CommonDTO {

  @NotBlank(message = CommonErrorCodes.E_HTTP_BAD_REQUEST)
  private String firstName;

  @NotBlank(message = CommonErrorCodes.E_HTTP_BAD_REQUEST)
  private String lastName;

  @NotNull(message = CommonErrorCodes.E_HTTP_BAD_REQUEST)
  private Integer age;

  @NotBlank(message = CommonErrorCodes.E_HTTP_BAD_REQUEST)
  private String emailAddress;
}
