package ro.common.security;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ro.common.exception.CommonSecurityException;
import ro.common.rest.CommonErrorCodes;
import ro.common.utils.HttpStatusCode;
import ro.common.utils.Utils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static ro.common.utils.Utils.*;

/**
 * JWT Authentication filter
 *
 * @author r.krishnakumar
 */
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private AuthenticationManager authenticationManager;

  private String usernameParameter;
  private String passwordParameter;

  public JwtAuthenticationFilter(
      AuthenticationManager authenticationManager,
      String loginProcessingUrl,
      String usernameParameter,
      String passwordParameter) {
    this.authenticationManager = authenticationManager;
    setFilterProcessesUrl(loginProcessingUrl);
    this.usernameParameter = usernameParameter;
    this.passwordParameter = passwordParameter;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
      throws AuthenticationException {
    try {
      JsonNode jsonNode = new ObjectMapper().readTree(req.getInputStream());
      return authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              jsonNode.get(this.usernameParameter).asText(),
              jsonNode.get(this.passwordParameter).asText(),
              new ArrayList<>()));
    } catch (Exception e) {
      CommonSecurityException cse =
          new CommonSecurityException(
              CommonErrorCodes.E_HTTP_UN_AUTHORIZED,
              req.getHeader(Utils.CORRELATION_ID),
              HttpStatusCode.UNAUTHORIZED,
              e.getMessage(),
              e);
      throw cse;
    }
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth)
      throws IOException, ServletException {
    final String authorities =
        auth.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));
    String token =
        JWT.create()
            .withSubject(((User) auth.getPrincipal()).getUsername())
            .withClaim(AUTHORITIES_KEY, authorities)
            .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .sign(HMAC512(SECRET.getBytes()));
    res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
  }

  protected void onAuthenticationFailure() {}
}
