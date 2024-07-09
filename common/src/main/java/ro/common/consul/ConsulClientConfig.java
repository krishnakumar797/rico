package ro.common.consul;

import com.ecwid.consul.v1.ConsulClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.commons.util.InetUtilsProperties;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDiscoveryClient
public class ConsulClientConfig {

    @Value("${ro.discovery.server.host}")
    private String discoveryServerHost;

    @Value("${ro.discovery.server.port}")
    private int discoveryServerPort;

    @Value("${ro.discovery.server.register}")
    private boolean discoveryServerRegister;

    @Value("${ro.discovery.service.name}")
    private String discoveryServiceName;

    @Value("${ro.discovery.server.preferIPAddress:false}")
    private boolean discoveryServerPreferIPAddress;

    @Bean
    public ConsulClient consulClient() {
        return new ConsulClient(discoveryServerHost, discoveryServerPort);
    }

    @Bean
    public ConsulDiscoveryProperties consulDiscoveryProperties() {
        ConsulDiscoveryProperties properties = new ConsulDiscoveryProperties(new InetUtils(new InetUtilsProperties()));
        properties.setPreferIpAddress(discoveryServerPreferIPAddress);
        properties.setHealthCheckInterval("10s");
        properties.setHealthCheckPath("/actuator/health");
        properties.setHealthCheckCriticalTimeout("30s");
        properties.setFailFast(true);
        properties.setServiceName(discoveryServiceName);
        properties.setInstanceId(discoveryServiceName);
        properties.setRegister(discoveryServerRegister);
        return properties;
    }
}

