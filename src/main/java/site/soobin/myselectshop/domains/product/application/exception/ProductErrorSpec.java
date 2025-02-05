package site.soobin.myselectshop.domains.product.application.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static site.soobin.myselectshop.domains.product.domain.ProductConstants.MIN_MY_PRICE;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import site.soobin.myselectshop.commons.exception.ErrorSpec;

@Getter
@RequiredArgsConstructor
public enum ProductErrorSpec implements ErrorSpec {
  DUPLICATE_PRODUCT_IN_FOLDER(CONFLICT, "이미 폴더에 존재하는 상품입니다."),
  NO_PRODUCT(NOT_FOUND, "해당 상품이 존재하지 않습니다."),
  NOT_OWNED_PRODUCT(FORBIDDEN, "회원님의 관심상품이 아닙니다."),
  INVALID_PRICE(BAD_REQUEST, "유효하지 않은 관심 가격입니다. 최소 %d원 이상으로 설정해주세요.".formatted(MIN_MY_PRICE));

  private final HttpStatusCode status;
  private final String message;
  private final String code = this.name();
}
