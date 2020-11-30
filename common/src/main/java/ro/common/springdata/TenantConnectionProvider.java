/* Licensed under Apache-2.0 */
package ro.common.springdata;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;

/**
 * Class that provides connection to the context
 *
 * @author krishna
 */
public class TenantConnectionProvider implements MultiTenantConnectionProvider {

  private static final long serialVersionUID = 7122273568351610417L;

  private String DEFAULT_TENANT = "public";
  private DataSource datasource;

  public TenantConnectionProvider(DataSource dataSource) {
    this.datasource = dataSource;
  }

  @Override
  public Connection getAnyConnection() throws SQLException {
    return datasource.getConnection();
  }

  @Override
  public void releaseAnyConnection(Connection connection) throws SQLException {
    connection.close();
  }

  @Override
  public Connection getConnection(String tenantIdentifier) throws SQLException {
    final Connection connection = getAnyConnection();
    connection.setSchema(tenantIdentifier);
    return connection;
  }

  @Override
  public void releaseConnection(String tenantIdentifier, Connection connection)
      throws SQLException {
    connection.setSchema(DEFAULT_TENANT);
    releaseAnyConnection(connection);
  }

  @Override
  public boolean supportsAggressiveRelease() {
    return false;
  }

  @Override
  @SuppressWarnings("rawtypes")
  public boolean isUnwrappableAs(Class aClass) {
    return false;
  }

  @Override
  public <T> T unwrap(Class<T> aClass) {
    return null;
  }
}
