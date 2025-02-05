package site.soobin.myselectshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.soobin.myselectshop.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {}
