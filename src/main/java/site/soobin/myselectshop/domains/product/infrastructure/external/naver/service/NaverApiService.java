package site.soobin.myselectshop.domains.product.infrastructure.external.naver.service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import site.soobin.myselectshop.domains.product.infrastructure.external.naver.dto.ItemDto;

@Slf4j(topic = "NAVER API")
@Service
public class NaverApiService {
  private final RestTemplate restTemplate;
  private final NaverSearchRequestFactory requestFactory;
  private final NaverItemMapper itemMapper;

  public NaverApiService(
      RestTemplateBuilder builder,
      NaverSearchRequestFactory requestFactory,
      NaverItemMapper itemMapper) {
    this.restTemplate = builder.build();
    this.requestFactory = requestFactory;
    this.itemMapper = itemMapper;
  }

  public List<ItemDto> searchItems(String query) {
    RequestEntity<Void> request = requestFactory.createSearchRequest(query);
    ResponseEntity<String> response = restTemplate.exchange(request, String.class);

    log.info("NAVER API Status Code : {}", response.getStatusCode());

    return itemMapper.toItemDtos(response.getBody());
  }
}
