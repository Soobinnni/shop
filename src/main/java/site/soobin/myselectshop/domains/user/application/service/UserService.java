package site.soobin.myselectshop.domains.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import site.soobin.myselectshop.commons.exception.ApiBusinessException;
import site.soobin.myselectshop.domains.user.application.dto.SignupRequestDto;
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
    String username = requestDto.getUsername();
    String password = passwordEncoder.encode(requestDto.getPassword());

    // 회원 중복 확인
    if (userRepository.existsByUsername(username)) {
      throw new ApiBusinessException(UserErrorSpec.DUPLICATE_USERNAME);
    }

    // email 중복확인
    String email = requestDto.getEmail();
    if (userRepository.existsByEmail(email)) {
      throw new ApiBusinessException(UserErrorSpec.DUPLICATE_EMAIL);
    }

    // 사용자 ROLE 확인
    UserRoleEnum role =
        domainService.validateAndGetRole(requestDto.isAdmin(), requestDto.getAdminToken());

    // 사용자 등록
    User user = new User(username, password, email, role);
    userRepository.save(user);
  }
}
