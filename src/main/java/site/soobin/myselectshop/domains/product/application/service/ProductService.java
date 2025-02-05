package site.soobin.myselectshop.domains.product.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.soobin.myselectshop.commons.security.UserDetailsImpl;
import site.soobin.myselectshop.domains.folder.domain.entity.Folder;
import site.soobin.myselectshop.domains.folder.domain.entity.ProductFolder;
import site.soobin.myselectshop.domains.folder.domain.repository.FolderRepository;
import site.soobin.myselectshop.domains.folder.domain.repository.ProductFolderRepository;
import site.soobin.myselectshop.domains.product.application.dto.ProductMypriceRequestDto;
import site.soobin.myselectshop.domains.product.application.dto.ProductRequestDto;
import site.soobin.myselectshop.domains.product.application.dto.ProductResponseDto;
import site.soobin.myselectshop.domains.product.domain.entity.Product;
import site.soobin.myselectshop.domains.product.domain.repository.ProductRepository;
import site.soobin.myselectshop.domains.product.infrastructure.external.naver.dto.ItemDto;
import site.soobin.myselectshop.domains.user.domain.entity.User;

@Service
@RequiredArgsConstructor
public class ProductService {
  public static final int MIN_MY_PRICE = 100;
  private final ProductRepository productRepository;
  private final FolderRepository folderRepository;
  private final ProductFolderRepository productFolderRepository;

  public ProductResponseDto createProduct(ProductRequestDto requestDto, UserDetailsImpl principal) {
    Product product =
        productRepository.save(new Product(requestDto, getUserFromPrincipal(principal)));
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
    productRepository.save(product);

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
    productRepository.save(product);
  }

  public void addFolder(Long productId, Long folderId, UserDetailsImpl principal) {
    // 필요한 엔티티
    User user = getUserFromPrincipal(principal);
    Product product = getProductById(productId);
    Folder folder = getFolderById(folderId);

    // 회원과의 관계 확인
    if (!isProductOwnedByUserInFolder(user, product, folder)) {
      throw new IllegalArgumentException("회원님의 관심상품이 아니거나, 회원님의 폴더가 아닙니다.");
    }

    // 이미 관심폴더에 상품이 존재하는 지 확인
    if (productFolderRepository.existsByProductAndFolder(product, folder)) {
      throw new IllegalArgumentException("중복된 폴더입니다");
    }

    // 저장
    productFolderRepository.save(new ProductFolder(product, folder));
  }

  public Page<ProductResponseDto> getProductsInFolder(
      UserDetailsImpl principal, Long folderId, int page, int size, String sortBy, boolean isAsc) {
    User user = getUserFromPrincipal(principal);
    Pageable pageable = getProductPageable(page, size, sortBy, isAsc);

    Page<Product> products =
        productRepository.findAllByUserAndProductFolderList_FolderId(user, folderId, pageable);

    return products.map(ProductResponseDto::new);
  }

  private boolean isOptimalPrice(int price) {
    return price >= MIN_MY_PRICE;
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
    return productRepository.findAll(pageable).map(ProductResponseDto::new);
  }

  private Page<ProductResponseDto> getProductsByUser(Pageable pageable, User user) {
    return productRepository.findAllByUser(user, pageable).map(ProductResponseDto::new);
  }

  private boolean isProductOwnedByUserInFolder(User user, Product product, Folder folder) {
    Long userId = user.getId();
    boolean isProductOwnedByUser = product.getUser().getId().equals(userId);
    boolean isFolderOwnedByUser = folder.getUser().getId().equals(userId);

    return isProductOwnedByUser && isFolderOwnedByUser;
  }

  private <T> T getEntityById(Long id, JpaRepository<T, Long> repository, String errorMessage) {
    return repository.findById(id).orElseThrow(() -> new NullPointerException(errorMessage));
  }

  private Product getProductById(Long id) {
    return getEntityById(id, productRepository, "해당 상품 존재하지 않습니다.");
  }

  private Folder getFolderById(Long id) {
    return getEntityById(id, folderRepository, "해당 폴더가 존재하지 않습니다.");
  }
}
