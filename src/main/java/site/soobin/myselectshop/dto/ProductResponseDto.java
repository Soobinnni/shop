package site.soobin.myselectshop.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import site.soobin.myselectshop.entity.Product;

@Getter
@NoArgsConstructor
public class ProductResponseDto {
  private Long id;
  private String title;
  private String link;
  private String image;
  private int lprice;
  private int myprice;

  public ProductResponseDto(Product product) {
    this.id = product.getId();
    this.title = product.getTitle();
    this.link = product.getLink();
    this.image = product.getImage();
    this.lprice = product.getLprice();
    this.myprice = product.getMyprice();
  }
}
