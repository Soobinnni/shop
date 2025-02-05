package site.soobin.myselectshop.domains.product.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.soobin.myselectshop.commons.exception.ApiBusinessException;
import site.soobin.myselectshop.commons.security.UserDetailsImpl;
import site.soobin.myselectshop.commons.utils.PageableFactory;
import site.soobin.myselectshop.domains.folder.application.exception.FolderErrorSpec;
import site.soobin.myselectshop.domains.folder.domain.entity.Folder;
import site.soobin.myselectshop.domains.folder.domain.entity.ProductFolder;
import site.soobin.myselectshop.domains.folder.domain.repository.FolderRepository;
import site.soobin.myselectshop.domains.folder.domain.repository.ProductFolderRepository;
import site.soobin.myselectshop.domains.product.application.dto.ProductMypriceRequestDto;
import site.soobin.myselectshop.domains.product.application.dto.ProductRequestDto;
import site.soobin.myselectshop.domains.product.application.dto.ProductResponseDto;
import site.soobin.myselectshop.domains.product.application.exception.ProductErrorSpec;
import site.soobin.myselectshop.domains.product.domain.entity.Product;
import site.soobin.myselectshop.domains.product.domain.repository.ProductRepository;
import site.soobin.myselectshop.domains.product.infrastructure.external.naver.dto.ItemDto;
import site.soobin.myselectshop.domains.user.domain.entity.User;

@Service
@RequiredArgsConstructor
public class ProductService {
  private final ProductRepository productRepository;
  private final FolderRepository folderRepository;
  private final ProductFolderRepository productFolderRepository;
  private final ProductDomainService domainService;

  private final PageableFactory pageableFactory;

  public ProductResponseDto createProduct(ProductRequestDto requestDto, UserDetailsImpl principal) {
    Product product = productRepository.save(new Product(requestDto, principal.getUser()));
    return new ProductResponseDto(product);
  }

  @Transactional
  public ProductResponseDto updateProduct(Long id, ProductMypriceRequestDto requestDto) {
    Product product = getProductById(id);
    product.validateAndUpdateMyPrice(requestDto.getMyprice());
    return new ProductResponseDto(product);
  }

  @Transactional(readOnly = true)
  public Page<ProductResponseDto> getProducts(
      UserDetailsImpl principal, int page, int size, String sortBy, boolean isAsc) {
    User user = principal.getUser();
    Pageable pageable = pageableFactory.create(page, size, sortBy, isAsc);

    return switch (user.getRole()) {
      case ADMIN -> getAllProducts(pageable);
      case USER -> getProductsByUser(pageable, user);
    };
  }

  @Transactional
  public void updateBySearch(Long id, ItemDto requestDto) {
    Product product = getProductById(id);
    product.updateByItemDto(requestDto);
    productRepository.save(product);
  }

  @Transactional
  public void addFolder(Long productId, Long folderId, UserDetailsImpl principal) {
    User user = principal.getUser();
    Product product = getProductById(productId);
    Folder folder = getFolderById(folderId);

    domainService.validateAndAddToFolder(product, folder, user);

    if (productFolderRepository.existsByProductAndFolder(product, folder)) {
      throw new ApiBusinessException(ProductErrorSpec.DUPLICATE_PRODUCT_IN_FOLDER);
    }
    productFolderRepository.save(new ProductFolder(product, folder));
  }

  public Page<ProductResponseDto> getProductsInFolder(
      UserDetailsImpl principal, Long folderId, int page, int size, String sortBy, boolean isAsc) {
    User user = principal.getUser();
    Pageable pageable = pageableFactory.create(page, size, sortBy, isAsc);
    return productRepository
        .findAllByUserAndProductFolderList_FolderId(user, folderId, pageable)
        .map(ProductResponseDto::new);
  }

  private Page<ProductResponseDto> getAllProducts(Pageable pageable) {
    return productRepository.findAll(pageable).map(ProductResponseDto::new);
  }

  private Page<ProductResponseDto> getProductsByUser(Pageable pageable, User user) {
    return productRepository.findAllByUser(user, pageable).map(ProductResponseDto::new);
  }

  private Product getProductById(Long id) {
    return productRepository
        .findById(id)
        .orElseThrow(() -> new ApiBusinessException(ProductErrorSpec.NO_PRODUCT));
  }

  private Folder getFolderById(Long id) {
    return folderRepository
        .findById(id)
        .orElseThrow(() -> new ApiBusinessException(FolderErrorSpec.NO_FOLDER));
  }
}
