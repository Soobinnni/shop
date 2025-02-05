package site.soobin.myselectshop.domains.folder.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.soobin.myselectshop.domains.folder.domain.entity.Folder;
import site.soobin.myselectshop.domains.folder.domain.entity.ProductFolder;
import site.soobin.myselectshop.domains.product.domain.entity.Product;

public interface ProductFolderRepository extends JpaRepository<ProductFolder, Long> {
  boolean existsByProductAndFolder(Product product, Folder folder);
}
