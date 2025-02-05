package site.soobin.myselectshop.domains.user.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoDto {
  String username;
  boolean isAdmin;
}
