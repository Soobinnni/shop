package site.soobin.myselectshop.domains.user.infrastructure.external.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.soobin.myselectshop.domains.user.infrastructure.external.dto.KakaoUserInfoDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoUserMapper {
  private static final String FIELD_ACCESS_TOKEN = "access_token";
  private static final String FIELD_ID = "id";
  private static final String FIELD_PROPERTIES = "properties";
  private static final String FIELD_NICKNAME = "nickname";

  private final ObjectMapper objectMapper;

  public String parseAccessToken(String responseBody) throws JsonProcessingException {
    JsonNode jsonNode = objectMapper.readTree(responseBody);
    return jsonNode.get(FIELD_ACCESS_TOKEN).asText();
  }

  public KakaoUserInfoDto parseUserInfo(String responseBody) throws JsonProcessingException {
    JsonNode jsonNode = objectMapper.readTree(responseBody);

    Long id = jsonNode.get(FIELD_ID).asLong();
    String nickname = jsonNode.get(FIELD_PROPERTIES).get(FIELD_NICKNAME).asText();

    log.info("카카오 사용자 정보: {} , {}", id, nickname);
    return KakaoUserInfoDto.from(id, nickname);
  }
}
