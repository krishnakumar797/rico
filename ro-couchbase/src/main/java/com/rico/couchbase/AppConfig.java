package com.rico.couchbase;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ro.common.couchbase.CouchBaseConfig;

/**
 * Configuration manager to add configurations for external systems. Multiple
 * configuration class can be imported as comma separated values
 * 
 * @author r.krishnakumar
 *
 */
@Configuration
@Import({ CouchBaseConfig.class })
public class AppConfig {

}
