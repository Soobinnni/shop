package site.soobin.myselectshop.domains.product.presentation.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.soobin.myselectshop.commons.security.UserDetailsImpl;
import site.soobin.myselectshop.domains.product.application.dto.ProductMypriceRequestDto;
import site.soobin.myselectshop.domains.product.application.dto.ProductRequestDto;
import site.soobin.myselectshop.domains.product.application.dto.ProductResponseDto;
import site.soobin.myselectshop.domains.product.application.service.ProductService;
import site.soobin.myselectshop.domains.user.domain.entity.ApiUseTime;
import site.soobin.myselectshop.domains.user.domain.entity.User;
import site.soobin.myselectshop.domains.user.domain.repository.ApiUseTimeRepository;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
  private final ProductService service;
  private final ApiUseTimeRepository apiUseTimeRepository;

  @PostMapping
  public ProductResponseDto createProduct(
      @RequestBody ProductRequestDto requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    // 측정 시작 시간
    long startTime = System.currentTimeMillis();

    try {
      // 응답 보내기
      return service.createProduct(requestDto, userDetails);
    } finally {
      // 측정 종료 시간
      long endTime = System.currentTimeMillis();
      // 수행시간 = 종료 시간 - 시작 시간
      long runTime = endTime - startTime;

      // 로그인 회원 정보
      User loginUser = userDetails.getUser();

      // API 사용시간 및 DB 에 기록
      ApiUseTime apiUseTime = apiUseTimeRepository.findByUser(loginUser).orElse(null);
      if (apiUseTime == null) {
        // 로그인 회원의 기록이 없으면
        apiUseTime = new ApiUseTime(loginUser, runTime);
      } else {
        // 로그인 회원의 기록이 이미 있으면
        apiUseTime.addUseTime(runTime);
      }

      System.out.println(
          "[API Use Time] Username: "
              + loginUser.getUsername()
              + ", Total Time: "
              + apiUseTime.getTotalTime()
              + " ms");
      apiUseTimeRepository.save(apiUseTime);
    }
  }

  @PutMapping("/{id}")
  public ProductResponseDto updateProduct(
      @PathVariable("id") Long id, @RequestBody ProductMypriceRequestDto requestDto) {
    return service.updateProduct(id, requestDto);
  }

  @GetMapping
  public Page<ProductResponseDto> getProducts(
      @AuthenticationPrincipal UserDetailsImpl principal,
      @RequestParam("page") int page,
      @RequestParam("size") int size,
      @RequestParam("sortBy") String sortBy,
      @RequestParam("isAsc") boolean isAsc) {
    return service.getProducts(principal, page - 1, size, sortBy, isAsc);
  }

  @PostMapping("/{productId}/folder")
  public void addFolder(
      @PathVariable("productId") Long productId,
      @RequestParam("folderId") Long folderId,
      @AuthenticationPrincipal UserDetailsImpl principal) {
    service.addFolder(productId, folderId, principal);
  }
}
