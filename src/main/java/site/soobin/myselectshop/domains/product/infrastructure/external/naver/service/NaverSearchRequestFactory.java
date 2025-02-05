package site.soobin.myselectshop.domains.product.infrastructure.external.naver.service;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
class NaverSearchRequestFactory {
  private static final String BASE_URL = "https://openapi.naver.com";
  private static final String SEARCH_PATH = "/v1/search/shop.json";
  private static final int DEFAULT_DISPLAY_COUNT = 15;
  private static final String CLIENT_ID_HEADER = "X-Naver-Client-Id";
  private static final String CLIENT_SECRET_HEADER = "X-Naver-Client-Secret";

  @Value("${naver.client.id}")
  private String naverClientId;

  @Value("${naver.client.secret}")
  private String naverClientSecret;

  public RequestEntity<Void> createSearchRequest(String query) {
    URI uri =
        UriComponentsBuilder.fromUriString(BASE_URL)
            .path(SEARCH_PATH)
            .queryParam("display", DEFAULT_DISPLAY_COUNT)
            .queryParam("query", query)
            .encode()
            .build()
            .toUri();

    return RequestEntity.get(uri)
        .header(CLIENT_ID_HEADER, naverClientId)
        .header(CLIENT_SECRET_HEADER, naverClientSecret)
        .build();
  }
}
