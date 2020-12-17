/* Licensed under Apache-2.0 */
package com.rico.hibernate.domain;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ro.common.rest.CommonDTO;
import ro.common.rest.CommonErrorCodes;

/**
 * Address DTO model
 *
 * @author krishna
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AddressDTO extends CommonDTO {

  @NotBlank(message = CommonErrorCodes.E_HTTP_BAD_REQUEST)
  private String houseName;

  @NotBlank(message = CommonErrorCodes.E_HTTP_BAD_REQUEST)
  private String streetName;

  @NotBlank(message = CommonErrorCodes.E_HTTP_BAD_REQUEST)
  private String residentStatus;

  @NotBlank(message = CommonErrorCodes.E_HTTP_BAD_REQUEST)
  private String cityName;

  @NotBlank(message = CommonErrorCodes.E_HTTP_BAD_REQUEST)
  private String stateName;

  @NotBlank(message = CommonErrorCodes.E_HTTP_BAD_REQUEST)
  private String country;
}
