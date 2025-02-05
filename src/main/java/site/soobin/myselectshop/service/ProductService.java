package site.soobin.myselectshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.soobin.myselectshop.dto.ProductMypriceRequestDto;
import site.soobin.myselectshop.dto.ProductRequestDto;
import site.soobin.myselectshop.dto.ProductResponseDto;
import site.soobin.myselectshop.entity.Product;
import site.soobin.myselectshop.entity.User;
import site.soobin.myselectshop.naver.dto.ItemDto;
import site.soobin.myselectshop.repository.ProductRepository;
import site.soobin.myselectshop.security.UserDetailsImpl;

@Service
@RequiredArgsConstructor
public class ProductService {
  public static final int MIN_MY_PRICE = 100;
  private final ProductRepository repository;

  public ProductResponseDto createProduct(ProductRequestDto requestDto, UserDetailsImpl principal) {
    Product product = repository.save(new Product(requestDto, getUserFromPrincipal(principal)));
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

  @Transactional(readOnly = true)
  public Page<ProductResponseDto> getProducts(
      UserDetailsImpl principal, int page, int size, String sortBy, boolean isAsc) {
    User user = getUserFromPrincipal(principal);
    Pageable pageable = getProductPageable(page, size, sortBy, isAsc);

    return switch (user.getRole()) {
      case ADMIN -> getAllProducts(pageable);
      case USER -> getProductsByUser(pageable, user);
    };
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

  private User getUserFromPrincipal(UserDetailsImpl principal) {
    return principal.getUser();
  }

  private Pageable getProductPageable(int page, int size, String sortBy, boolean isAsc) {
    Sort.Direction direction = isAsc ? Sort.Direction.ASC : Direction.DESC;
    Sort sort = Sort.by(direction, sortBy);

    return PageRequest.of(page, size, sort);
  }

  private Page<ProductResponseDto> getAllProducts(Pageable pageable) {
    return repository.findAll(pageable).map(ProductResponseDto::new);
  }

  private Page<ProductResponseDto> getProductsByUser(Pageable pageable, User user) {
    return repository.findAllByUser(user, pageable).map(ProductResponseDto::new);
  }
}
