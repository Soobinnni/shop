package site.soobin.myselectshop.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.soobin.myselectshop.dto.ProductMypriceRequestDto;
import site.soobin.myselectshop.dto.ProductRequestDto;
import site.soobin.myselectshop.dto.ProductResponseDto;
import site.soobin.myselectshop.entity.Product;
import site.soobin.myselectshop.naver.dto.ItemDto;
import site.soobin.myselectshop.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductService {
  private static final int MIN_MY_PRICE = 100;
  private final ProductRepository repository;

  public ProductResponseDto createProduct(ProductRequestDto requestDto) {
    Product product = repository.save(new Product(requestDto));
    return new ProductResponseDto(product);
  }

  @Transactional
  public ProductResponseDto updateProduct(Long id, ProductMypriceRequestDto requestDto) {
    // 가격 검사
    if (!isOptimalPrice(requestDto.getMyprice())) {
      throw new IllegalArgumentException("유효하지 않은 관심 가격입니다. 최소 " + MIN_MY_PRICE + "원 이상으로 설정해주세요.");
    }

    // 상품 조회
    Product product = this.getProductById(id);

    // 상품 업데이트
    product.update(requestDto);
    repository.save(product);

    return new ProductResponseDto(product);
  }

  public List<ProductResponseDto> getProducts() {
    return repository.findAll().stream().map(ProductResponseDto::new).toList();
  }

  @Transactional
  public void updateBySearch(Long id, ItemDto requestDto) {
    // 상품 조회
    Product product = this.getProductById(id);

    // 상품 업데이트
    product.updateByItemDto(requestDto);
    repository.save(product);
  }

  private boolean isOptimalPrice(int price) {
    return price >= MIN_MY_PRICE;
  }

  private Product getProductById(Long id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
  }
}
