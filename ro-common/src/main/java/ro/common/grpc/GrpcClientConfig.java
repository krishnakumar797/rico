package ro.common.grpc;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import net.devh.boot.grpc.client.channelfactory.GrpcChannelConfigurer;
import net.devh.boot.grpc.client.config.GrpcChannelProperties;
import net.devh.boot.grpc.client.config.GrpcChannelsProperties;
import net.devh.boot.grpc.client.config.NegotiationType;

/**
 * Configuration class only for GRPC Client
 * 
 * @author r.krishnakumar
 *
 */
@Configuration
public class GrpcClientConfig extends GrpcChannelsProperties {

	@Value("#{${ro.grpc.client.targets}}")
	private Map<String, String> targets;

	@Value("${ro.grpc.client.target.serviceType:static}")
	private String serviceType;

	/**
	 * Grpc client channel configuration
	 * 
	 * @return
	 */
	@Bean
	public GrpcChannelConfigurer keepAliveClientConfigurer() {
		targets.forEach((targetName, targetAddress) -> {
			GrpcChannelProperties properties = new GrpcChannelProperties();
			properties.setAddress(serviceType + "://" + targetAddress);
			properties.setNegotiationType(NegotiationType.PLAINTEXT);
			getClient().put(targetName, properties);
		});
		return (channelBuilder, name) -> {
			if (channelBuilder instanceof NettyChannelBuilder) {
				((NettyChannelBuilder) channelBuilder).keepAliveTime(30, TimeUnit.SECONDS).keepAliveTimeout(5,
						TimeUnit.SECONDS);
			}
		};
	}
}
