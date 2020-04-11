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
  public static enum LOG_LEVELS {
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
  public static enum LOG_APPENDERS {
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
}
