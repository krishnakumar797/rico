package ro.common.uaa;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.*;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.web.client.RestTemplate;
import ro.common.utils.Utils;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * UAA configuration params
 *
 * @author r.krishnakumar
 */
@Configuration
@DependsOn({"log", "security"})
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class UAAClientConfig extends WebSecurityConfigurerAdapter {

  @Value("#{${ro.security.rules}}")
  private Map<String, String> rules;

  @Value("#{${ro.security.defaultTargets: {ADMIN: '/welcome'}}}")
  private Map<String, String> defaultTargets;

  @Value("${ro.security.loginPage:DEFAULT}")
  private String loginPage;

  @Autowired private ClientRegistrationRepository registrationRepository;

  private String redirectUri;

  @PostConstruct
  public void init() {
    this.defaultTargets.forEach(
        (key, value) -> {
          Utils.defaultTargets.put("ROLE_" + key, value);
        });
  }

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
    permissions
        .and()
        .logout()
        .logoutUrl("/logout")
        .logoutSuccessHandler(new OAuthLogoutSuccessHandler())
        .and()
        .oauth2Client(
            oauth2 ->
                oauth2
                    .clientRegistrationRepository(registrationRepository)
                    .authorizedClientRepository(
                        this.oAuth2AuthorizedClientRepository(registrationRepository)))
        .oauth2Login()
        .loginPage(loginPage)
        .authorizationEndpoint()
        .authorizationRequestResolver(
            new CustomAuthorizationRequestResolver(
                registrationRepository, "/oauth2/authorize-client/"))
        .baseUri("/oauth2/authorize-client/")
        .authorizationRequestRepository(this.httpSessionAuthorizationRequestRepository())
        .and()
        .tokenEndpoint()
        .accessTokenResponseClient(accessTokenResponseClient())
        .and()
        .userInfoEndpoint()
        .userAuthoritiesMapper(this.userAuthoritiesMapper())
        .and()
        .redirectionEndpoint()
        .baseUri(redirectUri)
        .and()
        .successHandler(new OAuthCustomLoginSuccessHandler())
        .failureUrl("/loginFailure");
    // http.oauth2ResourceServer().jwt();
  }

  /**
   * For http session OAuth2AuthorizationRequestRepository
   *
   * @return
   */
  @Bean
  public AuthorizationRequestRepository<OAuth2AuthorizationRequest>
      httpSessionAuthorizationRequestRepository() {
    return new HttpSessionOAuth2AuthorizationRequestRepository();
  }

  /**
   * oAuth2 client repository to store the client registration details in memory
   *
   * @return
   */
  @Bean
  public OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository(
      ClientRegistrationRepository clientRegistrationRepository) {
    return new AuthenticatedPrincipalOAuth2AuthorizedClientRepository(
        new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository));
  }

  @Bean
  public UAARepository uaaRepository(
      OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository,
      ClientRegistrationRepository clientRegistrationRepository) {
    return new UAARepository(oAuth2AuthorizedClientRepository, clientRegistrationRepository);
  }

  /**
   * Registering UAA client credentials
   *
   * @return
   */
  @Bean
  public ClientRegistrationRepository clientRegistrationRepository(
      @Value("${ro.uaa.client.registrationId:DEFAULT}") String registrationId,
      @Value("${ro.uaa.client.name:DEFAULT}") String clientName,
      @Value("${ro.uaa.client.id:DEFAULT}") String clientId,
      @Value("${ro.uaa.client.secret:DEFAULT}") String clientSecret,
      @Value("${ro.uaa.redirect.uri:DEFAULT}") String redirectUri,
      @Value("#{'${ro.uaa.client.scopes}'.split(',')}") String[] clientScopes,
      @Value("${ro.uaa.host.url:DEFAULT}") String hostUrl) {
    this.redirectUri = redirectUri;
    ClientRegistration uaaClientRegistration =
        ClientRegistration.withRegistrationId(registrationId)
            .clientId(clientId)
            .clientSecret(clientSecret)
            .clientAuthenticationMethod(ClientAuthenticationMethod.POST)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .redirectUriTemplate("{baseUrl}" + redirectUri)
            .scope(clientScopes)
            .authorizationUri(hostUrl + "/oauth/authorize")
            .tokenUri(hostUrl + "/oauth/token")
            .userNameAttributeName(IdTokenClaimNames.SUB)
            .clientName(clientName)
            .jwkSetUri(hostUrl + "/token_keys")
            .userInfoUri(hostUrl + "/userinfo")
            .build();
    return new InMemoryClientRegistrationRepository(uaaClientRegistration);
  }

  /**
   * Custom bean for access token request entity and response mapping
   *
   * @return
   */
  @Bean
  public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest>
      accessTokenResponseClient() {
    DefaultAuthorizationCodeTokenResponseClient accessTokenResponseClient =
        new DefaultAuthorizationCodeTokenResponseClient();
    accessTokenResponseClient.setRequestEntityConverter(new CustomRequestEntityConverter());

    OAuth2AccessTokenResponseHttpMessageConverter tokenResponseHttpMessageConverter =
        new OAuth2AccessTokenResponseHttpMessageConverter();
    tokenResponseHttpMessageConverter.setTokenResponseConverter(new CustomTokenResponseConverter());
    RestTemplate restTemplate =
        new RestTemplate(
            Arrays.asList(new FormHttpMessageConverter(), tokenResponseHttpMessageConverter));
    restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
    accessTokenResponseClient.setRestOperations(restTemplate);

    return accessTokenResponseClient;
  }

  /**
   * Custom user authorities mapper
   *
   * @return
   */
  @Bean
  public GrantedAuthoritiesMapper userAuthoritiesMapper() {
    return (authorities) -> {
      Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
      authorities.forEach(
          authority -> {
            if (OidcUserAuthority.class.isInstance(authority)) {
              OidcUserAuthority oidcUserAuthority = (OidcUserAuthority) authority;
              OidcUserInfo userInfo = oidcUserAuthority.getUserInfo();
              if (userInfo.containsClaim("authorities")) {
                ObjectMapper objectMapper = new ObjectMapper();
                ArrayList<Role> authorityList =
                    objectMapper.convertValue(
                        userInfo.getClaimAsMap("authorities"),
                        new TypeReference<ArrayList<Role>>() {});
                for (Role role : authorityList) {
                  String roleName = "ROLE_" + role.getAuthority();
                  mappedAuthorities.add(new SimpleGrantedAuthority(roleName));
                }
              } else {
                String scope = ((OidcUserAuthority) authority).getAuthority();
                if (scope.contains("SCOPE")) {
                  scope = scope.replace("SCOPE", "ROLE");
                }
                mappedAuthorities.add(new SimpleGrantedAuthority(scope));
              }
            } else if (OAuth2UserAuthority.class.isInstance(authority)) {
              OAuth2UserAuthority oauth2UserAuthority = (OAuth2UserAuthority) authority;
              Map<String, Object> userAttributes = oauth2UserAuthority.getAttributes();
              if (userAttributes.containsKey("authorities")) {
                ObjectMapper objectMapper = new ObjectMapper();
                ArrayList<Role> authorityList =
                    objectMapper.convertValue(
                        userAttributes.get("authorities"), new TypeReference<ArrayList<Role>>() {});
                for (Role role : authorityList) {
                  String roleName = "ROLE_" + role.getAuthority();
                  mappedAuthorities.add(new SimpleGrantedAuthority(roleName));
                }
              }
            } else if (SimpleGrantedAuthority.class.isInstance(authority)) {
              String scope = authority.getAuthority();
              if (scope.contains("SCOPE")) {
                scope = scope.replace("SCOPE", "ROLE");
              }
              mappedAuthorities.add(new SimpleGrantedAuthority(scope));
            }
          });
      return mappedAuthorities;
    };
  }

  /**
   * Authorised client manager
   *
   * @param clientRegistrationRepository
   * @param authorizedClientRepository
   * @return
   */
  @Bean
  public OAuth2AuthorizedClientManager authorizedClientManager(
      ClientRegistrationRepository clientRegistrationRepository,
      OAuth2AuthorizedClientRepository authorizedClientRepository) {
    OAuth2AuthorizedClientProvider authorizedClientProvider =
        OAuth2AuthorizedClientProviderBuilder.builder()
            .authorizationCode()
            .refreshToken()
            .clientCredentials()
            .password()
            .build();
    DefaultOAuth2AuthorizedClientManager authorizedClientManager =
        new DefaultOAuth2AuthorizedClientManager(
            clientRegistrationRepository, authorizedClientRepository);
    authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
    return authorizedClientManager;
  }
}
