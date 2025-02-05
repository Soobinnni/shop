package site.soobin.myselectshop.domains.product.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import site.soobin.myselectshop.domains.product.domain.entity.Product;
import site.soobin.myselectshop.domains.user.domain.entity.User;

public interface ProductRepository extends JpaRepository<Product, Long> {

  Page<Product> findAllByUser(User user, Pageable pageable);

  Page<Product> findAllByUserAndProductFolderList_FolderId(
      User user, Long folderId, Pageable pageable);
}
