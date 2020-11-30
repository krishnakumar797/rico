/* Licensed under Apache-2.0 */
package ro.common.exception;

/**
 * Custom exception for spring data init
 *
 * @author r.krishnakumar
 */
public class SpringDataInitException extends RuntimeException {

  public SpringDataInitException(String message) {
    super(message);
  }

  public SpringDataInitException(String message, Throwable e) {
    super(message, e);
  }
}
