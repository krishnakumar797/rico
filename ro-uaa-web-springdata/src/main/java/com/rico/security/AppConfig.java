/* Licensed under Apache-2.0 */
package com.rico.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ro.common.config.CommonConfig;
import ro.common.logging.Log4j2Config;
import ro.common.rest.RestConfig;
import ro.common.springdata.SpringDataConfig;
import ro.common.uaa.UAAClientConfig;
import ro.common.web.WebConfig;

/** Configuration manager to add configurations for external systems */
@Configuration
@Import({
  CommonConfig.class,
  Log4j2Config.class,
  UAAClientConfig.class,
  RestConfig.class,
  SpringDataConfig.class,
  WebConfig.class
})
public class AppConfig {}
