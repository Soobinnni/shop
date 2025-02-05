package site.soobin.myselectshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.soobin.myselectshop.dto.ProductMypriceRequestDto;
import site.soobin.myselectshop.dto.ProductRequestDto;
import site.soobin.myselectshop.dto.ProductResponseDto;
import site.soobin.myselectshop.naver.service.ProductService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController {
  private final ProductService service;

  @PostMapping("/products")
  public ProductResponseDto createProduct(@RequestBody ProductRequestDto requestDto) {
    return service.createProduct(requestDto);
  }

  @PutMapping("/products/{id}")
  public ProductResponseDto updateProduct(
      @PathVariable("id") Long id, @RequestBody ProductMypriceRequestDto requestDto) {
    return service.updateProduct(id, requestDto);
  }
}
