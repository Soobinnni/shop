package site.soobin.myselectshop.domains.user.application.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import site.soobin.myselectshop.commons.exception.ApiBusinessException;
import site.soobin.myselectshop.domains.user.application.exception.UserErrorSpec;
import site.soobin.myselectshop.domains.user.domain.entity.UserRoleEnum;

@Service
public class UserDomainService {
  @Value("${token.signup.admin}")
  private String ADMIN_TOKEN;

  public UserRoleEnum validateAndGetRole(boolean isAdmin, String adminToken) {
    if (!isAdmin) return UserRoleEnum.USER;

    if (!ADMIN_TOKEN.equals(adminToken)) {
      throw new ApiBusinessException(UserErrorSpec.INVALID_ADMIN_TOKEN);
    }
    return UserRoleEnum.ADMIN;
  }
}
