package site.soobin.myselectshop.domains.user.infrastructure.external.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import site.soobin.myselectshop.domains.user.infrastructure.external.dto.KakaoUserInfoDto;

@Slf4j(topic = "KAKAO Login")
@Service
@RequiredArgsConstructor
public class KakaoApiService {
  private final RestTemplate restTemplate;
  private final KakaoLoginRequestFactory requestFactory;
  private final KakaoUserMapper kakaoUserMapper;

  public String getToken(String code) throws JsonProcessingException {
    RequestEntity<MultiValueMap<String, String>> requestEntity =
        requestFactory.createLoginRequest(code);

    // HTTP 요청 보내기
    ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

    // HTTP 응답 (JSON) -> 액세스 토큰 파싱
    return kakaoUserMapper.parseAccessToken(response.getBody());
  }

  public KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
    RequestEntity<MultiValueMap<String, String>> requestEntity =
        requestFactory.createUserInfoRequest(accessToken);

    // HTTP 요청 보내기
    ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

    // 응답 파싱 및 DTO 변환
    return kakaoUserMapper.parseUserInfo(response.getBody());
  }
}
