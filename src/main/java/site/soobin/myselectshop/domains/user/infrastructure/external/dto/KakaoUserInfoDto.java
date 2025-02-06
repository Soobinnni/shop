package site.soobin.myselectshop.domains.user.infrastructure.external.dto;

public record KakaoUserInfoDto(Long id, String nickname) {

  public static KakaoUserInfoDto from(Long id, String nickname) {
    return new KakaoUserInfoDto(id, nickname);
  }
}
