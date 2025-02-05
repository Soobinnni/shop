package site.soobin.myselectshop.commons.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Log4j2(topic = "Global API Exception")
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(Exception.class)
  public final ResponseEntity<ApiErrorResponse> handleAllExceptions(
      Exception exception, WebRequest request) {
    ApiErrorResponse response =
        ApiErrorResponseFactory.createResponse(GlobalErrorSpec.INTERNAL_ERROR, request);
    return buildResponse(exception, response);
  }

  @ExceptionHandler(ApiBusinessException.class)
  public final ResponseEntity<ApiErrorResponse> handleBaseException(
      ApiBusinessException exception, WebRequest request) {
    ApiErrorResponse response =
        ApiErrorResponseFactory.createResponse(exception.getErrorSpec(), request);
    return buildResponse(exception, response);
  }

  private <T> ResponseEntity<T> buildResponse(Exception exception, ApiErrorResponse response) {
    logErrorIfEnabled(exception, response, true);
    return new ResponseEntity<>((T) response, HttpStatusCode.valueOf(response.getStatus()));
  }

  private void logErrorIfEnabled(
      Exception exception, ApiErrorResponse response, boolean shouldLog) {
    if (shouldLog) {
      log.error(
          "Exception: system message: {}, response body: {}", exception.getMessage(), response);
    }
  }
}
