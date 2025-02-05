package site.soobin.myselectshop.domains.product.infrastructure.external.naver.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import site.soobin.myselectshop.domains.product.infrastructure.external.naver.dto.ItemDto;

@Component
class NaverItemMapper {
  private final String NAVER_ITEM_KEY = "items";

  public List<ItemDto> toItemDtos(String response) {
    JSONObject jsonObject = new JSONObject(response);

    return IntStream.range(0, jsonObject.getJSONArray(NAVER_ITEM_KEY).length())
        .mapToObj(i -> jsonObject.getJSONArray(NAVER_ITEM_KEY).getJSONObject(i))
        .map(ItemDto::from)
        .collect(Collectors.toList());
  }
}
