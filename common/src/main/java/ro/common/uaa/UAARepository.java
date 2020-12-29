package ro.common.uaa;

import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;

public class UAARepository {

  private OAuth2AuthorizedClientRepository authorizedClientRepository;
  private ClientRegistrationRepository clientRegistrationRepository;

  public UAARepository(
      OAuth2AuthorizedClientRepository authorizedClientRepository,  ClientRegistrationRepository clientRegistrationRepository) {
    this.authorizedClientRepository = authorizedClientRepository;
    this.clientRegistrationRepository = clientRegistrationRepository;
  }

  public OAuth2AuthorizedClientRepository getAuthorizedClientRepository() {
    return authorizedClientRepository;
  }

  public ClientRegistrationRepository getClientRegistrationRepository() {
    return clientRegistrationRepository;
  }
}
