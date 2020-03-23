package com.rico.grpc.server;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ro.common.grpc.GrpcServerConfig;

/**
 * Configuration manager to add configurations for external systems. Multiple
 * configuration class can be imported as comma separated values
 * 
 * @author r.krishnakumar
 *
 */
@Configuration
@Import({ GrpcServerConfig.class })
public class AppConfig {

}
