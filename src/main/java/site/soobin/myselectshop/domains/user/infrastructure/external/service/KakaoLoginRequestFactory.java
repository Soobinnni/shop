package site.soobin.myselectshop.domains.user.infrastructure.external.service;

import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class KakaoLoginRequestFactory {
  private static final String BASE_URL = "https://kauth.kakao.com";
  private static final String OAUTH_PATH = "/oauth/token";
  private static final String API_BASE_URL = "https://kapi.kakao.com";
  private static final String USER_INFO_PATH = "/v2/user/me";
  private static final String CONTENT_TYPE = "application/x-www-form-urlencoded;charset=utf-8";
  private static final String GRANT_TYPE = "authorization_code";
  private static final String REDIRECT_URI = "http://localhost:8080/api/user/kakao/callback";

  @Value("${kakao.key.rest-api}")
  private String kakaoRestApiKey;

  public RequestEntity<MultiValueMap<String, String>> createLoginRequest(String code) {
    URI uri = buildUri(BASE_URL, OAUTH_PATH);
    HttpHeaders headers = buildHeaders();
    MultiValueMap<String, String> body = buildAuthRequestBody(code);
    return RequestEntity.post(uri).headers(headers).body(body);
  }

  public RequestEntity<MultiValueMap<String, String>> createUserInfoRequest(String accessToken) {
    URI uri = buildUri(API_BASE_URL, USER_INFO_PATH);
    HttpHeaders headers = buildAuthHeaders(accessToken);
    return RequestEntity.post(uri).headers(headers).body(new LinkedMultiValueMap<>());
  }

  private URI buildUri(String baseUrl, String path) {
    return UriComponentsBuilder.fromUriString(baseUrl).path(path).encode().build().toUri();
  }

  private HttpHeaders buildHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-type", CONTENT_TYPE);
    return headers;
  }

  private HttpHeaders buildAuthHeaders(String accessToken) {
    HttpHeaders headers = buildHeaders();
    headers.add("Authorization", "Bearer " + accessToken);
    return headers;
  }

  private MultiValueMap<String, String> buildAuthRequestBody(String code) {
    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("grant_type", GRANT_TYPE);
    body.add("client_id", kakaoRestApiKey);
    body.add("redirect_uri", REDIRECT_URI);
    body.add("code", code);
    return body;
  }
}
