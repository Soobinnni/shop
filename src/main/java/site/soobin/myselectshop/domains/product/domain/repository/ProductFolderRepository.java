package site.soobin.myselectshop.domains.product.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.soobin.myselectshop.domains.folder.domain.entity.Folder;
import site.soobin.myselectshop.domains.product.domain.entity.Product;
import site.soobin.myselectshop.domains.product.domain.entity.ProductFolder;

public interface ProductFolderRepository extends JpaRepository<ProductFolder, Long> {
  boolean existsByProductAndFolder(Product product, Folder folder);
}
