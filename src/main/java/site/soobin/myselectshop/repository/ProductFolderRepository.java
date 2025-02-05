package site.soobin.myselectshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.soobin.myselectshop.entity.Folder;
import site.soobin.myselectshop.entity.Product;
import site.soobin.myselectshop.entity.ProductFolder;

public interface ProductFolderRepository extends JpaRepository<ProductFolder, Long> {
  boolean existsByProductAndFolder(Product product, Folder folder);
}
