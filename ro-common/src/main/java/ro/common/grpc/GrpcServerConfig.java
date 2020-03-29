/* Licensed under Apache-2.0 */
package ro.common.grpc;

import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import java.util.concurrent.TimeUnit;
import lombok.EqualsAndHashCode;
import net.devh.boot.grpc.server.config.GrpcServerProperties;
import net.devh.boot.grpc.server.serverfactory.GrpcServerConfigurer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class only for GRPC Server
 *
 * @author r.krishnakumar
 */
@Configuration
@EqualsAndHashCode(callSuper = true)
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
        ((NettyServerBuilder) serverBuilder)
            .keepAliveTime(30, TimeUnit.SECONDS)
            .keepAliveTimeout(5, TimeUnit.SECONDS)
            .permitKeepAliveWithoutCalls(true);
      }
    };
  }
}
