/* Licensed under Apache-2.0 */
package ro.common.utils;

import org.springframework.http.HttpStatus;
import ro.common.rest.CommonErrorCodes;
import ro.common.rest.CommonErrorResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Common Utils
 *
 * @author r.krishnakumar
 */
public class Utils {

  /** Enum for Log levels */
  public enum LOG_LEVELS {
    TRACE("TRACE"),
    DEBUG("DEBUG"),
    INFO("INFO"),
    WARN("WARN"),
    ERROR("ERROR"),
    FATAL("FATAL");

    private static final Map<String, LOG_LEVELS> BY_LABEL = new HashMap<>();

    static {
      for (LOG_LEVELS e : values()) {
        BY_LABEL.put(e.label, e);
      }
    }

    private final String label;

    private LOG_LEVELS(String label) {
      this.label = label;
    }

    public static LOG_LEVELS value(String label) {
      return BY_LABEL.get(label);
    }
  }

  /** Enum for log appenders */
  public enum LOG_APPENDERS {
    CONSOLE("CONSOLE"),
    FILE("FILE");

    private static final Map<String, LOG_APPENDERS> BY_LABEL = new HashMap<>();

    static {
      for (LOG_APPENDERS e : values()) {
        BY_LABEL.put(e.label, e);
      }
    }

    private final String label;

    private LOG_APPENDERS(String label) {
      this.label = label;
    }

    public static LOG_APPENDERS value(String label) {
      return BY_LABEL.get(label);
    }
  }

  /** Enum for Log levels */
  public enum SECURITY_TYPE {
    FORM("form"),
    JWT("jwt");

    public final String type;

    private SECURITY_TYPE(String type) {
      this.type = type;
    }
  }

  public static String securityType = new String();
  public static Map<String, String> defaultTargets = new HashMap<>();

  public static final String TOKEN_PREFIX = "Bearer ";
  public static final String HEADER_STRING = "Authorization";
  public static String SECRET = new String();
  public static Long EXPIRATION_TIME = 0l;
  public static String AUTHORITIES_KEY = "Authorities";

  public static String dataBaseType;

  public static String persistenceType = new String();

  public static boolean propertyHasTrailingSpaces(String property) {
    Character lastCharacter = property.charAt(property.length() - 1);
    return Character.isWhitespace(lastCharacter);
  }

  public static final String CORRELATION_ID = "correlation-id";

  public static final Pattern errorCodePattern =
      Pattern.compile(CommonErrorCodes.ERRORCODE_PATTERN);

  public static final Pattern numberPattern = Pattern.compile("\\d+");

  /**
   * Method to generate error code
   *
   * @param errorCode
   * @return
   */
  public static String generateErrorCode(String errorCode) {
    return "ERR-RICO-" + errorCode;
  }
  /**
   * Prepare ErrorResponse object for the generated Exception.
   *
   * @param status - the HttpStatus
   * @param errorCode - the error code for the particular exception
   * @param correlationId - the correlation id
   * @return ErrorResponse - the error response
   */
  public static CommonErrorResponse prepareErrorResponse(
      final HttpStatus status, final String errorCode, final String correlationId) {
    HttpStatus resStatus = status;
    String resErrorCode = errorCode;
    if (errorCode == null
        || errorCode.isEmpty()
        || (!errorCodePattern.matcher(errorCode).matches()
            && !numberPattern.matcher(errorCode).matches())) {
      // if there error code is equal to internal error code or does  then return internal
      // server error
      resStatus = HttpStatus.INTERNAL_SERVER_ERROR;
      resErrorCode = CommonErrorCodes.E_HTTP_INTERNAL_SERVER_ERROR;
    }
    return new CommonErrorResponse(
        Integer.toString(resStatus.value()), generateErrorCode(resErrorCode), correlationId);
  }

  /**
   * Checking if its a valid ip address
   *
   * @param ip
   * @return
   */
  public static boolean isValidIPAddress(String ip)
  {

    // Regex for digit from 0 to 255.
    String zeroTo255
            = "(\\d{1,2}|(0|1)\\"
            + "d{2}|2[0-4]\\d|25[0-5])";

    // Regex for a digit from 0 to 255 and
    // followed by a dot, repeat 4 times.
    // this is the regex to validate an IP address.
    String regex
            = zeroTo255 + "\\."
            + zeroTo255 + "\\."
            + zeroTo255 + "\\."
            + zeroTo255;

    // Compile the ReGex
    Pattern p = Pattern.compile(regex);

    // If the IP address is empty
    // return false
    if (ip == null) {
      return false;
    }

    // Pattern class contains matcher() method
    // to find matching between given IP address
    // and regular expression.
    Matcher m = p.matcher(ip);

    // Return if the IP address
    // matched the ReGex
    return m.matches();
  }
}
