package site.soobin.myselectshop.commons.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiErrorResponseFactory {
  public static ApiErrorResponse createResponse(ErrorSpec errorSpec, Object request) {
    return createResponse(errorSpec, request, null);
  }

  public static ApiErrorResponse createResponse(
      ErrorSpec errorSpec, Object request, Map<String, String> details) {
    return ApiErrorResponse.builder()
        .code(errorSpec.getCode())
        .message(errorSpec.getMessage())
        .status(errorSpec.getStatus().value())
        .path(extractPath(request))
        .details(details)
        .build();
  }

  private static String extractPath(Object request) {
    return request instanceof HttpServletRequest
        ? ((HttpServletRequest) request).getRequestURI()
        : null;
  }
}
