/* Licensed under Apache-2.0 */
package com.rico.springdata;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ro.common.config.CommonConfig;
import ro.common.logging.Log4j2Config;
import ro.common.springdata.SpringDataConfig;

/** Configuration manager to add configurations for external systems */
@Configuration
@Import({CommonConfig.class, Log4j2Config.class, SpringDataConfig.class})
public class AppConfig {}
