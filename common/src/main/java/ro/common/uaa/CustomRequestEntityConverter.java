package ro.common.uaa;

import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.util.MultiValueMap;

/** Customising access token request */
public class CustomRequestEntityConverter
    implements Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> {

  private OAuth2AuthorizationCodeGrantRequestEntityConverter defaultConverter;

  public CustomRequestEntityConverter() {
    defaultConverter = new OAuth2AuthorizationCodeGrantRequestEntityConverter();
  }

  /**
   * Modifying access token request with defaults from authorization code grant object
   *
   * @param req
   * @return
   */
  @Override
  public RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest req) {
    RequestEntity<?> entity = defaultConverter.convert(req);
    MultiValueMap<String, String> params = (MultiValueMap<String, String>) entity.getBody();
    // params.add("test2", "extra2");
    return new RequestEntity<>(params, entity.getHeaders(), entity.getMethod(), entity.getUrl());
  }
}
