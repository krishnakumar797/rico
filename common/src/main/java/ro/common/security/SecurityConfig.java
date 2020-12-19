/* Licensed under Apache-2.0 */
package ro.common.security;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ro.common.utils.Utils;

import javax.annotation.PostConstruct;

/**
 * Default configuration class for Security
 *
 * @author r.krishnakumar
 */
@Configuration
@DependsOn({"log", "security"})
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@AutoConfigurationPackage
@ComponentScan
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired MyCustomLoginSuccessHandler successHandler;

  @Autowired MyCustomLogoutSuccessHandler logOutHandler;

  @Autowired RestAuthenticationEntryPoint restAuthenticationEntryPoint;

  @Autowired RestAccessDeniedHandler restAccessDeniedHandler;

  @Autowired UserLoginService userLoginService;

  @Value("#{${ro.security.rules}}")
  private Map<String, String> rules;

  @Value("#{${ro.security.defaultTargets: {ADMIN: '/welcome'}}}")
  private Map<String, String> defaultTargets;

  @Value("${ro.security.loginPage:DEFAULT}")
  private String loginPage;

  @Value("${ro.security.loginProcessingUrl:DEFAULT}")
  private String loginProcessingUrl;

  @Value("${ro.security.username.param:DEFAULT}")
  private String usernameParam;

  @Value("${ro.security.password.param:DEFAULT}")
  private String passwordParam;

  @Value("${ro.security.failureUrl:DEFAULT}")
  private String failureUrl;

  @Value("${ro.security.logoutUrl:DEFAULT}")
  private String logoutUrl;

  @Value("${ro.security.jwt.secret:DEFAULT}")
  private String jwtSecret;

  @Value("${ro.security.jwt.tokenExpiryInMin:0}")
  private Integer tokenExpiryInMin;

  @PostConstruct
  public void init() {
    Utils.SECRET = jwtSecret;
    Utils.EXPIRATION_TIME = Long.valueOf(tokenExpiryInMin * 60 * 1000);
    this.defaultTargets.forEach(
        (key, value) -> {
          Utils.defaultTargets.put("ROLE_" + key, value);
        });
  }

  /**
   * Configuring security for urls, ROLE name should be prefixed with ROLE_ in the database column,
   * but for checking roles we need only actual name without the prefix. It will be automatically
   * added by spring security.
   */
  // @formatter:off
  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.csrf().disable();
    http.cors();
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
    // Checking the security type
    if (Utils.securityType.contentEquals(Utils.SECURITY_TYPE.FORM.type)) {
      http.headers().frameOptions().sameOrigin();
      permissions
          .and()
          .formLogin()
          .loginPage(loginPage)
          .loginProcessingUrl(loginProcessingUrl)
          .usernameParameter(usernameParam)
          .passwordParameter(passwordParam)
          .successHandler(successHandler)
          .failureUrl(failureUrl)
          .and()
          .logout()
          .logoutUrl(logoutUrl)
          .logoutSuccessHandler(logOutHandler);
    } else if (Utils.securityType.contentEquals(Utils.SECURITY_TYPE.JWT.type)) {
      permissions
          .and()
          .exceptionHandling()
          .authenticationEntryPoint(restAuthenticationEntryPoint)
          .and()
          .exceptionHandling()
          .accessDeniedHandler(restAccessDeniedHandler)
          .and()
          .addFilter(
              new JwtAuthenticationFilter(
                  authenticationManager(), loginProcessingUrl, usernameParam, passwordParam))
          .addFilter(new JwtAuthorizationFilter(authenticationManager()))
          .sessionManagement()
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
  }

  /**
   * Bcrypt Password encoder bean
   *
   * @return
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Cors configuration. By default all enabled
   *
   * @return
   */
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
    return source;
  }

  /**
   * Configuring DAOAuthentication provider
   *
   * @param auth
   * @throws Exception
   */
  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(authProvider());
  }

  /**
   * DAO Authentication provider creation using Userloginservice and Passwordencoder.
   *
   * @return
   */
  private DaoAuthenticationProvider authProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userLoginService);
    authProvider.setPasswordEncoder(passwordEncoder());
    authProvider.setHideUserNotFoundExceptions(false);
    return authProvider;
  }
}
