/* Licensed under Apache-2.0 */
package ro.common.uaa;

import static org.springframework.security.oauth2.jose.jws.SignatureAlgorithm.*;

import java.time.Duration;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.web.client.RestOperations;

/**
 * UAA resource server configuration params
 *
 * @author r.krishnakumar
 */
@Configuration
@DependsOn({"log", "security"})
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class UAAResourceServerConfig extends WebSecurityConfigurerAdapter {

  @Value("#{${ro.security.rules}}")
  private Map<String, String> rules;

  @Autowired private JwtDecoder jwtDecoder;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable();
    ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry permissions =
        http.authorizeRequests();
    for (Map.Entry<String, String> entry : rules.entrySet()) {
      if (entry.getValue().contains(",")) {
        String[] multiRule = entry.getValue().split(",");
        permissions = permissions.antMatchers(entry.getKey()).hasAnyRole(multiRule);
        continue;
      }
      permissions = permissions.antMatchers(entry.getKey()).hasRole(entry.getValue().trim());
    }
    permissions = permissions.antMatchers("/**").permitAll();
    http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(jwtDecoder)));
  }

  /**
   * Custom jwt decoder bean
   *
   * @param builder
   * @return
   */
  @Bean
  public JwtDecoder jwtDecoder(
      RestTemplateBuilder builder, @Value("${ro.uaa.host.url:DEFAULT}") String hostUrl) {
    RestOperations rest =
        builder
            .setConnectTimeout(Duration.ofSeconds(60))
            .setReadTimeout(Duration.ofSeconds(60))
            .build();
    NimbusJwtDecoder jwtDecoder =
        NimbusJwtDecoder.withJwkSetUri(hostUrl + "/token_keys")
            .jwsAlgorithms(
                algorithms -> {
                  algorithms.add(RS256);
                  algorithms.add(RS512);
                  algorithms.add(ES512);
                })
            .restOperations(rest)
            .build();
    return jwtDecoder;
  }

  /**
   * Custom Jwt Authentication converter
   *
   * @return
   */
  @Bean
  public JwtAuthenticationConverter jwtAuthenticationConverter() {
    JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter =
        new JwtGrantedAuthoritiesConverter();
    grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
    grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
    return jwtAuthenticationConverter;
  }
}
