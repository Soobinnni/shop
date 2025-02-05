package site.soobin.myselectshop.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import site.soobin.myselectshop.entity.Product;
import site.soobin.myselectshop.entity.User;

public interface ProductRepository extends JpaRepository<Product, Long> {

  List<Product> findAllByUser(User user);
}
