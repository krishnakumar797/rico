package com.rico.kafka;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ro.common.kafka.KafkaConfig;

/**
 * Configuration manager to add configurations for external systems. Multiple
 * configuration class can be imported as comma separated values
 * 
 * @author r.krishnakumar
 *
 */
@Configuration
@Import({ KafkaConfig.class })
public class AppConfig {

}
