/* Licensed under Apache-2.0 */
package ro.common.utils;

import java.util.HashMap;
import java.util.Map;

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

  public static String dataBaseType;

  public static String persistenceType = new String();

  public static boolean propertyHasTrailingSpaces(String property) {
    Character lastCharacter = property.charAt(property.length() - 1);
    return Character.isWhitespace(lastCharacter);
  }

  public static final String CORRELATION_ID = "correlation-id";

}
