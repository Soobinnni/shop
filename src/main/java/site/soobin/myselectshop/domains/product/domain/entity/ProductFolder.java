package site.soobin.myselectshop.domains.product.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import site.soobin.myselectshop.domains.folder.domain.entity.Folder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "product_folder")
public class ProductFolder {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "folder_id", nullable = false)
  private Folder folder;

  public ProductFolder(Product product, Folder folder) {
    this.product = product;
    this.folder = folder;
  }
}
