package site.soobin.myselectshop.domains.product.domain.entity;

import static site.soobin.myselectshop.domains.product.domain.ProductConstants.MIN_MY_PRICE;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.soobin.myselectshop.commons.exception.ApiBusinessException;
import site.soobin.myselectshop.commons.jpa.Timestamped;
import site.soobin.myselectshop.domains.product.application.dto.ProductRequestDto;
import site.soobin.myselectshop.domains.product.application.exception.ProductErrorSpec;
import site.soobin.myselectshop.domains.product.infrastructure.external.naver.dto.ItemDto;
import site.soobin.myselectshop.domains.user.domain.entity.User;

@Entity
@Getter
@Builder(access = AccessLevel.PUBLIC, builderMethodName = "innerBuilder")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product") // 매핑할 테이블의 이름을 지정
public class Product extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String image;

  @Column(nullable = false)
  private String link;

  @Column(nullable = false)
  private int lprice;

  @Column(nullable = false)
  private int myprice;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @OneToMany(mappedBy = "product")
  private List<ProductFolder> productFolderList = new ArrayList<>();

  public static ProductBuilder builder(ProductRequestDto requestDto) {
    return innerBuilder()
        .title(requestDto.title())
        .image(requestDto.image())
        .link(requestDto.link())
        .lprice(requestDto.lprice());
  }

  public void updateByItemDto(ItemDto requestDto) {
    this.lprice = requestDto.lprice();
  }

  public void validateAndUpdateMyPrice(int myPrice) {
    if (!isOptimalPrice(myPrice)) {
      throw new ApiBusinessException(ProductErrorSpec.INVALID_PRICE);
    }
    this.myprice = myPrice;
  }

  public void validateOwnership(User user) {
    if (!this.user.getId().equals(user.getId())) {
      throw new ApiBusinessException(ProductErrorSpec.NOT_OWNED_PRODUCT);
    }
  }

  private boolean isOptimalPrice(int price) {
    return price >= MIN_MY_PRICE;
  }
}
