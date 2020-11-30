/* Licensed under Apache-2.0 */
package ro.common.exception;

/**
 * Custom exception for data parsing issues
 *
 * @author r.krishnakumar
 */
public class DataParsingException extends Exception {

  public DataParsingException(String message) {
    super(message);
  }

  public DataParsingException(String message, Throwable e) {
    super(message, e);
  }
}
