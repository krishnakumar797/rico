/* Licensed under Apache-2.0 */
package ro.common.uaa;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

/** Custom Authorization grant request resolver */
public class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

  private OAuth2AuthorizationRequestResolver defaultResolver;

  public CustomAuthorizationRequestResolver(
      ClientRegistrationRepository repo, String authorizationRequestBaseUri) {
    defaultResolver =
        new DefaultOAuth2AuthorizationRequestResolver(repo, authorizationRequestBaseUri);
  }

  @Override
  public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
    OAuth2AuthorizationRequest req = defaultResolver.resolve(request);
    if (req != null) {
      req = customizeAuthorizationGrantRequest(req);
    }
    return req;
  }

  @Override
  public OAuth2AuthorizationRequest resolve(
      HttpServletRequest request, String clientRegistrationId) {
    OAuth2AuthorizationRequest req = defaultResolver.resolve(request, clientRegistrationId);
    if (req != null) {
      req = customizeAuthorizationGrantRequest(req);
    }
    return req;
  }

  /**
   * Add additional attributes if we want to customize authorization grant request to UAA server
   *
   * @param req
   * @return
   */
  private OAuth2AuthorizationRequest customizeAuthorizationGrantRequest(
      OAuth2AuthorizationRequest req) {
    Map<String, Object> extraParams = new HashMap<String, Object>();
    extraParams.putAll(req.getAdditionalParameters());
    // extraParams.put("test", "extra");
    return OAuth2AuthorizationRequest.from(req).additionalParameters(extraParams).build();
  }
}
