/* Licensed under Apache-2.0 */
package ro.common.utils;

/**
 * Tenant Context class for multi tenency support
 *
 * @author krishna
 */
public class TenantContext {
  private static ThreadLocal<String> currentTenant = new InheritableThreadLocal<>();

  public static String getCurrentTenant() {
    return currentTenant.get();
  }

  public static void setCurrentTenant(String tenant) {
    currentTenant.set(tenant);
  }

  public static void clear() {
    currentTenant.set(null);
  }
}
