package site.soobin.myselectshop.domains.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import site.soobin.myselectshop.commons.exception.ApiBusinessException;
import site.soobin.myselectshop.commons.security.UserDetailsImpl;
import site.soobin.myselectshop.domains.user.application.dto.SignupRequestDto;
import site.soobin.myselectshop.domains.user.application.dto.UserInfoDto;
import site.soobin.myselectshop.domains.user.application.exception.UserErrorSpec;
import site.soobin.myselectshop.domains.user.domain.entity.User;
import site.soobin.myselectshop.domains.user.domain.entity.UserRoleEnum;
import site.soobin.myselectshop.domains.user.domain.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final UserDomainService domainService;
  private final PasswordEncoder passwordEncoder;

  public void signup(SignupRequestDto requestDto) {
    String username = requestDto.username();
    String password = passwordEncoder.encode(requestDto.password());

    // 회원 중복 확인
    if (userRepository.existsByUsername(username)) {
      throw new ApiBusinessException(UserErrorSpec.DUPLICATE_USERNAME);
    }

    // email 중복확인
    String email = requestDto.email();
    if (userRepository.existsByEmail(email)) {
      throw new ApiBusinessException(UserErrorSpec.DUPLICATE_EMAIL);
    }

    // 사용자 ROLE 확인
    UserRoleEnum role =
        domainService.validateAndGetRole(requestDto.isAdmin(), requestDto.adminToken());

    // 사용자 등록
    User user = User.builder(username, password, email, role).build();
    userRepository.save(user);
  }

  public UserInfoDto getUserInfo(UserDetailsImpl principal) {
    return domainService.getUserInfo(principal);
  }
}
