package site.soobin.myselectshop.naver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.soobin.myselectshop.dto.ProductRequestDto;
import site.soobin.myselectshop.dto.ProductResponseDto;
import site.soobin.myselectshop.entity.Product;
import site.soobin.myselectshop.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductService {
  private final ProductRepository repository;

  public ProductResponseDto createProduct(ProductRequestDto requestDto) {
    Product product = repository.save(new Product(requestDto));
    return new ProductResponseDto(product);
  }
}
