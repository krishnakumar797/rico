package ro.common.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import ro.common.utils.Utils;

/**
 * Common Rest Exception
 *
 * @author krishna
 */
public class CommonRestException extends Exception {
  private final String code;
  private final String correlationId;
  private final HttpStatus status;

  public CommonRestException(
      String code,
      HttpHeaders headers,
      HttpStatus status,
      String errorMessage,
      Throwable throwable) {
    super(errorMessage, throwable);
    this.code = code;
    this.correlationId = headers.getFirst(Utils.CORRELATION_ID);
    this.status = status;
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
