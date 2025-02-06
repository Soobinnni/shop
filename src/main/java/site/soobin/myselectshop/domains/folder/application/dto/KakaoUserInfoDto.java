package site.soobin.myselectshop.domains.folder.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoUserInfoDto {
  private Long id;
  private String nickname;

  //  private String email;

  public KakaoUserInfoDto(Long id, String nickname) {
    this.id = id;
    this.nickname = nickname;
    //    this.email = email;
  }
}
