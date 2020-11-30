/* Licensed under Apache-2.0 */
package ro.common.exception;

import lombok.Getter;
import ro.common.utils.HttpStatusCode;

/**
 * Custom exceptions for services
 *
 * @author r.krishnakumar
 */
@Getter
public class GenericServiceException extends Exception {

  private final String code;

  private final HttpStatusCode status;

  public GenericServiceException(String message, String code, HttpStatusCode status) {
    super(message);
    this.code = code;
    this.status = status;
  }

  public GenericServiceException(String message, String code, HttpStatusCode status, Throwable e) {
    super(message, e);
    this.code = code;
    this.status = status;
  }
}
