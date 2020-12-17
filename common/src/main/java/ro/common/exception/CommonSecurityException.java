/* Licensed under Apache-2.0 */
package ro.common.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import ro.common.utils.Utils;

/**
 * Common Security Exception
 *
 * @author krishna
 */
public class CommonSecurityException extends Exception {
  private final String code;
  private final String correlationId;
  private final HttpStatus status;

  public CommonSecurityException(
      String code,
      HttpHeaders headers,
      ro.common.utils.HttpStatusCode status,
      String errorMessage,
      Throwable throwable) {
    super(errorMessage, throwable);
    this.code = code;
    this.correlationId = null != headers ? headers.getFirst(Utils.CORRELATION_ID) : null;
    this.status = HttpStatus.valueOf(status.value());
  }

  public CommonSecurityException(
      String code,
      String correlationId,
      ro.common.utils.HttpStatusCode status,
      String errorMessage,
      Throwable throwable) {
    super(errorMessage, throwable);
    this.code = code;
    this.correlationId = correlationId;
    this.status = HttpStatus.valueOf(status.value());
  }

  public String getCode() {
    return code;
  }

  public String getCorrelationId() {
    return correlationId;
  }

  public HttpStatus getStatus() {
    return status;
  }
}
