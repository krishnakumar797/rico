package ro.common.exception;

/**
 * Custom exceptions for services
 *
 * @author r.krishnakumar
 */
public class GenericServiceException extends Exception {

  public GenericServiceException(String message) {
    super(message);
  }

  public GenericServiceException(String message, Throwable e) {
    super(message, e);
  }
}
