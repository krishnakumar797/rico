/* Licensed under Apache-2.0 */
package ro.common.uaa;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ro.common.exception.CommonSecurityException;
import ro.common.rest.CommonErrorCodes;
import ro.common.security.User;
import ro.common.security.UserLoginService;
import ro.common.utils.HttpStatusCode;
import ro.common.utils.Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;

/**
 * Custom OAuth Login success handler
 *
 * @author r.krishnakumar
 */
@Log4j2
public class OAuthCustomLoginSuccessHandler implements AuthenticationSuccessHandler {

  private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException {
    handle(request, response, authentication);
    clearAuthenticationAttributes(request);
  }

  private void handle(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException {
    String targetUrl = determineTargetUrl(authentication);
    if (response.isCommitted()) {
      log.debug("Response has already been committed. Unable to redirect to " + targetUrl);
      return;
    }
    redirectStrategy.sendRedirect(request, response, targetUrl);
  }

  /** Builds the target URL according to the config file */
  private String determineTargetUrl(Authentication authentication) {
    int count = 0;
    String defaultUrl = "/";
    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    for (GrantedAuthority grantedAuthority : authorities) {
      if (Utils.defaultTargets.containsKey(grantedAuthority.getAuthority())) {
        count++;
        defaultUrl = Utils.defaultTargets.get(grantedAuthority.getAuthority());
      }
    }
    if (count == 1) {
      return defaultUrl;
    }
    return "/";
  }

  private void clearAuthenticationAttributes(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session == null) {
      return;
    }
    session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
  }
}
