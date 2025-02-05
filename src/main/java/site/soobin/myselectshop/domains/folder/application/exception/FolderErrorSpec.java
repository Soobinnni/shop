package site.soobin.myselectshop.domains.folder.application.exception;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import site.soobin.myselectshop.commons.exception.ErrorSpec;

@Getter
@RequiredArgsConstructor
public enum FolderErrorSpec implements ErrorSpec {
  NOT_OWNER(FORBIDDEN, "회원님의 폴더가 아닙니다."),
  DUPLICATE_FOLDER(CONFLICT, "폴더명이 중복되었습니다."),
  ;

  private final HttpStatusCode status;
  private final String message;
  private final String code = this.name();
}
