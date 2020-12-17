/* Licensed under Apache-2.0 */
package ro.common.rest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ro.common.springdata.AppContextInterceptor;

/**
 * Default configuration class for Rest
 *
 * @author r.krishnakumar
 */
@Configuration
@EnableWebMvc
@DependsOn({"log"})
public class RestConfig
    implements WebServerFactoryCustomizer<ConfigurableWebServerFactory>, WebMvcConfigurer {

  @Value("${ro.server.port: 8080}")
  private Integer port;

  @Autowired
  @Qualifier("multitenancy")
  private boolean isMultitenant;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new AppContextInterceptor(isMultitenant));
  }

  /**
   * Customizing server port using the property file, This can be overridden using the command line
   * property -Dserver.port=8083
   */
  @Override
  public void customize(ConfigurableWebServerFactory factory) {
    factory.setPort(port);
  }
  /**
   * Initiating common controller advice
   *
   * @return
   */
  @Bean
  public CommonControllerAdvice commonControllerAdvice() {
    return new CommonControllerAdvice();
  }

  /**
   * Model mapper to transform dto models to entity models
   *
   * @return
   */
  @Bean
  public CommonDTOConverter commonDtoConverter() {
    return new CommonDTOConverter(new ModelMapper());
  }

  /**
   * Custom http message converter
   *
   * @return
   */
  @Bean
  public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
    MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    jsonConverter.setObjectMapper(objectMapper);
    return jsonConverter;
  }
}
