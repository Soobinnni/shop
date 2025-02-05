package site.soobin.myselectshop.domains.product.presentation.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.soobin.myselectshop.commons.security.UserDetailsImpl;
import site.soobin.myselectshop.domains.product.application.dto.ProductResponseDto;
import site.soobin.myselectshop.domains.product.application.service.ProductService;
import site.soobin.myselectshop.domains.user.domain.entity.UserRoleEnum.Authority;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/products")
public class AdminProductController {
  private final ProductService service;

  @GetMapping
  @Secured(Authority.ADMIN)
  public Page<ProductResponseDto> getProducts(
      @AuthenticationPrincipal UserDetailsImpl principal,
      @RequestParam("page") int page,
      @RequestParam("size") int size,
      @RequestParam("sortBy") String sortBy,
      @RequestParam("isAsc") boolean isAsc) {
    return service.getProducts(principal, page - 1, size, sortBy, isAsc);
  }
}
