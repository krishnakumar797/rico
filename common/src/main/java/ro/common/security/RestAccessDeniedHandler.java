/* Licensed under Apache-2.0 */
package ro.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import ro.common.rest.CommonErrorCodes;
import ro.common.rest.CommonErrorResponse;
import ro.common.utils.Utils;

/** Access denied handler for custom error message */
@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

  @Override
  public void handle(
      HttpServletRequest request, HttpServletResponse response, AccessDeniedException e)
      throws IOException, ServletException {
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    CommonErrorResponse error = new CommonErrorResponse();
    String correlationId = request.getHeader(Utils.CORRELATION_ID);
    error =
        Utils.prepareErrorResponse(
            HttpStatus.FORBIDDEN, CommonErrorCodes.E_HTTP_FORBIDDEN, correlationId);
    response.setHeader(Utils.CORRELATION_ID, correlationId);
    OutputStream out = response.getOutputStream();
    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(out, error);
    out.flush();
  }
}
