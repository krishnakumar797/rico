package ro.common.rest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Common Error response for the rest api
 *
 * @author krishna
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonErrorResponse implements CorrelationId {

  @NotBlank private String status;

  @NotNull
  @Pattern(regexp = CommonErrorCodes.ERRORCODE_PATTERN)
  private String errorCode;

  @NotBlank private String correlationId;
}
