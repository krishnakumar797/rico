package ro.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import ro.common.rest.CommonErrorCodes;
import ro.common.rest.CommonErrorResponse;
import ro.common.utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/** Authentication entry point for custom message */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException, ServletException {
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    CommonErrorResponse error = new CommonErrorResponse();
    String correlationId = request.getHeader(Utils.CORRELATION_ID);
    if (authException instanceof InsufficientAuthenticationException) {
      error =
          Utils.prepareErrorResponse(
              HttpStatus.UNAUTHORIZED, CommonErrorCodes.E_HTTP_UNAUTHORIZED_RQST, correlationId);
      response.setHeader(Utils.CORRELATION_ID, correlationId);
    }
    OutputStream out = response.getOutputStream();
    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(out, error);
    out.flush();
  }
}
