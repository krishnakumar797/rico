/* Licensed under Apache-2.0 */
package ro.common.hazelcast;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;

/**
 * Default configuration class for Hazelcast
 *
 * @author r.krishnakumar
 */
@Configuration
@DependsOn("log")
@EnableCaching
public class HazelcastConfig {

  @Value("#{${ro.cache.hosts}}")
  private Map<String, Integer> hosts;

  @Value("${ro.cache.cluster.name}")
  private String clusterName;

  // for predefined cachenames
  @Value("#{${ro.cache.names:{null}}")
  private Optional<Map<String, Integer>> cacheNames;

  /**
   * Hazelcast config
   *
   * @return
   */
  @Bean(destroyMethod = "shutdown")
  public HazelcastInstance hazelcastInstance() {
    ClientConfig config = new ClientConfig();
    config.setInstanceName(clusterName);
    hosts
        .entrySet()
        .forEach(
            entry ->
                config
                    .getNetworkConfig()
                    .addAddress(entry.getKey() + ":" + Integer.valueOf(entry.getValue())));
    return HazelcastClient.newHazelcastClient(config);
  }

  /**
   * Hazelcast cache manager
   *
   * @param hazelcastInstance
   * @return
   */
  @Bean
  public HazelcastCacheManager cacheManager(HazelcastInstance hazelcastInstance) {
    return new HazelcastCacheManager(hazelcastInstance);
  }

  /**
   * Cache customizer
   *
   * @return
   */
  @Bean
  public CacheManagerCustomizer<HazelcastCacheManager> cacheManagerCustomizer() {
    return new CacheManagerCustomizer<HazelcastCacheManager>() {
      @Override
      public void customize(HazelcastCacheManager cacheManager) {
        StringBuffer sb = new StringBuffer();
        cacheNames.ifPresent(
            cache -> {
              cache
                  .entrySet()
                  .forEach(entry -> sb.append(entry.getKey() + "=" + entry.getValue() + ","));
            });
      }
    };
  }
}
