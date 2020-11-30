/* Licensed under Apache-2.0 */
package ro.common.couchbase;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.repository.support.IndexManager;

import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;

/**
 * Couchbase configuration params
 *
 * @author r.krishnakumar
 */
@Configuration
@DependsOn("log")
public class CouchBaseConfig extends AbstractCouchbaseConfiguration {

  @Value("${ro.db.hosts}")
  private List<String> host;

  @Value("${ro.db.bucket}")
  private String bucketName;

  @Value("${ro.db.username}")
  private String userName;

  @Value("${ro.db.password}")
  private String password;

  @Override
  public List<String> getBootstrapHosts() {
    return host;
  }

  @Bean("bucketName")
  @Override
  public String getBucketName() {
    return bucketName;
  }

  @Override
  public String getUsername() {
    return userName;
  }

  @Override
  public String getBucketPassword() {
    return password;
  }

  @Override
  public IndexManager indexManager() {
    return new IndexManager(true, true, true);
  }

  /** Default properties to be configured */
  @Override
  protected CouchbaseEnvironment getEnvironment() {
    return DefaultCouchbaseEnvironment.builder().bootstrapHttpDirectPort(8091).build();
  }
}
