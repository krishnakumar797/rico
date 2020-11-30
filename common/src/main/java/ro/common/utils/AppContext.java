/* Licensed under Apache-2.0 */
package ro.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Tenant Context class for multi tenency support
 *
 * @author krishna
 */
public class AppContext {

  private static String tenantKey = "tenant";

  private static String userIdKey = "userId";

  private static ThreadLocal<Map<String, String>> currentThreadLocal =
      InheritableThreadLocal.withInitial(
          () -> {
            Map<String, String> map = new HashMap<>();
            return map;
          });

  public static String getCurrentTenant() {
    return currentThreadLocal.get().get(tenantKey);
  }

  public static void setCurrentTenant(String tenant) {
    currentThreadLocal.get().put(tenantKey, tenant);
  }

  public static String getCurrentUserId() {
    return currentThreadLocal.get().get(userIdKey);
  }

  public static void setCurrentUserId(String userId) {
    currentThreadLocal.get().put(userIdKey, userId);
  }

  public static void clear() {
    currentThreadLocal.get().clear();
  }
}
