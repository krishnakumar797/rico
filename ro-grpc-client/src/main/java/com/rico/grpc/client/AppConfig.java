/* Licensed under Apache-2.0 */
package com.rico.grpc.client;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ro.common.grpc.GrpcClientConfig;

/**
 * Configuration manager to add configurations for external systems. Multiple configuration class
 * can be imported as comma separated values
 *
 * @author r.krishnakumar
 */
@Configuration
@Import({GrpcClientConfig.class})
public class AppConfig {}
