package site.soobin.myselectshop.domains.user.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import site.soobin.myselectshop.commons.security.jwt.JwtUtil;
import site.soobin.myselectshop.domains.user.domain.entity.User;
import site.soobin.myselectshop.domains.user.domain.repository.UserRepository;
import site.soobin.myselectshop.domains.user.infrastructure.external.dto.KakaoUserInfoDto;
import site.soobin.myselectshop.domains.user.infrastructure.external.service.KakaoApiService;

@Service
@RequiredArgsConstructor
@Log4j2
public class KakaoUserService {
  private final UserRepository userRepository;

  private final KakaoApiService kakaoApiService;
  private final UserDomainService userDomainService;
  private final JwtUtil jwtUtil;

  public String kakaoLogin(String code) throws JsonProcessingException {
    // 1. "인가 코드"로 "액세스 토큰" 요청
    String accessToken = kakaoApiService.getToken(code);
    // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
    KakaoUserInfoDto kakaoUserInfo = kakaoApiService.getKakaoUserInfo(accessToken);
    // 3. 필요시에 회원가입
    User kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo);
    // 4. jwt 토큰 반환
    return jwtUtil.createToken(kakaoUser.getUsername(), kakaoUser.getRole());
  }

  private User registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {
    // 기존 카카오 사용자 찾기
    User kakaoUser = userRepository.findByKakaoId(kakaoUserInfo.id()).orElse(null);

    if (kakaoUser == null) {
      kakaoUser = processNewKakaoUser(kakaoUserInfo);
      userRepository.save(kakaoUser);
    }

    return kakaoUser;
  }

  // 새로운 카카오 사용자 처리
  private User processNewKakaoUser(KakaoUserInfoDto kakaoUserInfo) {
    String kakaoEmail = userDomainService.generateKakaoEmail(kakaoUserInfo.nickname());
    User existingUser = userRepository.findByEmail(kakaoEmail).orElse(null);

    if (existingUser != null) { // 기존 사용자에 카카오 ID 업데이트
      return existingUser.kakaoIdUpdate(kakaoUserInfo.id());
    }
    return userDomainService.createNewKakaoUser(kakaoUserInfo, kakaoEmail); // 새로운 사용자->회원가입
  }
}
