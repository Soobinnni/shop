package site.soobin.myselectshop.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.soobin.myselectshop.dto.ProductMypriceRequestDto;
import site.soobin.myselectshop.dto.ProductRequestDto;
import site.soobin.myselectshop.dto.ProductResponseDto;
import site.soobin.myselectshop.security.UserDetailsImpl;
import site.soobin.myselectshop.service.ProductService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
  private final ProductService service;

  @PostMapping
  public ProductResponseDto createProduct(
      @RequestBody ProductRequestDto requestDto,
      @AuthenticationPrincipal UserDetailsImpl principal) {
    return service.createProduct(requestDto, principal);
  }

  @PutMapping("/{id}")
  public ProductResponseDto updateProduct(
      @PathVariable("id") Long id, @RequestBody ProductMypriceRequestDto requestDto) {
    return service.updateProduct(id, requestDto);
  }

  @GetMapping
  public List<ProductResponseDto> getProducts(@AuthenticationPrincipal UserDetailsImpl principal) {
    return service.getProducts(principal);
  }
}
