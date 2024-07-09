/* Licensed under Apache-2.0 */
package ro.common.grpc;

import com.ecwid.consul.v1.health.model.Check;
import io.grpc.ClientInterceptor;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import net.devh.boot.grpc.client.channelfactory.GrpcChannelConfigurer;
import net.devh.boot.grpc.client.config.GrpcChannelProperties;
import net.devh.boot.grpc.client.config.GrpcChannelsProperties;
import net.devh.boot.grpc.client.config.NegotiationType;
import net.devh.boot.grpc.client.interceptor.GrpcGlobalClientInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.consul.discovery.ConsulServiceInstance;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.Async;

/**
 * Configuration class only for GRPC Client
 *
 * @author r.krishnakumar
 */
@EqualsAndHashCode(callSuper = true)
@Configuration
@Log4j2
@DependsOn("log")
public class GrpcClientConfig extends GrpcChannelsProperties {

    @Value("#{${ro.grpc.client.targets}}")
    private Map<String, String> targets;

    @Value("${ro.grpc.client.target.serviceType:static}")
    private String serviceType;

    @Autowired
    private ApplicationContext context;

    private boolean isGrpcInitialized;

    /**
     * Grpc client channel configuration
     *
     * @return
     */
    @Bean
    public GrpcChannelConfigurer keepAliveClientConfigurer() {
        targets.forEach(
                (targetName, targetAddress) -> {
                    GrpcChannelProperties properties = new GrpcChannelProperties();
                    if (serviceType.contentEquals("static")) {
                        properties.setAddress(serviceType + "://" + targetAddress);
                    } else if (serviceType.contentEquals("discovery")) {
                        String[] targAddressArray = targetAddress.split(":");
                        String host = targAddressArray[0];
//                        String port = targAddressArray[1];
//                        DiscoveryClient discoveryClient = context.getBean(DiscoveryClient.class);
//                        List<ServiceInstance> instances = discoveryClient.getInstances(host);
//                        ConsulServiceInstance serviceInstance =  findHealthy(instances, host);
//                        if(serviceInstance == null) {
//                            log.error("No healthy instance found for " + host);
//                            return;
//                        }
                        //properties.setAddress("static://" + serviceInstance.getHealthService().getService().getAddress() + ":" + port);
                        properties.setAddress(serviceType +":///" + host);
                    } else {
                        properties.setAddress(serviceType + "://" + targetAddress);
                    }
                    properties.setNegotiationType(NegotiationType.PLAINTEXT);
                    getClient().put(targetName, properties);
                });
        return (channelBuilder, name) -> {
            if (channelBuilder instanceof NettyChannelBuilder) {
                ((NettyChannelBuilder) channelBuilder)
                        .enableRetry()
                        .maxRetryAttempts(5)
                        .keepAliveTime(30, TimeUnit.SECONDS)
                        .keepAliveTimeout(5, TimeUnit.SECONDS);
            }
        };
    }

    @Async
    private void retryingEventListener(GrpcChannelConfigurer configurer) {

    }

    /**
     * Grpc Client interceptor
     *
     * @return
     */
    @GrpcGlobalClientInterceptor
    public ClientInterceptor logClientInterceptor() {
        return new GrpcClientInterceptor();
    }


    private ConsulServiceInstance findHealthy(List<ServiceInstance> instances, String applicationName) {
        for (ServiceInstance instance : instances) {
            ConsulServiceInstance consulServiceInstance = (ConsulServiceInstance) instance;
            List<Check> healthStatus = consulServiceInstance.getHealthService().getChecks();
            for (Check check : healthStatus) {
                if (!check.getServiceName().equalsIgnoreCase(applicationName)) {
                    continue;
                }
                if (Check.CheckStatus.PASSING.equals(check.getStatus())) {
                    return consulServiceInstance;
                }
            }
        }
        return null;
    }
}
