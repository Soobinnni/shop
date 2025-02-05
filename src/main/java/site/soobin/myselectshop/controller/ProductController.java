package site.soobin.myselectshop.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
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
import site.soobin.myselectshop.naver.service.ProductService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
  private final ProductService service;

  @PostMapping
  public ProductResponseDto createProduct(@RequestBody ProductRequestDto requestDto) {
    return service.createProduct(requestDto);
  }

  @PutMapping("/{id}")
  public ProductResponseDto updateProduct(
      @PathVariable("id") Long id, @RequestBody ProductMypriceRequestDto requestDto) {
    return service.updateProduct(id, requestDto);
  }

  @GetMapping
  public List<ProductResponseDto> getProducts() {
    return service.getProducts();
  }
}
