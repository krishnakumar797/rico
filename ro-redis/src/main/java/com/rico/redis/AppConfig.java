/* Licensed under Apache-2.0 */
package com.rico.redis;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ro.common.config.CommonConfig;
import ro.common.logging.Log4j2Config;
import ro.common.redis.RedisConfig;

/**
 * Configuration manager to add configurations for external systems. Multiple configuration class
 * can be imported as comma separated values
 *
 * @author r.krishnakumar
 */
@Configuration
@Import({CommonConfig.class, Log4j2Config.class, RedisConfig.class})
public class AppConfig {}
