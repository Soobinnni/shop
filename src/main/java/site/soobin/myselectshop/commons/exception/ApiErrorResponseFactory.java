package site.soobin.myselectshop.commons.exception;

import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.context.request.WebRequest;

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
    return request instanceof WebRequest ? ((WebRequest) request).getDescription(false) : null;
  }
}
