/* Licensed under Apache-2.0 */
package ro.common.rest;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ro.common.exception.CommonRestException;
import ro.common.exception.CommonSecurityException;
import ro.common.utils.Utils;

/**
 * Common class that provides centralized exception handling across the controller methods
 *
 * @author r.krishnakumar
 */
@ControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
@Log4j2
public class CommonControllerAdvice extends ResponseEntityExceptionHandler
    implements RequestBodyAdvice {

  /**
   * Handle method argument not valid violation exception.
   *
   * @return ResponseEntity<Object> with error response
   */
  @Override
  public ResponseEntity<Object> handleMethodArgumentNotValid(
      final MethodArgumentNotValidException manve,
      final HttpHeaders headers,
      final HttpStatus status,
      final WebRequest request) {
    final String correlationId = this.getCorrelationIdFromHeader(headers);
    final List<FieldError> fieldErrors = manve.getBindingResult().getFieldErrors();
    final CommonErrorResponse error = this.processParameterErrors(correlationId, fieldErrors);
    return this.handleExceptionInternal(manve, error, headers, status, request);
  }

  /** Handle missing servlet request parameter exception. */
  @Override
  public ResponseEntity<Object> handleMissingServletRequestParameter(
      final MissingServletRequestParameterException msrpe,
      final HttpHeaders headers,
      final HttpStatus status,
      final WebRequest request) {
    return this.generateErrorResponse(
        msrpe, CommonErrorCodes.E_HTTP_MISSING_SERVLET_REQ_PARAM, headers, status, request);
  }

  /** Handle HTTP Request method not supported exception. */
  @Override
  public ResponseEntity<Object> handleHttpRequestMethodNotSupported(
      final HttpRequestMethodNotSupportedException msrpe,
      final HttpHeaders headers,
      final HttpStatus status,
      final WebRequest request) {
    return this.generateErrorResponse(
        msrpe, CommonErrorCodes.E_HTTP_METHOD_NOT_ALLOWED, headers, status, request);
  }

  /** Handle Servlet request binding exception. */
  @Override
  public ResponseEntity<Object> handleServletRequestBindingException(
      final ServletRequestBindingException srbe,
      final HttpHeaders headers,
      final HttpStatus status,
      final WebRequest request) {
    return this.generateErrorResponse(
        srbe, CommonErrorCodes.E_HTTP_BINDING_ERR, headers, status, request);
  }

  /** Handle Type mismatch exceptions. */
  @Override
  public ResponseEntity<Object> handleTypeMismatch(
      final TypeMismatchException tme,
      final HttpHeaders headers,
      final HttpStatus status,
      final WebRequest request) {
    return this.generateErrorResponse(
        tme, CommonErrorCodes.E_HTTP_TYPE_MISMATCH, headers, status, request);
  }

  /** Handle http message not readable exception. */
  @Override
  public ResponseEntity<Object> handleHttpMessageNotReadable(
      final HttpMessageNotReadableException hnmre,
      final HttpHeaders headers,
      final HttpStatus status,
      final WebRequest request) {
    return this.generateErrorResponse(
        hnmre, CommonErrorCodes.E_HTTP_HTTP_MSG_UNREADABLE, headers, status, request);
  }

  /** Handle http message not readable exception. */
  @Override
  public ResponseEntity<Object> handleMissingServletRequestPart(
      final MissingServletRequestPartException msrpe,
      final HttpHeaders headers,
      final HttpStatus status,
      final WebRequest request) {
    return this.generateErrorResponse(
        msrpe, CommonErrorCodes.E_HTTP_MISSING_SERVLET_REQ_PARAM, headers, status, request);
  }

  /** Handle http bind exception. */
  @Override
  public ResponseEntity<Object> handleBindException(
      final BindException be,
      final HttpHeaders headers,
      final HttpStatus status,
      final WebRequest request) {
    return this.generateErrorResponse(
        be, CommonErrorCodes.E_HTTP_BINDING_ERR, headers, status, request);
  }

  /** Handle Message not acceptable exception */
  @Override
  public ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
      final HttpMediaTypeNotAcceptableException ex,
      final HttpHeaders headers,
      final HttpStatus status,
      final WebRequest request) {
    return this.generateErrorResponse(
        ex, CommonErrorCodes.E_HTTP_BAD_REQUEST, headers, status, request);
  }

  /** Handle invalid url exceptions */
  @Override
  public ResponseEntity<Object> handleNoHandlerFoundException(
      final NoHandlerFoundException ex,
      final HttpHeaders headers,
      final HttpStatus status,
      final WebRequest request) {

    return this.generateErrorResponse(
        ex, CommonErrorCodes.E_HTTP_UNKNOWN_URL, headers, status, request);
  }

  /**
   * Generate the error response for the generated exception
   *
   * @param e the Exception
   * @param errorCode - respective error code
   * @param headers the header for response
   * @param status the status of the response
   * @param request the request
   * @return ResponseEntity<Object> - the ResponseEntity<Object> with error
   */
  private ResponseEntity<Object> generateErrorResponse(
      final Exception e,
      final String errorCode,
      final HttpHeaders headers,
      final HttpStatus status,
      final WebRequest request) {
    final String correlationId = this.getCorrelationIdFromHeader(headers);
    final CommonErrorResponse error = Utils.prepareErrorResponse(status, errorCode, correlationId);
    return this.handleExceptionInternal(e, error, headers, status, request);
  }

  /**
   * Handle constrained violation exception.
   *
   * @param cve the constraint violation
   * @return ErrorResponse
   */
  @ExceptionHandler(value = {ConstraintViolationException.class})
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ResponseEntity<CommonErrorResponse> handleConstraintViolation(
      final ConstraintViolationException cve) {
    HttpServletRequest request =
        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    final String correlationId = request.getHeader(Utils.CORRELATION_ID);
    final Set<ConstraintViolation<?>> violations = cve.getConstraintViolations();
    String errorCode = null;
    for (final ConstraintViolation<?> violation : violations) {
      errorCode = violation.getMessage();
    }
    final CommonErrorResponse error =
        Utils.prepareErrorResponse(HttpStatus.BAD_REQUEST, errorCode, correlationId);
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.set(
        Utils.CORRELATION_ID, error.getCorrelationId() != null ? error.getCorrelationId() : null);
    try {
      return new ResponseEntity<>(
          error, responseHeaders, HttpStatus.valueOf(Integer.parseInt(error.getStatus())));
    } catch (final HttpStatusCodeException | NumberFormatException exception) {
      return new ResponseEntity<>(error, responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Exception Handler for Custom Exceptions.
   *
   * @param ce the CommonRestException
   * @return ResponseEntity<ErrorResponse> with the error response for CommonRestException
   */
  @ResponseBody
  @ExceptionHandler(CommonRestException.class)
  public ResponseEntity<CommonErrorResponse> handleCustomException(final CommonRestException ce) {
    final CommonErrorResponse error =
        Utils.prepareErrorResponse(
            ce.getStatus(),
            ce.getCode(),
            ce.getCorrelationId() != null ? ce.getCorrelationId() : null);
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.set(
        Utils.CORRELATION_ID, ce.getCorrelationId() != null ? ce.getCorrelationId() : null);
    try {
      return new ResponseEntity<>(error, HttpStatus.valueOf(Integer.parseInt(error.getStatus())));
    } catch (final HttpStatusCodeException | NumberFormatException exception) {
      return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Exception Handler for web Security Exceptions.
   *
   * @param io the IOException
   * @return ResponseEntity<ErrorResponse> with the error response for CommonSecurityException
   */
  @ResponseBody
  @ExceptionHandler(IOException.class)
  public ResponseEntity<CommonErrorResponse> handleCustomSecurityException(final IOException io) {
    CommonErrorResponse error = new CommonErrorResponse();
    if (io.getCause() instanceof CommonSecurityException) {
      CommonSecurityException cse = (CommonSecurityException) io.getCause();
      error =
          Utils.prepareErrorResponse(
              cse.getStatus(),
              cse.getCode(),
              cse.getCorrelationId() != null ? cse.getCorrelationId() : null);
      HttpHeaders responseHeaders = new HttpHeaders();
      responseHeaders.set(
          Utils.CORRELATION_ID, cse.getCorrelationId() != null ? cse.getCorrelationId() : null);
    }
    try {
      return new ResponseEntity<>(error, HttpStatus.valueOf(Integer.parseInt(error.getStatus())));
    } catch (final HttpStatusCodeException | NumberFormatException exception) {
      return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

//  /**
//   * Exception Handler for REST Security Exceptions.
//   *
//   * @param cse the CommonSecurityException
//   * @return ResponseEntity<ErrorResponse> with the error response for CommonSecurityException
//   */
//  @ResponseBody
//  @ExceptionHandler(CommonSecurityException.class)
//  public ResponseEntity<CommonErrorResponse> handleCustomSecurityException(
//      final CommonSecurityException cse) {
//    CommonErrorResponse error = new CommonErrorResponse();
//    error =
//        this.prepareErrorResponse(
//            cse.getStatus(),
//            cse.getCode(),
//            cse.getCorrelationId() != null ? cse.getCorrelationId() : null);
//    HttpHeaders responseHeaders = new HttpHeaders();
//    responseHeaders.set(
//        Utils.CORRELATION_ID, cse.getCorrelationId() != null ? cse.getCorrelationId() : null);
//    try {
//      return new ResponseEntity<>(error, HttpStatus.valueOf(Integer.parseInt(error.getStatus())));
//    } catch (final HttpStatusCodeException | NumberFormatException exception) {
//      return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//  }

//  /**
//   * Exception Handler for REST Security Exceptions.
//   *
//   * @param ase the AuthenticationException
//   * @return ResponseEntity<ErrorResponse> with the error response for CommonSecurityException
//   */
//  @ResponseBody
//  @ExceptionHandler(AuthenticationException.class)
//  public ResponseEntity<CommonErrorResponse> handleCustomSecurityException(
//          final AuthenticationException ase, HttpRequest request) {
//    HttpHeaders headers = request.getHeaders();
//    String correlationId = null != headers ? headers.getFirst(Utils.CORRELATION_ID) : null;
//    CommonErrorResponse error = new CommonErrorResponse();
//    error =
//            this.prepareErrorResponse(
//                    HttpStatus.UNAUTHORIZED, CommonErrorCodes.E_HTTP_FORBIDDEN_ACCESS, correlationId);
//    HttpHeaders responseHeaders = new HttpHeaders();
//    responseHeaders.set(Utils.CORRELATION_ID, correlationId);
//    try {
//      return new ResponseEntity<>(error, HttpStatus.valueOf(Integer.parseInt(error.getStatus())));
//    } catch (final HttpStatusCodeException | NumberFormatException exception) {
//      return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//  }

  /**
   * Exception Handler for unhandled Exceptions for API requests.
   *
   * @param e - Any Exception
   * @return ErrorResponse
   */
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  @ExceptionHandler(Exception.class)
  public ResponseEntity<CommonErrorResponse> handleException(final Exception e) {
    HttpServletRequest request =
        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    final String correlationId = request.getHeader(Utils.CORRELATION_ID);
    final CommonErrorResponse error =
        Utils.prepareErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR, CommonErrorCodes.E_GEN_INTERNAL_ERR, correlationId);
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.set(
        Utils.CORRELATION_ID, error.getCorrelationId() != null ? error.getCorrelationId() : null);
    try {
      return new ResponseEntity<>(error, HttpStatus.valueOf(Integer.parseInt(error.getStatus())));
    } catch (final HttpStatusCodeException | NumberFormatException exception) {
      return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Process validation error from the list of invalid parameters in the request.
   *
   * @param fieldErrors - List of field errors
   * @return ErrorResponse - the error response
   */
  private CommonErrorResponse processParameterErrors(
      final String correlationId, final List<FieldError> fieldErrors) {
    final CommonErrorResponse error = new CommonErrorResponse();
    error.setCorrelationId(correlationId);
    error.setStatus(Integer.toString(HttpStatus.BAD_REQUEST.value()));
    if (fieldErrors != null && !fieldErrors.isEmpty()) {
      final String errorCode = fieldErrors.get(0).getDefaultMessage();
      error.setErrorCode(Utils.generateErrorCode(errorCode));
    }
    return error;
  }

  /**
   * Get the correlation id from HttpServletRequest.
   *
   * @return the correlation id, null if correlation id not available in http servlet request
   */
  private String getCorrelationIdFromHeader(HttpHeaders headers) {
    return headers.getFirst(Utils.CORRELATION_ID);
  }

  /** The default implementation returns the body that was passed in. */
  @Override
  @Nullable
  public Object handleEmptyBody(
      @Nullable final Object body,
      final HttpInputMessage inputMessage,
      final MethodParameter parameter,
      final Type targetType,
      final Class<? extends HttpMessageConverter<?>> converterType) {
    return body;
  }

  /** The default implementation returns true. */
  @Override
  public boolean supports(
      final MethodParameter methodParameter,
      final Type targetType,
      final Class<? extends HttpMessageConverter<?>> converterType) {
    return true;
  }

  @Override
  public HttpInputMessage beforeBodyRead(
      HttpInputMessage inputMessage,
      MethodParameter parameter,
      Type targetType,
      Class<? extends HttpMessageConverter<?>> converterType)
      throws IOException {
    return inputMessage;
  }

  @Override
  public Object afterBodyRead(
      Object body,
      HttpInputMessage inputMessage,
      MethodParameter parameter,
      Type targetType,
      Class<? extends HttpMessageConverter<?>> converterType) {
    return body;
  }
}