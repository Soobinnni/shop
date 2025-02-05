package site.soobin.myselectshop.commons.exception;

import lombok.Getter;

@Getter
public class ApiBusinessException extends RuntimeException {
  private final ErrorSpec errorSpec;

  public ApiBusinessException(ErrorSpec errorSpec) {
    super(errorSpec.getMessage());
    this.errorSpec = errorSpec;
  }
}
