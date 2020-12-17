/* Licensed under Apache-2.0 */
package ro.common.springdata;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import ro.common.utils.AppContext;

/**
 * Class to configure the tenant schema name
 *
 * @author krishna
 */
public class TenantSchemaResolver implements CurrentTenantIdentifierResolver {

  private String defaultTenant = "public";

  @Override
  public String resolveCurrentTenantIdentifier() {
    String t = AppContext.getCurrentTenant();
    if (t != null) {
      return t;
    } else {
      return defaultTenant;
    }
  }

  @Override
  public boolean validateExistingCurrentSessions() {
    return true;
  }
}
