package ro.common.grpc;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import net.devh.boot.grpc.server.config.GrpcServerProperties;
import net.devh.boot.grpc.server.serverfactory.GrpcServerConfigurer;

/**
 * Configuration class only for GRPC Server
 * 
 * @author r.krishnakumar
 *
 */
@Configuration
public class GrpcServerConfig extends GrpcServerProperties {

	@Value("${ro.grpc.server.port}")
	private int port;

	/**
	 * Grpc Server configuration bean
	 * 
	 * @return
	 */
	@Bean
	public GrpcServerConfigurer keepAliveServerConfigurer() {
		setPort(port);
		return serverBuilder -> {
			if (serverBuilder instanceof NettyServerBuilder) {
				((NettyServerBuilder) serverBuilder).keepAliveTime(30, TimeUnit.SECONDS)
						.keepAliveTimeout(5, TimeUnit.SECONDS).permitKeepAliveWithoutCalls(true);
			}
		};
	}

}
