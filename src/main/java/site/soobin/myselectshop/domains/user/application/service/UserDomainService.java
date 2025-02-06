package site.soobin.myselectshop.domains.user.application.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import site.soobin.myselectshop.commons.exception.ApiBusinessException;
import site.soobin.myselectshop.commons.security.UserDetailsImpl;
import site.soobin.myselectshop.domains.user.application.dto.UserInfoDto;
import site.soobin.myselectshop.domains.user.application.exception.UserErrorSpec;
import site.soobin.myselectshop.domains.user.domain.entity.User;
import site.soobin.myselectshop.domains.user.domain.entity.UserRoleEnum;
import site.soobin.myselectshop.domains.user.infrastructure.external.dto.KakaoUserInfoDto;

@Service
@RequiredArgsConstructor
public class UserDomainService {
  private final PasswordEncoder passwordEncoder;

  @Value("${token.signup.admin}")
  private String ADMIN_TOKEN;

  public UserRoleEnum validateAndGetRole(Boolean isAdmin, String adminToken) {
    if (isAdmin == null) return UserRoleEnum.USER;

    if (!ADMIN_TOKEN.equals(adminToken)) {
      throw new ApiBusinessException(UserErrorSpec.INVALID_ADMIN_TOKEN);
    }
    return UserRoleEnum.ADMIN;
  }

  public UserInfoDto getUserInfo(UserDetailsImpl principal) {
    User user = principal.getUser();
    return new UserInfoDto(user.getUsername(), user.getRole() == UserRoleEnum.ADMIN);
  }

  public String getHashedPassword(String originPassword) {
    return passwordEncoder.encode(originPassword);
  }

  public String generateRandomPassword() {
    return UUID.randomUUID().toString();
  }

  public String generateKakaoEmail(String nickname) {
    return nickname + "@asdf.com";
  }

  public User createNewKakaoUser(KakaoUserInfoDto kakaoUserInfo, String kakaoEmail) {
    String password = this.generateRandomPassword();
    String encodedPassword = this.getHashedPassword(password);

    return User.builder(kakaoUserInfo.nickname(), encodedPassword, kakaoEmail, UserRoleEnum.USER)
        .kakaoId(kakaoUserInfo.id())
        .build();
  }
}
