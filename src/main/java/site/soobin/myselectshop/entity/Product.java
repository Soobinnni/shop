package site.soobin.myselectshop.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import site.soobin.myselectshop.dto.ProductMypriceRequestDto;
import site.soobin.myselectshop.dto.ProductRequestDto;
import site.soobin.myselectshop.naver.dto.ItemDto;

@Entity // JPA가 관리할 수 있는 Entity 클래스 지정
@Getter
@Setter
@Table(name = "product") // 매핑할 테이블의 이름을 지정
@NoArgsConstructor
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

  public Product(ProductRequestDto requestDto, User user) {
    this.title = requestDto.getTitle();
    this.image = requestDto.getImage();
    this.link = requestDto.getLink();
    this.lprice = requestDto.getLprice();
    this.user = user;
  }

  public void update(ProductMypriceRequestDto requestDto) {
    this.myprice = requestDto.getMyprice();
  }

  public void updateByItemDto(ItemDto requestDto) {
    this.lprice = requestDto.getLprice();
  }
}
