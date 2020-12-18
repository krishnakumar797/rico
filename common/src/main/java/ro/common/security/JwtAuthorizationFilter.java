package ro.common.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static ro.common.utils.Utils.*;

/**
 * Jwt Authorization filter class
 *
 * @author r.krishnakumar
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

  public JwtAuthorizationFilter(AuthenticationManager authManager) {
    super(authManager);
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest req, HttpServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    String header = req.getHeader(HEADER_STRING);
    if (header == null || !header.startsWith(TOKEN_PREFIX)) {
      chain.doFilter(req, res);
      return;
    }
    UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    chain.doFilter(req, res);
  }

  /**
   * Create UsernamePassword Authentication token
   *
   * @param request
   * @return
   */
  private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
    String token = request.getHeader(HEADER_STRING);
    if (token == null) {
      return null;
    }
    // Parse the token.
    DecodedJWT decodedJWT =
        JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
            .build()
            .verify(token.replace(TOKEN_PREFIX, ""));
    String user = decodedJWT.getSubject();
    final Collection authorities =
        Arrays.stream(decodedJWT.getClaims().get(AUTHORITIES_KEY).asString().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    if (user != null) {
      return new UsernamePasswordAuthenticationToken(user, null, authorities);
    }
    return null;
  }
}
