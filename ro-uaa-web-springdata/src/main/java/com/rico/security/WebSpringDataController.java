/* Licensed under Apache-2.0 */
package com.rico.security;

import com.rico.security.services.UserService;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import ro.common.rest.CommonDTOConverter;
import ro.common.uaa.UAARepository;

/** Testing Web controller */
@Controller
@Validated
@Log4j2
public class WebSpringDataController {

  private static final String authorizationRequestBaseUri = "oauth2/authorize-client";

  @Autowired private CommonDTOConverter dtoConverter;

  @Autowired private UserService userService;

  @Autowired private UAARepository uaaRepository;

  @GetMapping(value = {"/index", "/"})
  public String homePage(HttpServletRequest request) {
    return "thyme/index";
  }

  @GetMapping(value = {"/oauth_login"})
  public String login(HttpServletRequest request, Model model) {
    Map<String, String> oauth2AuthenticationUrls = new HashMap<>();
    Iterable<ClientRegistration> clientRegistrations = null;
    ResolvableType type =
        ResolvableType.forInstance(uaaRepository.getClientRegistrationRepository())
            .as(Iterable.class);
    if (type != ResolvableType.NONE
        && ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
      clientRegistrations =
          (Iterable<ClientRegistration>) uaaRepository.getClientRegistrationRepository();
    }
    clientRegistrations.forEach(
        registration ->
            oauth2AuthenticationUrls.put(
                registration.getClientName(),
                authorizationRequestBaseUri + "/" + registration.getRegistrationId()));
    model.addAttribute("urls", oauth2AuthenticationUrls);
    return "jsp/login/oauth_login";
  }

  @GetMapping(value = {"/welcome"})
  public String welcome(
      HttpSession session,
      HttpServletRequest request,
      OAuth2AuthenticationToken authentication,
      Model model) {
    try {
      OAuth2AuthorizedClient authorizedClient =
          this.uaaRepository
              .getAuthorizedClientRepository()
              .loadAuthorizedClient(
                  authentication.getAuthorizedClientRegistrationId(), authentication, request);
      OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
      model.addAttribute("name", authentication.getPrincipal().getAttribute("user_name"));
    } catch (Exception e) {
      log.error("Error at welcome page ", e);
    }
    return "thyme/welcome";
  }
}
