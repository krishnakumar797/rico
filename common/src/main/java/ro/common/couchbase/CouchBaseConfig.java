/* Licensed under Apache-2.0 */
package ro.common.couchbase;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Couchbase configuration params
 *
 * @author r.krishnakumar
 */
@Configuration
@DependsOn("log")
public class CouchBaseConfig extends AbstractCouchbaseConfiguration {

  @Value("${ro.db.hosts}")
  private List<String> hosts;

  @Value("${ro.db.bucket}")
  private String bucketName;

  @Value("${ro.db.username}")
  private String userName;

  @Value("${ro.db.password}")
  private String password;

  @Override
  public String getConnectionString() {
    List<String> hostArray = hosts.stream().map(hostName -> "couchbase://"+hostName).collect(Collectors.toList());
    String.join(",",hostArray.toArray(String[]::new));
    return null;
  }

  @Override
  public String getUserName() {
    return userName;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Bean("bucketName")
  @Override
  public String getBucketName() {
    return bucketName;
  }

}
