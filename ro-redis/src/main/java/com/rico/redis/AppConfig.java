package com.rico.redis;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ro.common.redis.RedisConfig;

/**
 * Configuration manager to add configurations for external systems. Multiple
 * configuration class can be imported as comma separated values
 * 
 * @author r.krishnakumar
 *
 */
@Configuration
@Import({ RedisConfig.class })
public class AppConfig {

}
