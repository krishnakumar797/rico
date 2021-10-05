/* Licensed under Apache-2.0 */
package ro.common.uaa;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

/**
 * Custom logout Success handler
 *
 * @author r.krishnakumar
 */
@Log4j2
public class OAuthLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler
    implements LogoutSuccessHandler {

  @Override
  public void onLogoutSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication auth)
      throws IOException, ServletException {
    String targetUrl = null;
    try {
      targetUrl = "index";
      if (auth != null) {
        HttpSession session = request.getSession();
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        session.invalidate();
        SecurityContextHolder.getContext().setAuthentication(null);
        auth.setAuthenticated(false);
      }
      response.sendRedirect(targetUrl);
    } catch (Exception e) {
      log.error(
          "Error came when user logout and unable to redirected to " + auth + " " + targetUrl);
    }
  }
}
