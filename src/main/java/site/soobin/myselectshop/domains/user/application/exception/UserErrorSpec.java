package site.soobin.myselectshop.domains.user.application.exception;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import site.soobin.myselectshop.commons.exception.ErrorSpec;

@Getter
@RequiredArgsConstructor
public enum UserErrorSpec implements ErrorSpec {
  DUPLICATE_USERNAME(CONFLICT, "중복된 사용자가 존재합니다."),
  DUPLICATE_EMAIL(CONFLICT, "중복된 Email 입니다."),
  INVALID_ADMIN_TOKEN(FORBIDDEN, "관리자 암호가 틀려 등록이 불가능합니다.");

  private final HttpStatusCode status;
  private final String message;
  private final String code = this.name();
}
