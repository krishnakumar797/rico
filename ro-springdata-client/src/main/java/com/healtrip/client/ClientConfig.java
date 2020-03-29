/* Licensed under Apache-2.0 */
package com.healtrip.client;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ro.common.elasticsearch.ElasticSearchConfig;
import ro.common.grpc.GrpcClientConfig;

/** Configuration manager to add configurations for external systems */
@Configuration
@Import({GrpcClientConfig.class, ElasticSearchConfig.class})
public class ClientConfig {}
