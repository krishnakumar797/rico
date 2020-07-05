/* Licensed under Apache-2.0 */
package ro.common.redis;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import io.lettuce.core.ReadFrom;

/**
 * Default configuration class for Redis
 *
 * @author r.krishnakumar
 */
@Configuration
@DependsOn("log")
@EnableAutoConfiguration(
    exclude = {RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class})
public class RedisConfig {

  @Value("#{${ro.keyvaluestore.hosts}}")
  private Map<String, Integer> hosts;

  @Value("${ro.keyvaluestore.standalone}")
  private boolean standalone;

  @Value("${ro.keyvaluestore.master: 'mymaster'}")
  private String master;

  @Value("${ro.keyvaluestore.password: ''}")
  private String password;

  /**
   * Redis lettuceconnection factory creation
   *
   * @return
   */
  @Bean
  public LettuceConnectionFactory redisConnectionFactory() {
    LettuceClientConfiguration clientConfig =
        LettuceClientConfiguration.builder().readFrom(ReadFrom.REPLICA_PREFERRED).build();
    if (hosts.isEmpty()) {
      throw new RuntimeException("Invalid host config for KeyValueStore");
    }
    if (standalone) {
      if (hosts.size() != 1) {
        throw new RuntimeException("Invalid host config for KeyValueStore standalone server");
      }
      RedisStandaloneConfiguration serverConfig =
          new RedisStandaloneConfiguration(hosts.keySet().iterator().next());
      serverConfig.setPort(hosts.values().iterator().next());
      if (!password.isEmpty()) {
        serverConfig.setPassword(password);
      }
      return new LettuceConnectionFactory(serverConfig, clientConfig);
    }
    // Redis sentinal config
    RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration().master(master);
    hosts
        .entrySet()
        .forEach(
            entry -> sentinelConfig.sentinel(entry.getKey(), Integer.valueOf(entry.getValue())));
    if (!password.isEmpty()) {
      sentinelConfig.setPassword(password);
    }
    return new LettuceConnectionFactory(sentinelConfig, clientConfig);
  }

  /**
   * Create redis template
   *
   * @param connectionFactory
   * @return
   */
  @Bean
  public RedisTemplate<byte[], byte[]> redisTemplate(RedisConnectionFactory connectionFactory) {
    RedisTemplate<byte[], byte[]> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory);
    return template;
  }
}
