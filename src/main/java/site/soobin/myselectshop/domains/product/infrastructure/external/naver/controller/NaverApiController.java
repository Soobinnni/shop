package site.soobin.myselectshop.domains.product.infrastructure.external.naver.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.soobin.myselectshop.domains.product.infrastructure.external.naver.dto.ItemDto;
import site.soobin.myselectshop.domains.product.infrastructure.external.naver.service.NaverApiService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class NaverApiController {

  private final NaverApiService naverApiService;

  @GetMapping("/search")
  public List<ItemDto> searchItems(@RequestParam("query") String query) {
    return naverApiService.searchItems(query);
  }
}
