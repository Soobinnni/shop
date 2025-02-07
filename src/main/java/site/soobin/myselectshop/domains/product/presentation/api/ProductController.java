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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
  private final ProductService service;

  @PostMapping
  public ProductResponseDto createProduct(
      @RequestBody ProductRequestDto requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return service.createProduct(requestDto, userDetails);
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
