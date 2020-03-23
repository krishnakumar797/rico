package ro.common.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import io.lettuce.core.ReadFrom;

/**
 * Default configuration class for Redis
 * 
 * @author r.krishnakumar
 *
 */
@Configuration
public class RedisConfig {

	@Value("${ro.cache.host}")
	private String hostName;

	@Value("${ro.cache.port: 6379}")
	private Integer port;

	@Value("${ro.cache.password: ''}")
	private String password;

	/**
	 * Redis lettuceconnection factory creation
	 * 
	 * @return
	 */
	@Bean
	public LettuceConnectionFactory redisConnectionFactory() {
		LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
				.readFrom(ReadFrom.REPLICA_PREFERRED).build();
		RedisStandaloneConfiguration serverConfig = new RedisStandaloneConfiguration(hostName);
		serverConfig.setPort(port);
		if (!password.isEmpty()) {
			serverConfig.setPassword(password);
		}
		return new LettuceConnectionFactory(serverConfig, clientConfig);
	}

	/**
	 * Create redis template
	 * 
	 * @param connectionFactory
	 * @return
	 */
	@Bean
	public RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<byte[], byte[]> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		return template;
	}

}
