/* Licensed under Apache-2.0 */
package com.rico.hazelcast;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ro.common.config.CommonConfig;
import ro.common.hazelcast.HazelcastConfig;
import ro.common.hibernate.HibernateConfig;
import ro.common.logging.Log4j2Config;

/** Configuration manager to add configurations for external systems */
@Configuration
@Import({CommonConfig.class, Log4j2Config.class, HibernateConfig.class, HazelcastConfig.class})
public class AppConfig {}
