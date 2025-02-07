package site.soobin.myselectshop.domains.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import site.soobin.myselectshop.commons.exception.ApiBusinessException;
import site.soobin.myselectshop.commons.utils.PageableFactory;
import site.soobin.myselectshop.domains.folder.application.service.FolderService;
import site.soobin.myselectshop.domains.product.application.dto.ProductMypriceRequestDto;
import site.soobin.myselectshop.domains.product.application.dto.ProductRequestDto;
import site.soobin.myselectshop.domains.product.application.dto.ProductResponseDto;
import site.soobin.myselectshop.domains.product.application.exception.ProductErrorSpec;
import site.soobin.myselectshop.domains.product.application.service.ProductDomainService;
import site.soobin.myselectshop.domains.product.application.service.ProductService;
import site.soobin.myselectshop.domains.product.domain.ProductConstants;
import site.soobin.myselectshop.domains.product.domain.entity.Product;
import site.soobin.myselectshop.domains.product.domain.repository.ProductFolderRepository;
import site.soobin.myselectshop.domains.product.domain.repository.ProductRepository;
import site.soobin.myselectshop.domains.user.domain.entity.User;
import site.soobin.myselectshop.domains.user.domain.entity.UserRoleEnum;

@ExtendWith(MockitoExtension.class) // @Mock 사용을 위해 설정합니다.
class ProductServiceTest {

  @Mock ProductRepository productRepository;
  @Mock ProductFolderRepository productFolderRepository;
  @Mock
  ProductDomainService domainService;

  @Mock FolderService folderService;
  @Mock PageableFactory pageableFactory;

  @Test
  @DisplayName("관심 상품 희망가 - 최저가 이상으로 변경")
  void test1() {
    // given
    Long productId = 100L;
    int myprice = ProductConstants.MIN_MY_PRICE + 3_000_000;

    ProductMypriceRequestDto requestMyPriceDto = new ProductMypriceRequestDto(myprice);

    User user = User.builder("name", "hashed-pwd", "asdf@asdf.com", UserRoleEnum.USER).build();
    ProductRequestDto requestProductDto =
        new ProductRequestDto(
            "Apple <b>맥북</b> <b>프로</b> 16형 2021년 <b>M1</b> Max 10코어 실버 (MK1H3KH/A) ",
            "https://shopping-phinf.pstatic.net/main_2941337/29413376619.20220705152340.jpg",
            "https://search.shopping.naver.com/gate.nhn?id=29413376619",
            3515000);

    Product product = Product.builder(requestProductDto).user(user).build();

    ProductService productService =
        new ProductService(
            productRepository,
            productFolderRepository,
            domainService,
            folderService,
            pageableFactory);

    given(productRepository.findById(productId)).willReturn(Optional.of(product));

    // when
    ProductResponseDto result = productService.updateProduct(productId, requestMyPriceDto);

    // then
    assertEquals(myprice, result.myprice());
  }

  @Test
  @DisplayName("관심 상품 희망가 - 최저가 미만으로 변경")
  void test2() {
    // given
    Long productId = 200L;
    int myprice = ProductConstants.MIN_MY_PRICE + 3_000_000;

    ProductMypriceRequestDto requestMyPriceDto = new ProductMypriceRequestDto(myprice);

    ProductService productService =
        new ProductService(
            productRepository,
            productFolderRepository,
            domainService,
            folderService,
            pageableFactory);

    // when
    Exception exception =
        assertThrows(
            ApiBusinessException.class,
            () -> {
              productService.updateProduct(productId, requestMyPriceDto);
            });

    // then
    assertEquals(ProductErrorSpec.NO_PRODUCT.getMessage(), exception.getMessage());
  }
}
