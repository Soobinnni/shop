package site.soobin.myselectshop.commons.utils;

import java.util.Map;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationErrorUtil {

  public static Map<String, String> extractFieldErrors(MethodArgumentNotValidException exception) {
    return exception.getBindingResult().getFieldErrors().stream()
        .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
  }

  public static Map<String, String> extractMethodErrors(
      HandlerMethodValidationException exception) {
    return exception.getParameterValidationResults().stream()
        .collect(
            Collectors.toMap(
                result -> {
                  MessageSourceResolvable error = result.getResolvableErrors().get(0);
                  String constraintName = error.getCodes()[1];

                  return constraintName;
                },
                result -> result.getResolvableErrors().get(0).getDefaultMessage(),
                (existing, replacement) -> existing));
  }
}
